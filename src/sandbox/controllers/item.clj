(ns sandbox.controllers.item
  (:require [sandbox.db.item :as db]))


(defn get-all-items []
  (vals @db/all-items))

(defn get-item [id]
  (db/find-item-by-id id))

(defn add-new-item [label qty unit-price]
  (db/insert-item! label qty unit-price))

