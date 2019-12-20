(ns sandbox.db.shopping-list
  (:require [sandbox.adapters :as adapter]
            [sandbox.db.item :as db.item]
            [sandbox.helpers :as helpers]
            [sandbox.logic.shopping-list :as logic]))

(def all-shopping-lists (atom {}))
(defn find-shopping-list-by-id [id]
  (helpers/filter-by-id id @all-shopping-lists))

(defn delete-shopping-list-by-id [id]
  (swap! all-shopping-lists logic/without-shopping-list id))

(defn insert-shopping-list!
  [customer-id label]
  (->> (logic/new-shopping-list customer-id label)
       adapter/internal->db
       (swap! all-shopping-lists logic/with-new-shopping-list)
       adapter/db->internal))

(defn insert-item-into-shopping-list!
  [shopping-list-id item-id]
  (->> (db.item/find-item-by-id item-id)
       (swap! all-shopping-lists
              logic/shopping-list-with-new-item
              shopping-list-id))
  (find-shopping-list-by-id shopping-list-id))
