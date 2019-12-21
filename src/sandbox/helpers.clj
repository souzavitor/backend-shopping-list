(ns sandbox.helpers
  (:require [clojure.pprint :as pp]
            [clojure.string :as str]))

(defn filter-by-id
  [id indexed-list]
  (if (and (map? indexed-list))
    (-> id str indexed-list)
    (filter #(= id (:id %)) indexed-list)))

(defn str-vector->camel-case [r n]
  (->> n
       str/capitalize
       (str r)))

(defn kebab->camel-case [s]
  (->> (str/split s #"-")
       (reduce str-vector->camel-case)))

(defn kebab-keyword->camel-case [[k v]]
  {(-> k name kebab->camel-case keyword) v})

(defmacro debug-go
  [expression]
  `(let [result# ~expression]
     (pp/pprint result#)
     result#))
