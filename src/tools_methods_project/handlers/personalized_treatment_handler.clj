(ns tools-methods-project.handlers.personalized-treatment-handler
  (:require [tools-methods-project.services.personalized-treatment-service :as servicee]
            [tools-methods-project.helpers.response :refer [json-response]]
            [cheshire.core :as cheshire]))

(defn personalized-treatment [request]
  (try
    (let [body                   (slurp (:body request))
          parsed-body            (cheshire/parse-string body true)
          personalized-treatment (servicee/recommend-treatment parsed-body)]
      (if (= (:status personalized-treatment) :success)
        (json-response 200 personalized-treatment)
        (json-response 400 personalized-treatment)))
    (catch Exception e
      (json-response 500 {:status  "error"
                          :message (.getMessage e)}))))
