(ns mtodo.redux)

(def ^:private defaulted-key :-redux-defaulted-)
(defn- init-reducer [state ty data]
  (assoc state defaulted-key 0))

(defn- validate-reducer [n]
  (fn [state ty data]
      (do
        (when (-> (defaulted-key state) (>= n))
          (.error js/console "No reducer available for" (str ty)))
        state)))

(defn- proper-reducers [reducers]
  (let [n (count reducers)
        finish-reducer (validate-reducer n)]
    (-> [init-reducer]
        (into reducers)
        (conj finish-reducer))))

(defn default [state ty data]
  (update state defaulted-key inc))

(defn redux [state reducers ty data]
  (let [reducers (proper-reducers reducers)]
    (reduce #(%2 %1 ty data) state reducers)))

(defn store [state reducers]
  (let [f (fn [ty data] (swap! state redux reducers ty data))]
    (atom
      (fn
        ([ty data] (f ty data))
        ([[ty data]] (f ty data))))))

(defn push [push-atom]
  (fn
     ([ty data] (@push-atom ty data))
     ([[ty data]] (@push-atom ty data))))
