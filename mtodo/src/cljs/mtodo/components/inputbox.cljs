(ns mtodo.components.inputbox
    (:require [reagent.core :as r]))

(defn raw [{:keys [text on-save on-stop name type]}]
  (let [val (r/atom text)
        type (if (empty? type) :text type)

        stop #(if on-stop (on-stop))

        save #(let [v (-> @val str clojure.string/trim)]
                (when on-save
                  (if-not (empty? v) (on-save v))
                  (stop)))

        reset #(let [v (-> % .-target .-value)]
                 (reset! val v))]

    (fn [props]
        [:input (merge props
                       {:type type :value @val :name name :id name :on-blur save
                        :on-change reset
                        :on-key-down #(case (.-which %)
                                        13 (save)
                                        27 (stop)
                                        nil)})])))

(def inputbox (with-meta raw
                {:component-did-mount #(let [node (r/dom-node %)]
                                         (.focus node)
                                         (.select node))}))
