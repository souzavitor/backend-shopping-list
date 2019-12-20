(ns sandbox.resolvers
  (:require [sandbox.adapters.shopping-list :as adapter.shopping]
            [sandbox.adapters.item :as adapter.item]
            [sandbox.controllers.shopping-list :as controller.shopping]
            [sandbox.controllers.item :as controller.item]))

(defn get-all-shopping-lists [_context {customer-id :customerId} _parent]
  (if-let [shopping-lists (controller.shopping/get-all-shopping-lists customer-id)]
    (map adapter.shopping/internal->graphql shopping-lists)
    []))

(defn get-all-items [_context _args _parent]
  (if-let [all-items (controller.item/get-all-items)]
    (map adapter.item/internal->graphql all-items)
    []))
