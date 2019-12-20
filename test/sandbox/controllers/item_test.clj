(ns sandbox.controllers.item-test
  (:require [clojure.test :refer :all]
            [sandbox.controllers.item :as controller]
            [sandbox.db.item :as db]
            [sandbox.logic.item :as logic]
            [matcher-combinators.matchers :as m]
            [matcher-combinators.test]))

(def label "My Shopping List")

;; create new shopping list to use in our test cases
(def item (logic/new-item "A" 10 10.0))
(def expected-item
  (-> item
      (assoc :id uuid?)))

(def item-id (:id item))
(def item-id-str (str item-id))

(deftest test-controller-functions
  (testing "adding new item"
    (is (match? (m/equals expected-item)
                (controller/add-new-item "A" 10 10.0))))

  (testing "get item by id"
    (reset! db/all-items {item-id-str item})
    (is (match? (m/equals item)
                (controller/get-item item-id)))))
