(() => {
  "use strict";

  describe("schemata Validator", () => {
    var Validator, $httpBackend;
    var schema = {
      "$schema": "http://json-schema.org/draft-04/hyper-schema",
    type: ["object"],
    id: "example",
    title: "Example hyper schema",
    description: "An example JSON Hyper Schema",
    links: [{href: "http://example.org", rel: "self"}],
    properties: {
      welcome: {
        "$ref": "#/definitions/welcome"
      }
    },
    definitions: {
      welcome: {
        "$schema": "http://json-schema.org/draft-04/hyper-schema",
    title: "Welcome",
    description: "An example welcome resource",
    type: ["object"],
    definitions: {
      name: {
        description: "User's name",
        readOnly: true,
        type: ["string"]
      },
      greeting: {
        description: "Welcome's greeting word",
        readOnly: true,
        type: ["string"]
      },
      message: {
        description: "Welcome message",
        readOnly: true,
        type: ["string"]
      }
    },
    properties: {
      message: {"$ref": "#/definitions/welcome/definitions/message"}
    },
    required: ["message"],
    links: [
    {
      description: "Default welcome",
      href: "/welcome",
      method: "GET",
      rel: "self",
      schema: {
        type: ["object"],
        properties: {}
      }
    },
    {
      description: "All welcomes",
      href: "/welcomes",
      method: "GET",
      rel: "instances",
      schema: {
        type: ["object"],
        properties: {}
      }
    },
    {
      description: "Custom welcome",
      href: "/welcome/user",
      method: "POST",
      rel: "create",
      schema: {
        type: ["object"],
        properties: {
          name: {"$ref": "#/definitions/welcome/definitions/name"},
          greeting: {"$ref": "#/definitions/welcome/definitions/greeting"}
        },
        required: ["name"]
      }
    }
      ]
      }
    }
    };

    beforeEach(module("application"));

    beforeEach(angular.mock.http.init);
    afterEach(angular.mock.http.reset);

    beforeEach((done) => {
      inject((_Validator_, _$httpBackend_) => {
        $httpBackend = _$httpBackend_;
        $httpBackend.whenGET(/.+\.html/).respond("");
        $httpBackend.whenGET("/base/build/assets/json/hyper-schema.json").passThrough();

        Validator = _Validator_;
        Validator.setSchema("/base/build/", schema, (ok) => {
          if (!ok) { throw "Unable to set schema"; }
          done();
        });
      });
    });

    describe("request validation", () => {
      it("passes for link without params", () => {
        var v = Validator.request("welcome", "GET", "/welcome", {});

        expect(v).toEqual(true);
        expect(Validator.error.message).toEqual("");
      });

      it("passes for link with params", () => {
        var v = Validator.request("welcome", "POST", "/welcome/user", {
          name: "user",
          greeting: "hey"
        });

        expect(v).toEqual(true);
        expect(Validator.error.message).toEqual("");
      });

      it("fails for link with missing required param", () => {
        var v = Validator.request("welcome", "POST", "/welcome/user", {
          greeting: "hey"
        });
        expect(v).toEqual(false);
        expect(Validator.error.message).toEqual("should have required property 'name'");
      });

      it("fails when link not found", () => {
        var v = Validator.request("welcome", "GET", "/stuff", {});
        expect(v).toEqual(false);
        expect(Validator.error.message).toEqual("Link GET /stuff is not found for resource welcome");
      });

      it("fails when resource not found", () => {
        var v = Validator.request("nope", "GET", "/stuff", {});
        expect(v).toEqual(false);
        expect(Validator.error.message).toEqual("Resource nope is not found");
      });
    });

    describe("response validation", () => {
      it("passes for link with correct response", () => {
        var v = Validator.response("welcome", "GET", "/welcome", {message: "hello"});
        expect(v).toEqual(true);
        expect(Validator.error.message).toEqual("");
      });

      it("passes for link with rel=instances", () => {
        var v = Validator.response("welcome", "GET", "/welcomes", [
          {message: "hello"},
          {message: "hi, world"}
        ]);

        expect(v).toEqual(true);
        expect(Validator.error.message).toEqual("");
      });

      it("fails for link with incorrect response", () => {
        var v = Validator.response("welcome", "GET", "/welcome", {stuff: "hello"});
        expect(v).toEqual(false);
        expect(Validator.error.message).toEqual("should have required property 'message'");
      });

      it("fails for link with rel=instances and non-array response", () => {
        var v = Validator.response("welcome", "GET", "/welcomes", {message: "hello"});
        expect(v).toEqual(false);
        expect(Validator.error.message).toEqual("should be array");
      });

      it("fails for link with rel=instances and incorrect objects in array", () => {
        var v = Validator.response("welcome", "GET", "/welcomes", [
          {some: "stuff"},
          {message: "hello"},
          {another: "stuff"}
        ]);

        expect(v).toEqual(false);
        expect(Validator.error.message).toEqual("should have required property 'message'");
      });
    });
  });
})();
