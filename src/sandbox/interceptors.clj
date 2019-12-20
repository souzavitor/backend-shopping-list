(ns sandbox.interceptors
  (:require [cheshire.core :as json]
            [io.pedestal.interceptor :as interceptor])
  (:import [java.util UUID]))

(defn path-id->uuid
  ([] (path-id->uuid :id :uuid))
  ([src-name dest-name]
   (interceptor/interceptor
    {:name  ::path-id->uuid
     :enter (fn [context]
              (if-let [id (-> context :request :path-params src-name)]
                (update-in context [:request dest-name] #(or % (UUID/fromString id)))
                context))})))

(def content-length-json-body
  (interceptor/interceptor
   {:name  ::content-length-json-body
    :leave (fn [context]
             (let [response           (:response context)
                   body               (:body response)
                   json-response-body (if body (json/generate-string body) "")
                   ;; Content-Length is the size of the response in bytes
                   ;; Let's count the bytes instead of the string, in case there are unicode characters
                   content-length     (count (.getBytes ^String json-response-body))
                   headers            (:headers response {})]
               (assoc context
                 :response {:status  (:status response)
                            :body    json-response-body
                            :headers (merge headers
                                            {"Content-Type"   "application/json;charset=UTF-8"
                                             "Content-Length" (str content-length)})})))}))
