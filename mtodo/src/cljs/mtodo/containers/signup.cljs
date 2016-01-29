(ns mtodo.containers.signup
    (:require [mtodo.data :as data]
              [mtodo.components.inputbox :as inputbox]))

(defn signup-inputbox [{:keys [ty field text]}]
  [inputbox/raw {:type ty :name (name field) :text text
                 :on-save #(data/push! :signup-edit {field %})}])

(defn email [value]
  [signup-inputbox {:ty "email" :field :email :text value}])

(defn password [value]
  [signup-inputbox {:ty "password" :field :password :text value}])

(defn confirm [value]
  [signup-inputbox {:ty "password" :field :confirm :text value}])

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
