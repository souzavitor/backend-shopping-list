(ns sandbox.adapters.item)

(defn internal->graphql [internal]
  (-> internal
      (assoc :unitPrice (:unit-price internal))
      (dissoc :unit-price)))
