(ns tools-methods-project.handlers.symptom-checker-handler
  (:require [tools-methods-project.use-cases.symptom-checker :as checker]
            [tools-methods-project.use-cases.symptoms-history :as history]
            [cheshire.core :as cheshire]
            [clojure.data.json :as json]))

(defn check-symptoms-handler [request]
  (try
    (let [body (slurp (:body request))
          {:keys [symptoms user-id]} (cheshire/parse-string body true)]
      (if symptoms
        (let [keyword-symptoms (map keyword symptoms)
              result (checker/check-symptoms keyword-symptoms user-id)]
          (if (= (:status result) :success)
            {:status 200
             :headers {"Content-Type" "application/json"}
             :body (cheshire/generate-string result)}
            {:status 400
             :headers {"Content-Type" "application/json"}
             :body (cheshire/generate-string result)}))
        {:status 400
         :headers {"Content-Type" "application/json"}
         :body (cheshire/generate-string {:status "error"
                                          :message "Invalid input. Missing required fields."})}))
    (catch Exception e
      {:status 500
       :headers {"Content-Type" "application/json"}
       :body (cheshire/generate-string {:status "error"
                                        :message (.getMessage e)})})))


(defn symptoms-history-handler [request]
  (let [user-id (:user-id (json/read-str (slurp (:body request)) :key-fn keyword))]
    (if user-id
      (let [history-data (history/get-history user-id)]
        (if (seq history-data)
          {:status 200
           :headers {"Content-Type" "application/json"}
           :body (json/write-str {:history history-data})}
          {:status 404
           :headers {"Content-Type" "application/json"}
           :body (json/write-str {:error "No history found for this user."})}))
      {:status 400
       :headers {"Content-Type" "application/json"}
       :body (json/write-str {:error "No user ID provided."})})))