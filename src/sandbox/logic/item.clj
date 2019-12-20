(ns sandbox.logic.item
  (:import [java.util UUID]))

(defn new-item [label qty unit-price]
  {:id         (UUID/randomUUID)
   :label      label
   :qty        qty
   :unit-price unit-price})

(defn item-list-with-new-item [all-item-list item]
  (conj all-item-list item))
