(ns sandbox.logic.shopping-list
  (:require [sandbox.logic.item :as logic.item])
  (:import [java.util UUID]))

(defn new-shopping-list [customer-id label]
  {:id          (UUID/randomUUID)
   :customer-id customer-id
   :label       label
   :items       []})

(defn with-new-shopping-list [all-shopping-lists shopping-list]
  (conj all-shopping-lists shopping-list))

(defn without-shopping-list [all-shopping-lists id]
  (dissoc all-shopping-lists (str id)))

(defn shopping-list-with-new-item
  [all-shopping-lists shopping-list-id item]
  (map #(if (= shopping-list-id (:id %))
          (update % :items logic.item/item-list-with-new-item item))
       all-shopping-lists))
