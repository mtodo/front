(function() {
  'use strict';

  var app = angular.module("application");

  app.service("Validator", ["$http", function($http) {
    this.resources = {};
    this.error = {};
    this.ajv = {};

    this.setSchema = function(urlPrefix, schema, done) {
      var that = this;

      that.resources = {};
      for (var resource in schema.definitions) {
        if (!Object.prototype.hasOwnProperty(schema.definitions, resource)) { continue; }
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
    };

    this.request = function(resource, method, href, data) {
      return true;
    };
  }]);
})();
