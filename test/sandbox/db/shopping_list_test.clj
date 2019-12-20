(ns sandbox.db.shopping-list-test
  (:require [sandbox.db.shopping-list :as db]
            [sandbox.logic.shopping-list :as logic]
            [midje.sweet :refer :all]
            [matcher-combinators.matchers :as m]
            [matcher-combinators.midje :refer [match]])
  (:import [java.util UUID]))

(def customer-id (UUID/randomUUID))
(def label "My Shopping List")

(def shopping-list (logic/new-shopping-list customer-id label))
(def shopping-list-id (:id shopping-list))
(def shopping-list-id-str (str shopping-list-id))

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
    (db/insert-shopping-list! customer-id label)
    => (match (m/embeds {:id          uuid?
                         :customer-id customer-id
                         :label       label
                         :items       vector?}))))

