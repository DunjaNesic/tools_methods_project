(ns tools-methods-project.handlers.symptom-checker-handler
  (:require [tools-methods-project.use-cases.symptom-checker :as checker]
            [tools-methods-project.use-cases.symptoms-history :as history]
            [clojure.data.json :as json]))

(defn check-symptoms-handler [request]
  (let [body (json/read-str (slurp (:body request)) :key-fn keyword)
        symptoms (:symptoms body)
        user-id (:user-id body)]
    (if symptoms
      (let [keyword-symptoms (map keyword symptoms)
            result (checker/check-symptoms keyword-symptoms user-id)]
        {:status 200
         :headers {"Content-Type" "application/json"}
         :body (json/write-str result)})
      {:status 400
       :headers {"Content-Type" "application/json"}
       :body (json/write-str {:error "No symptoms provided."})})))


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