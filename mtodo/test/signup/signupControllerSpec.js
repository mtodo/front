(() => {
  "use strict";

  describe("SignupController", () => {
    var Validator, SignupController, httpBackend;

    beforeEach(module("application"));

    beforeEach(angular.mock.http.init);
    afterEach(angular.mock.http.reset);

    beforeEach(inject((_Validator_, $controller, $rootScope, $injector) => {
      Validator = _Validator_;
      httpBackend = $injector.get("$httpBackend");

      var scope = $rootScope.$new();
      SignupController = $controller("SignupController", { $scope: scope });
      SignupController.schemaUrlPrefix = "/base/build/";

      httpBackend.whenGET(/templates\/.+\.html/).respond("");
      httpBackend.whenGET("/base/build/assets/json/hyper-schema.json").passThrough();
      httpBackend.whenGET("/base/build/assets/json/schemata.json").passThrough();
    }));

    afterEach(() => {
      httpBackend.verifyNoOutstandingExpectation();
      httpBackend.verifyNoOutstandingRequest();
    });

    it("signs new user up", (done) => {
      SignupController.email = "john@example.org";
      SignupController.password = "welcome";
      SignupController.confirm = "welcome";

      httpBackend.expectPOST("/api/signup", {
        email: "john@example.org",
        password: "welcome",
        confirm: "welcome"
      }).respond({});

      SignupController.signup((ok) => {
        expect(ok).toEqual(true);

        httpBackend.flush();

        expect(Validator.error.message).toEqual("");
      });
    });
  });
})();
