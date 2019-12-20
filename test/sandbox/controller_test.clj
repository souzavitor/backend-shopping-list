(ns sandbox.controller_test
  (:require [midje.sweet :refer :all]
            [sandbox.controller :as controller]
            [sandbox.db :as db]
            [sandbox.logic :as logic]
            [sandbox.db :as db]
            [matcher-combinators.matchers :as m]
            [matcher-combinators.midje :refer [match]]
            [sandbox.adapters :as adapter])
  (:import [java.util UUID]))

(def customer-id (UUID/randomUUID))
(def label "My Shopping List")

;; create new shopping list to use in our test cases
(def shopping-list (logic/new-shopping-list customer-id label))
(def shopping-list-id (:id shopping-list))
(def shopping-list-id-str (str shopping-list-id))

(facts "about our micro service controller"
  (fact "get shopping list by id"
    (reset! db/all-shopping-lists {shopping-list-id-str shopping-list})
    (controller/get-shopping-list shopping-list-id) => shopping-list)

  (fact "should add a new shopping list"
    (reset! db/all-shopping-lists {})
    (adapter/db->internal (controller/add-new-shopping-list customer-id label))
    => (match (m/equals {:id          uuid?
                         :customer-id customer-id
                         :label       label
                         :items       []}))))

;(facts "about shopping list items"
;  (fact "add new item to the shopping list"
;    (reset! db/all-shopping-lists [shopping-list])
;    (let [item (logic/new-item "A" 5 10.98)]
;      (controller/add-new-item-to-shopping-list shopping-list-id item))))
