(ns tools-methods-project.handlers.personalized-treatment-handler
  (:require [tools-methods-project.services.personalized-treatment-service :as servicee]
            [clojure.data.json :as json]))

(defn personalized-treatment [request]
  (let [user-data (json/read-str (slurp (:body request)) :key-fn keyword)
        personalized-treatment (servicee/recommend-treatment user-data)]
    {:status 200
     :headers {"Content-Type" "application/json"}
     :body (json/write-str personalized-treatment)}))
