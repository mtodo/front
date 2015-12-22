(() => {
  "use strict";

  describe("FirstTodo model", () => {
    var rootScope, FirstTodo;

    beforeEach(module("application"));

    beforeEach(inject(($injector, _FirstTodo_) => {
      FirstTodo = _FirstTodo_;
      rootScope = $injector.get("$rootScope");
      spyOn(rootScope, "$broadcast");
    }));

    it("is possible to update the title", () => {
      FirstTodo.updateTitle("Feed my cats");
      expect(FirstTodo.title).toEqual("Feed my cats");
    });

    it("updating first todo's title broadcasts event", () => {
      FirstTodo.updateTitle("Feed my dogs");
      expect(rootScope.$broadcast).toHaveBeenCalledWith(
        "FirstTodo::titleUpdated", "Feed my dogs"
      );
    });
  });

  describe("FirstTodoController", () => {
    var FirstTodo, FirstTodoController, scope;

    beforeEach(module("application"));

    beforeEach(inject((_FirstTodo_, $controller, $rootScope) => {
      FirstTodo = _FirstTodo_;
      scope = $rootScope.$new();
      FirstTodoController = $controller("FirstTodoController", { $scope: scope });
    }));

    it("updates when FirstTodo is updated", () => {
      FirstTodo.updateTitle("Feed my precious cats!");
      expect(scope.title).toEqual(
        "Feed my precious cats!"
      );
    });

    it("updates FirstTodo's title when updated", () => {
      scope.title = "Feed my awesome dogs!";
      scope.updateTitle();
      expect(FirstTodo.title).toEqual(
        "Feed my awesome dogs!"
      );
    });
  });
})();
