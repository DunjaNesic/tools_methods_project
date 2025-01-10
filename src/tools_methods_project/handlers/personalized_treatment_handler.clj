(ns tools-methods-project.handlers.personalized-treatment-handler
  (:require [tools-methods-project.services.personalized-treatment-service :as servicee]
            [cheshire.core :as cheshire]))

(defn personalized-treatment [request]
  (try
    (let [body (slurp (:body request))
          parsed-body (cheshire/parse-string body true)
          personalized-treatment (servicee/recommend-treatment parsed-body)]
      (if (= (:status personalized-treatment) :success)
        {:status 200
         :headers {"Content-Type" "application/json"}
         :body (cheshire/generate-string personalized-treatment)}
        {:status 400
         :headers {"Content-Type" "application/json"}
         :body (cheshire/generate-string personalized-treatment)}))
    (catch Exception e
      (println "Error during personalized-treatment:" (.getMessage e))
      {:status 500
       :headers {"Content-Type" "application/json"}
       :body (cheshire/generate-string {:status "error"
                                        :message (.getMessage e)})})))
