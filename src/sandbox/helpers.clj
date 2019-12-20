(ns sandbox.helpers
  (:require [clojure.pprint :as pp]))

(defn filter-by-id
  [id indexed-list]
  (if (and (map? indexed-list))
    (-> id str indexed-list)
    (filter #(= id (:id %)) indexed-list)))

(defmacro debug-go
  [expression]
  `(let [result# ~expression]
     (pp/pprint result#)
     result#))
