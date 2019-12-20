(ns sandbox.db.item
  (:require [sandbox.adapters :as adapter]
            [sandbox.helpers :as helpers]
            [sandbox.logic.item :as logic.item]))

(def all-items (atom {}))

(defn find-item-by-id [id]
  (helpers/filter-by-id id @all-items))

(defn insert-item!
  [label qty unit-price]
  (->> (logic.item/new-item label qty unit-price)
       adapter/internal->db
       (swap! all-items logic.item/item-list-with-new-item)
       adapter/db->internal))

