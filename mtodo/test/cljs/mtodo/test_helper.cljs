(ns mtodo.test-helper
    (:require [reagent.core :as reagent]
              [cljs-react-test.simulate :as sim]
              [mtodo.data :as data]
              [mtodo.redux :as redux]))

(def is-client (not (nil? (try (.-document js/window)
                               (catch js/Object e nil)))))

(def rflush reagent/flush)

(defn add-test-div [name]
  (let [doc     js/document
        body    (.-body js/document)
        div     (.createElement doc "div")]
    (.appendChild body div)
    div))

(defn with-mounted-component [comp f]
  (when is-client
    (let [div (add-test-div "_testreagent")]
      (let [comp (reagent/render-component comp div #(f comp div))]
        (reagent/unmount-component-at-node div)
        (rflush)
        (.removeChild (.-body js/document) div)))))

(defn with-reducer [capture-ty handler f]
  (do
    (reset! data/*push!*
            (fn [ty data]
                (if (-> ty (= capture-ty)) (handler data))))
    (f)))

(defn with-recorder [f]
  (let [got (atom nil)]
    (do
      (reset! data/*push!*
              (fn [ty data]
                  (reset! got [ty data])))
      (f got))))

(defn re-found-in [re div]
  (let [res (.-innerHTML div)]
    (if (re-find re res)
      true
      (do (println "Not found: " res)
        false))))

(defn contained-in [x container]
  (some #(= x %) container))

(defn found-in [[selector attrs text] div]
  (let [elem (.querySelector div (name selector))]
    (if elem
      (let [elem-text (clojure.string/trim (.-textContent elem))]
        (if (-> text (= elem-text) (or (nil? text)))
          (if (reduce-kv #(and %1 (= %3 (.getAttribute elem (name %2)))) true attrs)
            true
            (do (println "Element" selector "expected to have attrs:" (str attrs))
              false))
          (do (println "Element" selector "does not have text: " text ", but has:" elem-text)
            false)))
      (do (println "Not found:" selector)
        false))))

(defn click! [div selector]
  (let [elem (.querySelector div (name selector))]
    (sim/click elem nil)))

(defn edit! [div selector value]
  (let [elem (.querySelector div (name selector))]
    (do
      (.focus elem)
      (set! (.-value elem) value)
      (sim/change elem nil)
      (.blur elem))))
