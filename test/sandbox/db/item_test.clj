(ns sandbox.db.item-test
  (:require [matcher-combinators.midje :refer [match]]
            [midje.sweet :refer :all]
            [sandbox.db.item :as db]
            [sandbox.logic.item :as logic.item]))

(def item (logic.item/new-item "A" 10 10.0))
(def item-id (:id item))
(def item-id-str (str item-id))

(facts "about handling items"
  (fact "should add new item"
    (reset! db/all-items {item-id-str item})
    (db/insert-item! (:label item) (:qty item) (:unit-price item))
    => (match {:id         uuid?
               :label      "A"
               :qty        10
               :unit-price 10.0}))

  (fact "should find an item by its id"
    (reset! db/all-items {item-id-str item})
    (db/find-item-by-id item-id) => item))
