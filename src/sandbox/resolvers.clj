(ns sandbox.resolvers
  (:require [sandbox.controller :as controller]
            [sandbox.adapters :as adapter]))

(defn get-all-shopping-lists [_context _args _parent]
  (if-let [shopping-lists (controller/get-all-shopping-lists)]
    (map adapter/internal->graphql shopping-lists)
    []))
