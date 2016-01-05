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
