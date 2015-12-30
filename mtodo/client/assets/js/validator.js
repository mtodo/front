(function() {
  'use strict';

  var app = angular.module("application");

  app.service("Validator", ["$http", function($http) {
    this.resources = {};
    this.error = {message: ""};
    this.ajv = {};

    this.setSchema = function(urlPrefix, schema, done) {
      var that = this;

      that.resources = {};
      for (var resource in schema.definitions) {
        if (!Object.prototype.hasOwnProperty.call(schema.definitions, resource)) { continue; }
        that.addResource(resource, schema.definitions[resource]);
      }

      $http({method: "GET", url: urlPrefix + "assets/json/hyper-schema.json"}).then(function success(response) {
        that.ajv = Ajv();
        that.ajv.addMetaSchema(response.data, "http://json-schema.org/draft-04/hyper-schema", true);
        that.ajv.compile(schema);
        done(true);
      }, function failure(response) {
        done(false);
      });
    };

    this.addResource = function(name, schema) {
      this.resources[name] = Jsonary.createSchema(schema);
    };

    this.request = function(resource, method, href, rawData) {
      var resources = this.resources,
          error = this.error;

      error.message = "";

      if (!Object.prototype.hasOwnProperty.call(resources, resource)) {
        error.message = "Resource " + resource + " is not found";
        return false;
      }

      var data = Jsonary.create(rawData);
      data.addSchema(resources[resource]);

      var foundLink, links = data.links();
      for (var i = 0; i < links.length; i++) {
        var link = links[i];
        if (link.method === method && link.href === href) {
          foundLink = link;
        }
      }

      if (!foundLink) {
        error.message = "Link " + method + " " + href + " is not found for resource " + resource;
        return false;
      }

      return true;
    };
  }]);
})();
