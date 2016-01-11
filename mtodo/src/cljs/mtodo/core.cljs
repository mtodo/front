(ns mtodo.core
  (:require [reagent.core :as reagent]
            [reagent.session :as session]
            [secretary.core :as secretary :include-macros true]
            [accountant.core :as accountant]
            [mtodo.containers.home :as home]
            [mtodo.containers.example :as example]
            [mtodo.containers.about :as about]))

;; -------------------------
;; Routes

(defn current-page []
  [:div [(session/get :current-page)]])

(secretary/defroute "/" []
                    (session/put! :current-page #'home/page))

(secretary/defroute "/about" []
                    (session/put! :current-page #'about/page))

(secretary/defroute "/example" []
                    (session/put! :current-page #'example/page))

;; -------------------------
;; Initialize app

(defn mount-root []
  (reagent/render [current-page] (.getElementById js/document "app")))

(defn init! []
  (accountant/configure-navigation!)
  (accountant/dispatch-current!)
  (mount-root))
