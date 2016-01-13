(ns mtodo.test
    (:require [cljs.test :refer-macros [run-all-tests]]
              [mtodo.core-test]))

(enable-console-print!)

(defn ^:export run []
  (run-all-tests #"mtodo.*-test"))
