(ns mtodo.reducers.todos
    (:require [mtodo.redux :as redux]
              [mtodo.actions.todos :as todos]))

(defmulti r (fn [state ty data] ty))
(defmethod r :default [state ty data]
  (redux/default state ty data))

(defmethod r todos/reset-ty [state ty data]
  (assoc state :todos (sorted-map)))

(defmethod r todos/add-ty [state ty {:keys [title done]}]
  (let [state (update state :counter inc)
        id (:counter state)]
    (assoc-in state [:todos id] {:id id :title title :done done})))
