(ns sandbox.controller
  (:require [sandbox.db :as db]
            [sandbox.adapters :as adapter]))

(defn get-all-shopping-lists []
  (vals @db/all-shopping-lists))

(defn get-shopping-list [id]
  (db/find-shopping-list-by-id id))

(defn delete-shopping-list [id]
  (db/delete-shopping-list-by-id id))

(defn add-new-shopping-list [customer-id label]
  (db/insert-shopping-list! customer-id label))

(defn update-shopping-list-with-item [shopping-list-id item-id]
  (db/insert-item-into-shopping-list! shopping-list-id item-id))
