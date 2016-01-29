(ns mtodo.containers.signup
    (:require [mtodo.data :as data]
              [mtodo.components.inputbox :as inputbox]))

(def ^:private signup *ns*)

(defn email [value]
  [inputbox/raw {:type "email" :name "email" :text value
                 :on-save #(data/push! :signup-edit {:email %})}])

(defn password [value]
  [inputbox/raw {:type "password" :name "password" :text value
                 :on-save #(data/push! :signup-edit {:password %})}])

(defn confirm [value]
  [inputbox/raw {:type "password" :name "confirm" :text value
                 :on-save #(data/push! :signup-edit {:confirm %})}])

(defn submit [signup]
  [:a {:href "#"
       :on-click #(data/push! :signup-submit signup)}
   "Start achieving"])

(defn form [signup]
  [:form
   [email (:email signup)]
   [password (:password signup)]
   [confirm (:confirm signup)]
   [submit signup]])

(defn root [signup]
  [:div
   [:h1 "Signup"]
   [form signup]])
