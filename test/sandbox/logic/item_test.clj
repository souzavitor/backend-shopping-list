(ns sandbox.logic.item-test
  (:require [midje.sweet :refer :all]
            [matcher-combinators.midje :refer [match]]
            [sandbox.logic.item :as logic]))

(def item (logic/new-item "A" 10 10.0))
(def item-id (:id item))

(facts "about items"
  (fact "should be able to init new shopping list item"
    (logic/new-item "Item 1" 10 10.9) => (match {:id         uuid?
                                                 :label      "Item 1"
                                                 :qty        10
                                                 :unit-price 10.9}))

  (fact "should create new item list with new item"
    (logic/item-list-with-new-item [] item) => (match [item])))
