(function() {
  'use strict';

  var app = angular.module("application");

  app.controller("SignupController", ["$resource", "Validator", function($resource, Validator) {
    this.Signup = $resource("/api/signup", {
      //email: "@email",
      //password: "@password",
      //confirm: "@confirm"
    }, {
      save: {
        method: "POST",
        params: {},
        transformRequest: [function(data, header) {
          console.log(data);
          var v = Validator.request("signup", "POST", "/signup", data);
          if (!v) {
            console.log(Validator.error);
            throw Validator.error.message;
          }
          return data;
        }, angular.toJson],
      }
    });

    this.schemaUrlPrefix = "/";

    this.email = "";
    this.password = "";
    this.confirm = "";

    this.signup = function(done) {
      var that = this;

      Validator.setDefaultSchema(that.schemaUrlPrefix, function(ok) {
        if (!ok) {
          console.log("Unable to init schema validator :(");
          if (done) { done(false); }
          throw "Unable to init schema validator";
        }

        var newSignup = new that.Signup({
          email: that.email,
          password: that.password,
          confirm: that.confirm
        });

        newSignup.$save();
        if (done) { done(true); }
      });
    };
  }]);
})();
