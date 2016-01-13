(ns mtodo.containers.signup)

(def ^:private signup *ns*)

(defn email [value]
  [:input {:type "email" :name "email" :value value}])

(defn password [value]
  [:input {:type "password" :name "password" :value value}])

(defn confirm [value]
  [:input {:type "password" :name "confirm" :value value}])

(defn form [signup]
  [:form
   [email (:email signup)]
   [password (:password signup)]
   [confirm (:confirm signup)]
   [:a {:href "#"} "Start achieving"]])

(defn root [signup]
  [:div
    [:h1 "Signup"]
    [form signup]])
