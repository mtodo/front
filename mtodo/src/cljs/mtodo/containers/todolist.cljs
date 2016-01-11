(ns mtodo.containers.todolist
    (:require [mtodo.data :as data]
              [mtodo.components.inputbox :as inputbox]
              [mtodo.actions.todo :as todo]))

(defn todolistitem [{:keys [id title done editing]}]
  (let [todo-id (str "todo_" id)]
    [:li
     [:input {:type :checkbox :checked done
              :id todo-id
              :on-change #(data/push! (todo/toggle id))}]
     (if editing
       [inputbox/inputbox {:text title
                           :on-save #(data/push! (todo/edit-save id %))
                           :on-stop #(data/push! (todo/edit-stop id))}]
       [:label {:on-click #(data/push! (todo/edit id))} title])]))

(defn todolist [todos]
  [:ul
   (for [id (keys todos)]
     ^{:key id} [todolistitem (get todos id)])])
