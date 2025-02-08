(ns tools-methods-project.handlers.symptom-checker-handler
  (:require [tools-methods-project.use-cases.symptom-checker :as checker]
            [tools-methods-project.use-cases.symptoms-history :as history]
            [tools-methods-project.helpers.response :refer [json-response]]
            [cheshire.core :as json]))

(defn check-symptoms-handler [request]
  (let [{:keys [symptoms user-id] :as body} (try
                                              (json/parse-string (slurp (:body request)) true)
                                              (catch Exception e
                                                {:error "Invalid JSON format"}))]
    (cond
      (:error body) (json-response 400 {:status "error" :message (:error body)})
      (not symptoms) (json-response 400 {:status "error" :message "Invalid input. Missing required fields."})
      :else
      (let [keyword-symptoms (map keyword symptoms)
            result (checker/check-symptoms keyword-symptoms user-id)]
        (json-response (if (= (:status result) :success) 200 400) result)))))

(defn symptoms-history-handler [request]
  (let [{:keys [user-id] :as body} (try
                                     (json/parse-string (slurp (:body request)) true)
                                     (catch Exception e
                                       {:error "Invalid JSON format"}))]
    (cond
      (:error body) (json-response 400 {:error (:error body)})
      (not user-id) (json-response 400 {:error "No user ID provided."})
      :else
      (let [history-data (history/get-history user-id)]
        (if (seq history-data)
          (json-response 200 {:history history-data})
          (json-response 404 {:error "No history found for this user."}))))))

