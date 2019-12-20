(ns sandbox.adapters.shopping-list)

(defn internal->graphql [internal]
  (-> internal
      (assoc :customerId (:customer-id internal))
      (dissoc :items :customer-id)))
