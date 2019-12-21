(ns sandbox.adapters.shopping-list
  (:require [sandbox.adapters :as adapters]))

(defn internal->graphql [internal-map]
  (adapters/internal->graphql internal-map))
