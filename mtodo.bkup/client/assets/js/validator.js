(function() {
  'use strict';

  var app = angular.module("application");

  app.service("Validator", ["$http", function($http) {
    this.resources = {};
    this.error = {message: ""};
    this.ajv = {};
    this.compiled = {};
    this.alreadySet = false;

    this.setDefaultSchema = function(urlPrefix, done) {
      var that = this;

      if (that.alreadySet) {
        done(true);
        return;
      }

      $http({method: "GET", url: urlPrefix + "assets/json/schemata.json"})
        .then(function success(response) {
          that.setSchema(urlPrefix, response.data, done);
        }, function failure(response) {
          done(false);
        });
    };

    this.setSchema = function(urlPrefix, schema, done) {
      var that = this;

      if (that.alreadySet) {
        done(true);
        return;
      }

      that.resources = {};
      for (var resource in schema.definitions) {
        if (!this.hasKey(schema.definitions, resource)) { continue; }
        that.addResource(resource, schema.definitions[resource]);
      }

      $http({method: "GET", url: urlPrefix + "assets/json/hyper-schema.json"}).then(function success(response) {
        that.ajv = Ajv();
        that.ajv.addMetaSchema(response.data, "http://json-schema.org/draft-04/hyper-schema", true);
        that.ajv.addSchema(schema, "schemata");
        that.alreadySet = true;
        done(true);
      }, function failure(response) {
        done(false);
      });
    };

    this.addResource = function(name, schema) {
      this.resources[name] = Jsonary.createSchema(schema);
    };

    this.request = function(resource, method, href, rawData) {
      this.error.message = "";

      var found = this.findLink(resource, method, href, rawData);
      if (!found) {
        return false;
      }

      var validate = this.ajvCompile(
        "schemata#/definitions/" + resource + "/links/" + found.idx + "/schema",
        "self"
      );

      var valid = validate(rawData);
      if (!valid) {
        this.error = validate.errors[0];
      }

      return valid;
    };

    this.response = function(resource, method, href, rawData) {
      this.error.message = "";

      var found = this.findLink(resource, method, href, rawData);
      if (!found) {
        return false;
      }

      var validate = this.ajvCompile(
        "schemata#/definitions/" + resource,
        found.link.rel
      );

      var valid = validate(rawData);
      if (!valid) {
        this.error = validate.errors[0];
      }

      return valid;
    };

    this.findLink = function(resource, method, href, rawData) {
      var resources = this.resources,
          error = this.error;

      if (!this.hasKey(resources, resource)) {
        error.message = "Resource " + resource + " is not found";
        return false;
      }

      var data = Jsonary.create(rawData);
      data.addSchema(resources[resource]);

      var foundLink, foundIdx, links = data.links();
      for (var i = 0; i < links.length; i++) {
        var link = links[i];
        if (link.method === method && link.href === href) {
          foundLink = link;
          foundIdx = i;
        }
      }

      if (!foundLink) {
        error.message = "Link " + method + " " + href + " is not found for resource " + resource;
        return false;
      }

      return {
        link: foundLink,
        idx: foundIdx
      };
    };

    this.selfLinkRel = function(ref) { return {"$ref": ref}; };
    this.instancesLinkRel = function(ref) { return {
      type: ["array"],
      items: { "$ref": ref }
    } }

    this.linkRels = {
      self: this.selfLinkRel,
      create: this.selfLinkRel,
      instances: this.instancesLinkRel
    };

    this.ajvCompile = function(ref, rel) {
      var key = rel + "://" + ref;

      if (this.hasKey(this.compiled, key)) {
        return this.compiled[key];
      }

      this.compiled[key] = this.ajv.compile(
        this.linkRels[rel](ref)
      );

      return this.compiled[key];
    };

    this.hasKey = function(obj, key) {
      return Object.prototype.hasOwnProperty.call(obj, key);
    };
  }]);
})();
