(ns tools-methods-project.helpers.response
  (:require [cheshire.core :as cheshire]))

(defn json-response [status payload]
  {:status status
   :headers {"Content-Type" "application/json"}
   :body (cheshire/encode payload)})

(defn handle-domain-result [result]
  (if (= (:domain-status result) :success)
    (json-response 200 {:message (:message result)})
    (json-response 400 {:error (:message result)})))