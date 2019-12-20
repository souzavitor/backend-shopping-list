(ns sandbox.resolvers
  (:require [sandbox.adapters.shopping-list :as adapter]
            [sandbox.controllers.shopping-list :as controller.shopping]))

(defn get-all-shopping-lists [_context _args _parent]
  (if-let [shopping-lists (controller.shopping/get-all-shopping-lists)]
    (map adapter/internal->graphql shopping-lists)
    []))
