(ns sandbox.adapters)

(defn internal->db [internal]
  {(-> internal :id str) internal})

(defn db->internal [db]
  (-> db vals last))
