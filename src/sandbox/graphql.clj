(ns sandbox.graphql
  (:require [com.walmartlabs.lacinia.schema :as schema]))


(def scalars
  {:ID
   {:parse     str
    :serialize str}})

(def objects
  {:ShoppingList
   {:fields {:id         {:type '(non-null ID)}
             :customerId {:type '(non-null ID)}
             :label      {:type 'String}}}})

(defn get-all-shopping-lists [_context _args _parent]
  [])

(def skeleton
  {:scalars scalars
   :objects objects
   :queries {:allShoppingLists
             {:type    '(non-null (list :ShoppingList))
              :resolve get-all-shopping-lists}}})

(def graphql-schema (schema/compile skeleton))
