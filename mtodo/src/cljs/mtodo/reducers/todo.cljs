(ns mtodo.reducers.todo
    (:require [mtodo.redux :as redux]
              [mtodo.actions.todo :as todo]))

(defmulti r (fn [state ty data] ty))
(defmethod r :default [state ty data]
  (redux/default state ty data))

(defmethod r todo/toggle-ty [state ty {:keys [id]}]
  (update-in state [:todos id :done] not))

(defmethod r todo/edit-ty [state ty {:keys [id]}]
  (assoc-in state [:todos id :editing] true))

(defmethod r todo/edit-save-ty [state ty {:keys [id title]}]
  (assoc-in state [:todos id :title] title))

(defmethod r todo/edit-stop-ty [state ty {:keys [id]}]
  (assoc-in state [:todos id :editing] false))
