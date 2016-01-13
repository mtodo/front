(ns mtodo.signup-test
    (:require [cljs.test :refer-macros [is are deftest testing use-fixtures]]
              [mtodo.test-helper :refer [with-mounted-component found-in contained-in]]
              [mtodo.containers.signup :as signup]))

(deftest test-signup
  (let [john {:email "john.smith@example.org" :password "welcome" :confirm "welcome"}]

    (testing "root"
             (with-mounted-component (signup/root john)
               (fn [c div]
                   (is (found-in #"Signup" div)))))

    (testing "form"
             (with-mounted-component (signup/form john)
               (fn [c div]
                   (is (found-in #"<form[^>]+>" div))
                   (is (found-in #"<a[^>]+>Start achieving" div)))))

    (testing "root has form"
             (is (-> [signup/form john] (contained-in (signup/root john)))))

    (testing "email"
             (with-mounted-component (signup/email "john.smith@example.org")
               (fn [c div]
                   (is (found-in #"<input[^>]+type=\"email\"[^>]+name=\"email\"[^>]+value=\"john\.smith@example\.org\"" div)))))

    (testing "password"
             (with-mounted-component (signup/password "welcome")
               (fn [c div]
                   (is (found-in #"<input[^>]+type=\"password\"[^>]+name=\"password\"[^>]+value=\"welcome\"" div)))))

    (testing "confirm"
             (with-mounted-component (signup/confirm "welcome")
               (fn [c div]
                   (is (found-in #"<input[^>]+type=\"password\"[^>]+name=\"confirm\"[^>]+value=\"welcome\"" div)))))

    (testing "form has email"
             (is (-> [signup/email "john.smith@example.org"] (contained-in (signup/form {:email "john.smith@example.org"})))))

    (testing "form has password"
             (is (-> [signup/password "welcome"] (contained-in (signup/form {:password "welcome"})))))

    (testing "form has confirm"
             (is (-> [signup/confirm "welcome"] (contained-in (signup/form {:confirm "welcome"})))))

    ))
