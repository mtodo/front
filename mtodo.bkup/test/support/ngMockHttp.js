(() => {
  "use strict";

  angular.mock.http = {
    init: () => {
      angular.module("ngMock", ["ng", "ngMockE2E"]).provider({
        $exceptionHandler: angular.mock.$ExceptionHandlerProvider,
        $log: angular.mock.$LogProvider,
        $interval: angular.mock.$IntervalProvider,
        $rootElement: angular.mock.$RootElementProvider,
      }).config(["$provide", ($provide) => {
        $provide.decorator("$timeout", angular.mock.$TimeoutDecorator);
        $provide.decorator("$$rAF", angular.mock.$RAFDecorator);
        $provide.decorator("$rootScope", angular.mock.$RootScopeDecorator);
        $provide.decorator("$controller", angular.mock.$ControllerDecorator);
      }]);
    },

    reset: () => {
      angular.module('ngMock', ['ng']).provider({
        $browser: angular.mock.$BrowserProvider,
        $exceptionHandler: angular.mock.$ExceptionHandlerProvider,
        $log: angular.mock.$LogProvider,
        $interval: angular.mock.$IntervalProvider,
        $httpBackend: angular.mock.$HttpBackendProvider,
        $rootElement: angular.mock.$RootElementProvider
      }).config(['$provide', function($provide) {
        $provide.decorator('$timeout', angular.mock.$TimeoutDecorator);
        $provide.decorator('$$rAF', angular.mock.$RAFDecorator);
        $provide.decorator('$rootScope', angular.mock.$RootScopeDecorator);
        $provide.decorator('$controller', angular.mock.$ControllerDecorator);
      }]);
    },
  };
})();
