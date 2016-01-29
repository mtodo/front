(ns mtodo.test-macro)

(defmacro mount
  "mounts component to dom and recording reducer"
  [comp args & body]
  `(mtodo.test-helper/mount ~comp (fn ~args ~@body)))
