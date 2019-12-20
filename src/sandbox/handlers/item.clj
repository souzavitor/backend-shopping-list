(ns sandbox.handlers.item
  (:require [ring.util.response :as ring-resp]
            [sandbox.controllers.item :as controller.item]))

(defn get-all-items [_]
  (ring-resp/response {:data (controller.item/get-all-items)}))

(defn create-new-item
  [{{item :item} :json-params}]
  (let [label      (:label item)
        qty        (:qty item)
        unit-price (:unit-price item)]
    (-> {:data (controller.item/add-new-item label qty unit-price)}
        ring-resp/response)))

(defn get-item
  [{:keys [item-uuid]}]
  (ring-resp/response {:data (controller.item/get-item item-uuid)}))
