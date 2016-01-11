(ns mtodo.components.inputbox
    (:require [reagent.core :as r]))

(defn raw-inputbox [{:keys [text on-save on-stop]}]
  (let [val (r/atom text)
        stop #(if on-stop (on-stop))
        save #(let [v (-> @val str clojure.string/trim)]
                (when on-save
                  (if-not (empty? v) (on-save v))
                  (stop)))]
    (fn [props]
        [:input (merge props
                       {:type :text :value @val :on-blur save
                        :on-change #(reset! val (-> % .-target .-value))
                        :on-key-down #(case (.-which %)
                                        13 (save)
                                        27 (stop)
                                        nil)})])))

(def inputbox (with-meta raw-inputbox
                {:component-did-mount #(let [node (r/dom-node %)]
                                         (.focus node)
                                         (.select node))}))
