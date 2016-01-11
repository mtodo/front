(ns mtodo.containers.home
    (:require [mtodo.data :as data]
              [mtodo.containers.todolist :as todolist]))

(defn page []
  [:div [:h2 "Welcome to mtodo"]
   [:div [:a {:href "/about"} "go to about page"]]
   [:div [:h3 "Your todos"]]
   [:div [todolist/todolist (:todos @data/state)]]])
