(ns tools-methods-project.handlers.specialist-handler
  (:require [tools-methods-project.use-cases.specialists-by-specialty :refer [get-specialists-by-specialty]]
            [ring.util.response :refer [response header]]
            [clojure.string :as str]
            [cheshire.core :refer [generate-string]]))

(defn parse-query-params [query-string]
  (when query-string
    (let [[key value] (str/split query-string #"=")]
      {(keyword key) value})))

(defn specialist-handler [request]
  (let [query-params (parse-query-params (:query-string request))
        specialty (:specialty query-params)]
    ;; (println "specijalnost je" specialty)
    (if specialty
      (let [specialists (get-specialists-by-specialty specialty)]
        (-> (response (generate-string specialists))
            (header "Content-Type" "application/json")))
      (-> (response (generate-string {:error "Specialty is required"}))
          (header "Content-Type" "application/json")
          (assoc :status 400)))))