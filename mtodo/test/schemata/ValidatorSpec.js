(() => {
  "use strict";

  describe("schemata Validator", () => {
    var Validator;
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

    beforeEach(inject((_Validator_) => {
      Validator = _Validator_;
    }));

    describe("request validation", () => {
      it("passes for link without params", () => {
        var v = Validator.request("GET /welcome", {});
        expect(v).toEqual(true);
      });

      it("passes for link with params", () => {
        var v = Validator.request("POST /welcome/user", {
          name: "user",
          greeting: "hey"
        });
        expect(v).toEqual(true);
      });

      it("passes for link with missing required param", () => {
        var v = Validator.request("POST /welcome/user", {
          greeting: "hey"
        });
        expect(v).toEqual(false);
        expect(Validator.error.message).toEqual("Stub error message")
      });
    });
  });
})();
