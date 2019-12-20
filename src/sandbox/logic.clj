(ns sandbox.logic
  (:import [java.util UUID]))

(defn new-shopping-list [customer-id label]
  {:id          (UUID/randomUUID)
   :customer-id customer-id
   :label       label
   :items       []})

(defn with-new-shopping-list [all-shopping-lists shopping-list]
  (conj all-shopping-lists shopping-list))

(defn new-item [label qty unit-price]
  {:id         (UUID/randomUUID)
   :label      label
   :qty        qty
   :unit-price unit-price})

(defn item-list-with-new-item [all-item-list item]
  (conj all-item-list item))

(defn shopping-list-with-new-item
  [all-shopping-lists shopping-list-id item]
  (if (map? all-shopping-lists)
    (clojure.pprint/pprint all-shopping-lists))
  (map #(if (= shopping-list-id (:id %))
          (update % :items item-list-with-new-item item))
       all-shopping-lists))
