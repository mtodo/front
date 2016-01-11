(ns mtodo.containers.example
    (:require [mtodo.data :as data]
              [mtodo.containers.todolist :as todolist]))

(defn page []
  [:div [:h2 "Reagent + Redux example"]
   [:div [:a {:href "/"} "go to home page"]]
   [:div [:h3 "Your todos"]]
   [:div [todolist/todolist (:todos @data/state)]]])
