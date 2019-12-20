(ns sandbox.service
  (:require [sandbox.interceptors :as interceptors]
            [sandbox.controller :as controller]
            [sandbox.db :as db]
            [sandbox.graphql :as graphql]
            [com.walmartlabs.lacinia.pedestal :as lacinia]
            [io.pedestal.http :as http]
            [io.pedestal.http.body-params :as body-params]
            [ring.util.response :as ring-resp]
            [io.pedestal.http.route :as route]))

(defn hello-world [_]
  (ring-resp/response (str "Hello world!")))

(defn get-all-shopping-lists [_]
  (ring-resp/response {:data (controller/get-all-shopping-lists)}))

(defn create-new-shopping-list
  [{{shopping-list :shopping-list} :json-params}]
  (let [customer-id (:customer-id shopping-list)
        label       (:label shopping-list)]
    (-> {:data (controller/add-new-shopping-list customer-id label)}
        ring-resp/response)))

(defn delete-shopping-list
  [{:keys [shopping-list-uuid]}]
  (controller/delete-shopping-list shopping-list-uuid)
  (ring-resp/status (ring-resp/response {}) 204))

(defn get-shopping-list
  [{:keys [shopping-list-uuid]}]
  (ring-resp/response {:data (controller/get-shopping-list shopping-list-uuid)}))

(defn add-new-item-into-shopping-list
  [{:keys [shopping-list-uuid json-params]}]
  (->> (controller/update-shopping-list-with-item shopping-list-uuid (:item-id json-params))
       ring-resp/response))

(def version (constantly (ring-resp/response {:version "1.0"})))

(def common-interceptors [(body-params/body-params)
                          http/json-body
                          interceptors/content-length-json-body])

(def graphql-routes
  (lacinia/graphql-routes graphql/graphql-schema
                          {:graphiql   true
                           :asset-path "/graphql/assets"
                           :ide-path   "/graphql/ide"
                           :path       "/graphql/query"}))

(def rest-routes
  #{["/api/" :get (conj [(body-params/body-params)] `hello-world)]
    ["/api/version" :get (conj common-interceptors `version)]
    ["/api/shopping-lists" :get (conj common-interceptors `get-all-shopping-lists)]
    ["/api/shopping-list/" :post (conj common-interceptors `create-new-shopping-list)]
    ["/api/shopping-list/:shopping-list-id" :delete (conj common-interceptors
                                                          (interceptors/path-id->uuid :shopping-list-id
                                                                                      :shopping-list-uuid)
                                                          `delete-shopping-list)]
    ["/api/shopping-list/:shopping-list-id" :put (conj common-interceptors
                                                       (interceptors/path-id->uuid :shopping-list-id
                                                                                   :shopping-list-uuid)
                                                       `add-new-item-into-shopping-list)]
    ["/api/shopping-list/:shopping-list-id" :get (conj common-interceptors
                                                       (interceptors/path-id->uuid :shopping-list-id
                                                                                   :shopping-list-uuid)
                                                       `get-shopping-list)]})

(def routes (route/expand-routes (into rest-routes graphql-routes)))

(def service {:env                     :prod
              ;; ::http/interceptors []
              ::http/routes            routes
              ;;::http/allowed-origins ["scheme://host:port"]
              ::http/type              :jetty
              ;;::http/host "localhost"
              ::http/port              8080
              ;; Options to pass to the container (Jetty)
              ::http/container-options {:h2c? true
                                        :h2?  false
                                        :ssl? false}})
