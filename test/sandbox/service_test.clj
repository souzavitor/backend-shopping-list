(ns sandbox.service-test
  (:require [clojure.test :refer :all]
            [cheshire.core :as json]
            [io.pedestal.http :as bootstrap]
            [io.pedestal.test :refer [response-for]]
            [matcher-combinators.test]
            [matcher-combinators.matchers :as m]
            [midje.sweet :refer :all]
            [sandbox.db.shopping-list :as db.shopping]
            [sandbox.logic.shopping-list :as logic.shopping]
            [sandbox.logic.item :as logic.item]
            [sandbox.service :as service]
            [sandbox.db.item :as db.item])
  (:import [java.util UUID]))

(def customer-id (UUID/randomUUID))
(def shopping-list-label "My Shopping List")
(def shopping-list (logic.shopping/new-shopping-list customer-id shopping-list-label))
(def expected-shopping-list
  (-> shopping-list
      (update :id str)
      (update :customer-id str)))
(def new-expected-shopping-list
  (-> expected-shopping-list
      (assoc :id string?)))

(def shopping-list-id (:id shopping-list))
(def shopping-list-id-str (str shopping-list-id))

(def item-label "A")
(def item (logic.item/new-item item-label 10 10.0))
(def expected-item
  (update item :id str))
(def new-expected-item
  (-> expected-item
      (assoc :id string?)))

(def item-id (:id item))
(def item-id-str (str item-id))

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

; Clojure test
(deftest testing-common-rest-api-endpoints
  (testing "/api/version"
    (is (match? (m/embeds {:body   {:version "1.0"}
                           :status 200})
                (request "/api/version" :get)))))

(deftest testing-rest-api-endpoints
  (testing "shopping list api endpoints"
    (testing "get /api/shopping-lists"
      (reset! db.shopping/all-shopping-lists {shopping-list-id-str shopping-list})
      (is (match? (m/embeds {:body   {:data [expected-shopping-list]}
                             :status 200})
                  (request "/api/shopping-lists" :get))))

    (testing "get /api/shopping-list/:id"
      (reset! db.shopping/all-shopping-lists {shopping-list-id-str shopping-list})
      (is (match? (m/embeds {:body   {:data expected-shopping-list}
                             :status 200})
                  (request (str "/api/shopping-list/" shopping-list-id-str) :get))))

    (testing "post /api/shopping-list/"
      (reset! db.shopping/all-shopping-lists {})
      (is (match? (m/embeds {:body   {:data new-expected-shopping-list}
                             :status 200})
                  (request "/api/shopping-list/"
                           :post
                           {:body    (json/encode {:shopping-list
                                                   {:customer-id (:customer-id shopping-list)
                                                    :label       shopping-list-label}})
                            :headers {"Content-Type" "application/json"}}))))

    (testing "delete /api/shopping-list/:id"
      (reset! db.shopping/all-shopping-lists {shopping-list-id-str shopping-list})
      (is (match? (m/embeds {:status 204})
                  (request (str "/api/shopping-list/" shopping-list-id-str) :delete)))))

  (testing "items endpoints"
    (testing "get /api/items"
      (reset! db.item/all-items {item-id-str item})
      (is (match? (m/embeds {:body   {:data [expected-item]}
                             :status 200})
                  (request "/api/items" :get))))

    (testing "post /api/item"
      (reset! db.item/all-items {})
      (is (match? (m/embeds {:body   {:data new-expected-item}
                             :status 200})
                  (request
                   "/api/item/"
                   :post
                   {:body    (json/encode {:item
                                           {:label      item-label
                                            :qty        10
                                            :unit-price 10.0}})
                    :headers {"Content-Type" "application/json"}}))))))
