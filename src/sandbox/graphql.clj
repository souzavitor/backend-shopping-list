(ns sandbox.graphql
  (:require [com.walmartlabs.lacinia.schema :as schema]
            [com.walmartlabs.lacinia.util :refer [attach-resolvers]]
            [sandbox.resolvers :as resolvers]))

(def scalars
  {:ID
   {:parse     str
    :serialize str}})

(def objects
  {:ShoppingList
   {:fields {:id         {:type '(non-null ID)}
             :customerId {:type '(non-null ID)}
             :label      {:type 'String}}}})

(def resolvers
  {:get-all-shopping-lists resolvers/get-all-shopping-lists})

(def queries
  {:allShoppingLists
   {:type    '(non-null (list :ShoppingList))
    :resolve :get-all-shopping-lists}})

(def skeleton
  {:scalars scalars
   :objects objects
   :queries queries})

(def graphql-schema
  (-> skeleton
      (attach-resolvers resolvers)
      schema/compile))
