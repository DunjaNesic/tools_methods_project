(ns tools-methods-project.handlers.symptom-checker-handler
  (:require [tools-methods-project.use-cases.symptom-checker :as checker]
            [clojure.data.json :as json]))

(defn check-symptoms-handler [request]
  (let [body (json/read-str (slurp (:body request)) :key-fn keyword)
        symptoms (:symptoms body)]
    (if symptoms
      (let [keyword-symptoms (map keyword symptoms)
            result (checker/check-symptoms keyword-symptoms)]
        {:status 200
         :headers {"Content-Type" "application/json"}
         :body (json/write-str result)})
      {:status 400
       :headers {"Content-Type" "application/json"}
       :body (json/write-str {:error "No symptoms provided."})})))
