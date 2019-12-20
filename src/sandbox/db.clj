(ns sandbox.db
  (:require [sandbox.logic :as logic]
            [sandbox.helpers :as helpers]
            [sandbox.adapters :as adapter]))

(def all-shopping-lists (atom {}))
(def all-items (atom {}))

(defn find-shopping-list-by-id [id]
  (helpers/filter-by-id id @all-shopping-lists))

(defn find-item-by-id [id]
  (helpers/filter-by-id id @all-items))

(defn insert-shopping-list!
  [customer-id label]
  (->> (logic/new-shopping-list customer-id label)
       adapter/internal->db
       (swap! all-shopping-lists logic/with-new-shopping-list)))

(defn insert-item!
  [label qty unit-price]
  (->> (logic/new-item label qty unit-price)
       adapter/internal->db
       (swap! all-items logic/item-list-with-new-item)))

(defn insert-item-into-shopping-list!
  [shopping-list-id item-id]
  (->> (find-item-by-id item-id)
       (swap! all-shopping-lists
              logic/shopping-list-with-new-item
              shopping-list-id))
  (find-shopping-list-by-id shopping-list-id))

