(ns sandbox.adapters)

(defn internal->db [internal]
  {(-> internal :id str) internal})

(defn db->internal [db]
  (-> db vals last))

(defn internal->graphql [internal]
  (-> internal
      (assoc :customerId (:customer-id internal))
      (dissoc :items :customer-id)))
