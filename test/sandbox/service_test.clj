(ns sandbox.service-test
  (:require [midje.sweet :refer :all]
            [sandbox.service :as service]
            [io.pedestal.http :as bootstrap]
            [io.pedestal.test :refer [response-for]]
            [cheshire.core :as json]
            [sandbox.logic :as logic]
            [sandbox.db :as db])
  (:import [java.util UUID]))

(def customer-id (UUID/randomUUID))
(def shopping-list (logic/new-shopping-list customer-id))
(def shopping-list-id (:id shopping-list))

(def item (logic/new-item "A" 10 10.0))
(def item-id (:id item))

;; Initialize our service so we can test the endpoints
(def service
  (::bootstrap/service-fn (bootstrap/create-servlet service/service)))

;; helper functions to handle request and response
(defn json-parse [body]
  (try (json/parse-string body keyword)
       (catch Exception _ body)))

(defn request
  ([uri method]
   (request uri method {}))
  ([uri method {:keys [body headers]}]
   (let [{:keys [headers status body]} (response-for service
                                                     method uri
                                                     :body body
                                                     :headers headers)]
     {:headers headers
      :status  status
      :body    (json-parse body)})))


;; Finally our tests
(facts "about our HTTP service"
  (fact "API should return a simple hello world"
    (request "/" :get) => (just {:headers map? :status 200 :body "Hello world!"}))
  (fact "API should return shopping list"
    (reset! db/all-shopping-lists [shopping-list])
    (request "/shopping-lists" :get) => (just {:headers map?
                                               :status  200
                                               :body    map?}))

  (fact "API should be able to return one shopping list according to ID"
    (reset! db/all-shopping-lists [shopping-list])
    (request (str "/shopping-list/" shopping-list-id) :get) => (just {:headers (contains
                                                                                {"Content-Type"
                                                                                 "application/json;charset=UTF-8"})
                                                                      :body    map?
                                                                      :status  200}))
  (fact "API should be able to receive a new shopping list"
    (reset! db/all-shopping-lists [])
    (request "/shopping-list/"
             :post
             {:body    (json/encode {:customer-id (:customer-id shopping-list)})
              :headers {"Content-Type" "application/json"}}) => (just {:headers (contains
                                                                                 {"Content-Type"
                                                                                  "application/json;charset=UTF-8"})
                                                                       :body    map?
                                                                       :status  200}))
  (fact "API should be able to include new item to the shopping list"
    (reset! db/all-shopping-lists [shopping-list])
    (reset! db/all-items [item])
    (request (str "/shopping-list/" shopping-list-id)
             :put
             {:body    (json/encode {:item-id item-id})
              :headers {"Content-Type" "application/json"}}) => (just {:headers (contains
                                                                              {"Content-Type"
                                                                               "application/json;charset=UTF-8"})
                                                                    :body    map?
                                                                    :status  200})))
