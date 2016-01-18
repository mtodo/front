(ns mtodo.signup-test
    (:require [cljs.test :refer-macros [is are deftest testing use-fixtures async]]
              [mtodo.test-helper :refer [with-mounted-component with-reducer found-in contained-in click!]]
              [mtodo.containers.signup :as signup]))

(deftest test-signup
  (let [john {:email "john.smith@example.org" :password "welcome" :confirm "welcome"}]

    (testing "root"
             (with-mounted-component (signup/root john)
               (fn [c div]
                   (is (-> [:h1 {} "Signup"] (found-in div))))))

    (testing "form"
             (with-mounted-component (signup/form john)
               (fn [c div]
                   (is (-> [:form {}] (found-in div)))
                   (is (-> [:a {} "Start achieving"] (found-in div))))))

    (testing "form has submit button"
             (is (-> [signup/submit john] (contained-in (signup/form john)))))

    (testing "root has form"
             (is (-> [signup/form john] (contained-in (signup/root john)))))

    (testing "email"
             (with-mounted-component (signup/email "john.smith@example.org")
               (fn [c div]
                   (is (-> [:input {:type "email" :name "email" :value "john.smith@example.org"}] (found-in div))))))

    (testing "password"
             (with-mounted-component (signup/password "welcome")
               (fn [c div]
                   (is (-> [:input {:type "password" :name "password" :value "welcome"}] (found-in div))))))

    (testing "confirm"
             (with-mounted-component (signup/confirm "welcome")
               (fn [c div]
                   (is (-> [:input {:type "password" :name "confirm" :value "welcome"}] (found-in div))))))

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
             (let [submitted (atom nil)]
               (with-reducer :signup-submit #(reset! submitted %)
                 (fn []
                     (with-mounted-component (signup/submit john)
                       (fn [c div]
                           (do
                             (click! div :a)
                             (is (-> @submitted (= john))))))))))

    ))
