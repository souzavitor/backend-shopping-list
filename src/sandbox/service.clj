(ns sandbox.service
  (:require [sandbox.handlers.shopping-list :as handler.shopping]
            [sandbox.handlers.item :as handler.item]
            [sandbox.interceptors :as interceptors]
            [sandbox.graphql :as graphql]
            [com.walmartlabs.lacinia.pedestal :as lacinia]
            [io.pedestal.http :as http]
            [io.pedestal.http.body-params :as body-params]
            [ring.util.response :as ring-resp]
            [io.pedestal.http.route :as route]))

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
  #{["/api/version" :get (conj common-interceptors `version)]

    ["/api/shopping-lists" :get (conj common-interceptors `handler.shopping/get-all-shopping-lists)]
    ["/api/shopping-list/" :post (conj common-interceptors `handler.shopping/create-new-shopping-list)]
    ["/api/shopping-list/:shopping-list-id" :delete (conj common-interceptors
                                                          (interceptors/path-id->uuid :shopping-list-id
                                                                                      :shopping-list-uuid)
                                                          `handler.shopping/delete-shopping-list)]
    ["/api/shopping-list/:shopping-list-id" :put (conj common-interceptors
                                                       (interceptors/path-id->uuid :shopping-list-id
                                                                                   :shopping-list-uuid)
                                                       `handler.shopping/add-new-item-into-shopping-list)]
    ["/api/shopping-list/:shopping-list-id" :get (conj common-interceptors
                                                       (interceptors/path-id->uuid :shopping-list-id
                                                                                   :shopping-list-uuid)
                                                       `handler.shopping/get-shopping-list)]

    ["/api/items" :get (conj common-interceptors `handler.item/get-all-items)]
    ["/api/item/" :post (conj common-interceptors `handler.item/create-new-item)]})

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
