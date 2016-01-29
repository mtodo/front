(ns ^:figwheel-always mtodo.test
    (:require [cljs.test :as test :refer-macros [run-all-tests] :refer [report]]
              [mtodo.core-test]
              [mtodo.signup-test]))

(enable-console-print!)

(defn ^:export run []
  (run-all-tests #"mtodo.*-test"))

(defmethod report [::test/default :summary] [m]
  (println "\nRan:" (:test m) "tests containing"
           (+ (:pass m) (:fail m) (:error m)) "assertions.")
  (println "\n\n==================================")
  (println "\n" (:fail m) "failures," (:error m) "errors")
  (println "\n==================================\n\n")
  (if (-> (:fail m) (+ (:error m)) (> 0))
    (.error js/console "FAIL!")
    (println "OK\n")))

(run)
