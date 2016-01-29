(ns ^:figwheel-load mtodo.signup-test
    (:require [cljs.test :refer-macros [is are deftest testing use-fixtures async]]
              [mtodo.test-helper :refer [found-in contained-in click! edit!]]
              [mtodo.test-macro :refer-macros [mount]]
              [mtodo.containers.signup :as signup]))

(deftest test-signup
  (let [john {:email "john.smith@example.org" :password "welcome" :confirm "welcome"}]

    (testing "root"
             (mount (signup/root john) [c div]
                   (is (-> [:h1 {} "Signup"] (found-in div)))))

    (testing "form"
             (mount (signup/form john) [c div]
                   (is (-> [:form {}] (found-in div)))
                   (is (-> [:a {} "Start achieving"] (found-in div)))))

    (testing "form has submit button"
             (is (-> [signup/submit john] (contained-in (signup/form john)))))

    (testing "root has form"
             (is (-> [signup/form john] (contained-in (signup/root john)))))

    (testing "email"
             (mount (signup/email "john.smith@example.org") [c div]
                   (is (-> [:input {:type "email" :name "email" :value "john.smith@example.org"}] (found-in div)))))

    (testing "password"
             (mount (signup/password "welcome") [c div]
                   (is (-> [:input {:type "password" :name "password" :value "welcome"}] (found-in div)))))

    (testing "confirm"
             (mount (signup/confirm "welcome") [c div]
                   (is (-> [:input {:type "password" :name "confirm" :value "welcome"}] (found-in div)))))

    (testing "form has email"
             (is (-> [signup/email "john.smith@example.org"] (contained-in (signup/form {:email "john.smith@example.org"})))))

    (testing "form has password"
             (is (-> [signup/password "welcome"] (contained-in (signup/form {:password "welcome"})))))

    (testing "form has confirm"
             (is (-> [signup/confirm "welcome"] (contained-in (signup/form {:confirm "welcome"})))))

    ))

(deftest test-signup-events
  (let [john {:email "john.smith@example.org" :password "welcome" :confirm "welcome"}]

    (testing "click submit button"
             (mount (signup/submit john) [c div got]
                    (click! div :a)
                    (is (-> @got (= [:signup-submit john])))))

    (testing "editing email"
             (mount (signup/email "john@example.org") [c div got]
                    (edit! div :input "sarah@example.org")
                    (is (-> @got (= [:signup-edit {:email "sarah@example.org"}])))))

    (testing "editing password"
             (mount (signup/password "welcome") [c div got]
                    (edit! div :input "helloworld")
                    (is (-> @got (= [:signup-edit {:password "helloworld"}])))))

    ))
