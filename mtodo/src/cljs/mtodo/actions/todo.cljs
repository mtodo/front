(ns mtodo.actions.todo)

(def toggle-ty :todo-toggle)
(defn toggle [id]
  [toggle-ty {:id id}])

(def edit-ty :todo-edit)
(defn edit [id]
  [edit-ty {:id id}])

(def edit-save-ty :todo-edit-save)
(defn edit-save [id title]
  [edit-save-ty {:id id :title title}])

(def edit-stop-ty :todo-edit-stop)
(defn edit-stop [id]
  [edit-stop-ty {:id id}])
