(ns sandbox.handlers.shopping-list
  (:require [ring.util.response :as ring-resp]
            [sandbox.controllers.shopping-list :as controller.shopping]))

(defn get-all-shopping-lists [_]
  (ring-resp/response {:data (controller.shopping/get-all-shopping-lists)}))

(defn create-new-shopping-list
  [{{shopping-list :shopping-list} :json-params}]
  (let [customer-id (:customer-id shopping-list)
        label       (:label shopping-list)]
    (-> {:data (controller.shopping/add-new-shopping-list customer-id label)}
        ring-resp/response)))

(defn delete-shopping-list
  [{:keys [shopping-list-uuid]}]
  (controller.shopping/delete-shopping-list shopping-list-uuid)
  (ring-resp/status (ring-resp/response {}) 204))

(defn get-shopping-list
  [{:keys [shopping-list-uuid]}]
  (ring-resp/response {:data (controller.shopping/get-shopping-list shopping-list-uuid)}))

(defn add-new-item-into-shopping-list
  [{:keys [shopping-list-uuid json-params]}]
  (->> (controller.shopping/update-shopping-list-with-item shopping-list-uuid (:item-id json-params))
       ring-resp/response))
