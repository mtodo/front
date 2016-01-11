(ns mtodo.actions.todos)

(def reset-ty :todos-reset)
(defn reset []
  [reset-ty {}])

(def add-ty :todos-add)
(defn add
  ([title] (add title {:done false}))
  ([title {:keys [done]}] [add-ty {:title title :done done}]))

(def rm-ty :todos-rm)
(defn rm [id]
  [rm-ty {:id id}])
