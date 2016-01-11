(ns mtodo.core-test
    (:require-macros [cljs.test :refer (is deftest testing)])
    (:require [cljs.test]
              [mtodo.core :as c]))

(deftest it-works
  (let [a 2
        b 2
        c 4]
    (is (-> a (+ b) (= c)))))
