(ns mtodo.core-test
  (:require [cljs.test :refer-macros [is are deftest testing use-fixtures]]
            [mtodo.test-helper :refer [with-mounted-component found-in]]
            [mtodo.containers.home :as home]))

(deftest test-home
  (with-mounted-component (home/page)
    (fn [c div]
      (is (found-in #"Welcome to" div)))))
