(ns sandbox.graphql
  (:require [com.walmartlabs.lacinia.schema :as schema]))


(def scalars
  {:ID
   {:parse     str
    :serialize str}})

(def skeleton
  {:scalars scalars
   :objects {:ShoppingList
             {:fields {:id          {:type        '(non-null ID)
                                     :description "Shopping List Uuid"}
                       :customerId {:type        '(non-null ID)
                                     :description "Customer Uuid who owns this shopping list"}
                       :label       {:type        'String
                                     :description "A simple label to make it easier to find shopping list"}}}}
   :queries {:hello
             {:type    'String
              :resolve (constantly "world")}}})

(def graphql-schema (schema/compile skeleton))
