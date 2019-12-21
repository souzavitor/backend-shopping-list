(ns sandbox.adapters
  (:require [sandbox.helpers :as helpers]))

(defn internal->db [internal]
  {(-> internal :id str) internal})

(defn db->internal [db]
  (-> db vals last))

(defn internal->graphql [internal-map]
  (->> internal-map
       (map helpers/kebab-keyword->camel-case)
       (reduce merge)))
