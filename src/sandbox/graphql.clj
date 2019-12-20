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
             :label      {:type '(non-null String)}}}
   :Item
   {:fields {:id        {:type '(non-null ID)}
             :label     {:type '(non-null String)}
             :qty       {:type '(non-null Int)}
             :unitPrice {:type '(non-null Float)}}}})

(def resolvers
  {:get-all-shopping-lists resolvers/get-all-shopping-lists
   :get-all-items          resolvers/get-all-items})

(def queries
  {:allShoppingLists {:type    '(non-null (list :ShoppingList))
                      :args    {:customerId {:type 'ID}}
                      :resolve :get-all-shopping-lists}
   :allItems         {:type    '(non-null (list :Item))
                      :resolve :get-all-items}})

(def skeleton
  {:scalars scalars
   :objects objects
   :queries queries})

(def graphql-schema
  (-> skeleton
      (attach-resolvers resolvers)
      schema/compile))
