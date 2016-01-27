(ns mtodo.containers.signup
    (:require [mtodo.data :as data]
              [mtodo.components.inputbox :as inputbox]))

(def ^:private signup *ns*)

(defn email [value]
  [inputbox/raw {:type "email" :name "email" :text value
                 :on-save #(data/push! :signup-edit-email {:email %})}])

(defn password [value]
  [:input {:type "password" :name "password" :value value}])

(defn confirm [value]
  [:input {:type "password" :name "confirm" :value value}])

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
