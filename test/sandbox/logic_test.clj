(ns sandbox.logic_test
  (:require [sandbox.logic :as logic]
            [midje.sweet :refer :all]
            [matcher-combinators.midje :refer [match]]
            [matcher-combinators.matchers :as m])
  (:import [java.util UUID]))

;; Let's create the constants that we need for this test
(def customer-id (UUID/randomUUID))
(def label "My Shopping List")
(def shopping-list {:id          (UUID/randomUUID)
                    :customer-id customer-id
                    :label       label
                    :items       []})
(def item {:id         (UUID/randomUUID)
           :label      "A"
           :qty        10
           :unit-price 10.0})

;; Let's test the facts regarding our logic
(facts "about shopping lists"
  (fact "should be able to init a shopping list"
    (logic/new-shopping-list customer-id label)
    => (match {:id          uuid?
               :customer-id customer-id
               :items       vector?}))

  (fact "should create new list of shopping lists with new item"
    (logic/with-new-shopping-list [] shopping-list) => (match [shopping-list]))

  (fact "Should create new shopping list with new item"
    (let [item (logic/new-item "A" 1 1.9)]
      (logic/shopping-list-with-new-item [shopping-list]
                                         (:id shopping-list)
                                         item)
      => (match (m/equals [{:id          (:id shopping-list)
                            :customer-id (:customer-id shopping-list)
                            :label       (:label shopping-list)
                            :items       [item]}])))))

(facts "about items"
  (fact "should be able to init new shopping list item"
    (logic/new-item "Item 1" 10 10.9) => (match {:id         uuid?
                                                 :label      "Item 1"
                                                 :qty        10
                                                 :unit-price 10.9}))

  (fact "should create new item list with new item"
    (logic/item-list-with-new-item [] item) => (match [item])))
