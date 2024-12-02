(ns tools-methods-project.personalized-treatment-handler
  (:require [ring.adapter.jetty :refer [run-jetty]]
            [clojure.data.json :as json]
            [tools-methods-project.personalized-treatment :refer [recommend-treatment]]))

(defn personalized-treatment [request]
  (let [user-data (json/read-str (slurp (:body request)) :key-fn keyword)
        personalized-treatment (recommend-treatment user-data)]
    {:status 200
     :headers {"Content-Type" "application/json"}
     :body (json/write-str personalized-treatment)}))