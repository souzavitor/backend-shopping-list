(ns sandbox.controllers.shopping-list-test
  (:require [midje.sweet :refer :all]
            [sandbox.controllers.shopping-list :as controller]
            [sandbox.db.shopping-list :as db]
            [sandbox.logic.shopping-list :as logic]
            [matcher-combinators.matchers :as m]
            [matcher-combinators.midje :refer [match]])
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
    (controller/add-new-shopping-list customer-id label)
    => (match (m/equals {:id          uuid?
                         :customer-id customer-id
                         :label       label
                         :items       []}))))
