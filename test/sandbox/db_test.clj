(ns sandbox.db-test
  (:require [sandbox.db :as db]
            [sandbox.logic :as logic]
            [midje.sweet :refer :all]
            [matcher-combinators.matchers :as m]
            [matcher-combinators.midje :refer [match]]
            [sandbox.adapters :as adapter])
  (:import [java.util UUID]))

(def customer-id (UUID/randomUUID))
(def label "My Shopping List")

(def shopping-list (logic/new-shopping-list customer-id label))
(def shopping-list-id (:id shopping-list))
(def shopping-list-id-str (str shopping-list-id))

(def item (logic/new-item "A" 10 10.0))
(def item-id (:id item))
(def item-id-str (str item-id))

(facts "about handling shopping lists"
  (fact "get shopping list by id"
    (reset! db/all-shopping-lists {shopping-list-id-str shopping-list})
    (db/find-shopping-list-by-id shopping-list-id)
    => (match (m/equals {:id          shopping-list-id
                         :customer-id customer-id
                         :label       label
                         :items       []})))

  (fact "should add a new shopping list"
    (reset! db/all-shopping-lists {})
    (adapter/db->internal (db/insert-shopping-list! customer-id label))
    => (match (m/embeds {:id          uuid?
                         :customer-id customer-id
                         :label       label
                         :items       vector?}))))

(facts "about handling items"
  (fact "should add new item"
    (reset! db/all-items {item-id-str item})
    (adapter/db->internal (db/insert-item! (:label item) (:qty item) (:unit-price item)))
    => (match {:id         uuid?
               :label      "A"
               :qty        10
               :unit-price 10.0}))

  (fact "should find an item by its id"
    (reset! db/all-items {item-id-str item})
    (db/find-item-by-id item-id) => item)

  ;(fact "should be able to include new item to the shopping list"
  ;  (reset! db/all-items {item-id-str item})
  ;  (reset! db/all-shopping-lists {shopping-list-id-str shopping-list})
  ;  (db/insert-item-into-shopping-list! shopping-list-id item-id)
  ;  => (match (m/embeds {:id          uuid?
  ;                       :customer-id customer-id
  ;                       :items       vector?})))
  )
