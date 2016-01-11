(ns mtodo.tests-runner
    (:require [doo.runner :refer-macros [doo-tests]]
              [mtodo.core-test]))

(doo-tests 'mtodo.core-test)
