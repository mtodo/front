(function() {
  'use strict';

  var app = angular.module("application");

  app.service("Validator", ["$http", function($http) {
    this.resources = {};
    this.error = {};
    this.ajv = {};

    this.setSchema = function(schema) {
      this.resources = {};
      for (var resource in schema.definitions) {
        if (!Object.prototype.hasOwnProperty(schema.definitions, resource)) { continue; }
        this.addResource(resource, schema.definitions[resource]);
      }

      $http({method: "GET", url: "/assets/json/hyper-schema.json"}).then(function success(response) {
        this.ajv = Ajv();
        this.ajv.addMetaSchema(JSON.parse(response), "http://json-schema.org/draft-04/hyper-schema");
        this.ajv.compile(schema);
      }, function failure(response) {});
    };

    this.addResource = function(name, schema) {
    };

    this.request = function(resource, method, href, data) {
      return true;
    };
  }]);
})();
