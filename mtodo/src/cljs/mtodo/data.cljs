(ns mtodo.data
  (:require [reagent.core :as reagent]
            [mtodo.actions.todos :as todos]
            [mtodo.reducers.todos :as todos-reducers]
            [mtodo.reducers.todo :as todo-reducers]
            [mtodo.redux :as redux]))

(defonce state (reagent/atom {:counter 0
                              :todos (sorted-map)}))

(def push! (redux/store state
                        [todos-reducers/r
                         todo-reducers/r]))

(def init
  (do
    (push! (todos/reset))
    (push! (todos/add "Feed my cats"))
    (push! (todos/add "Do laundry" {:done true}))
    (push! (todos/add "Read a book"))))
