(function() {
  'use strict';

  var app = angular.module('application', [
    'ui.router',
    'ngAnimate',

    //foundation
    'foundation',
    'foundation.dynamicRouting',
    'foundation.dynamicRouting.animations'
  ]);

  app
    .config(config)
    .run(run)
  ;

  config.$inject = ['$urlRouterProvider', '$locationProvider'];

  function config($urlProvider, $locationProvider) {
    $urlProvider.otherwise('/');

    $locationProvider.html5Mode({
      enabled:false,
      requireBase: false
    });

    $locationProvider.hashPrefix('!');
  }

  function run() {
    FastClick.attach(document.body);
  }

  app.directive("back", ["$window", function($window) {
    return {
      restrict: "A",
      link: function(scope, elem, attrs) {
        elem.bind("click", function() {
          $window.history.back();
        });
      }
    }
  }]);

})();

(function() {
  'use strict';

  var app = angular.module("application");

  app.service("FirstTodo", ["$rootScope", function($rootScope) {
    this.title = "";
    this.updateTitle = function(newTitle) {
      this.title = newTitle;
      $rootScope.$broadcast("FirstTodo::titleUpdated", newTitle);
    };
  }]);

  app.controller("FirstTodoController", ["$scope", "FirstTodo", function($scope, FirstTodo) {
    $scope.title = FirstTodo.title;

    $scope.updateTitle = function() {
      FirstTodo.updateTitle($scope.title);
    };

    $scope.$on("FirstTodo::titleUpdated", function(e) {
      $scope.title = FirstTodo.title;
    });
  }]);
})();

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
        console.log("http get success");
        that.ajv = Ajv();
        that.ajv.addMetaSchema(response.data, "http://json-schema.org/draft-04/hyper-schema", true);
        console.log("Added meta schema");
        that.ajv.compile(schema);
        done(true);
      }, function failure(response) {
        console.log("http get failure");
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
