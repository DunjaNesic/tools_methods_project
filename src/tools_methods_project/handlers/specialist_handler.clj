(ns tools-methods-project.handlers.specialist-handler
  (:require [tools-methods-project.use-cases.specialists-by-specialty :refer [get-specialists-by-specialty]]
            [clojure.string :as str]
            [tools-methods-project.helpers.response :refer [json-response]]
            [tools-methods-project.user :refer [get-all-specialists get-all-patients]]))

(defn parse-query-params [query-string]
  (when query-string
    (let [[key value] (str/split query-string #"=")]
      {(keyword key) value})))

(defn specialist-handler [request]
  (let [query-params (parse-query-params (:query-string request))
        specialty (:specialty query-params)]
    (if specialty
      (json-response 200 {:status "success"
                          :specialists (get-specialists-by-specialty specialty)})
      (json-response 400 {:error "Specialty is required"}))))

(defn handle-get-all-specialists []
  (json-response 200 {:status "success"
                      :specialists (get-all-specialists)}))

(defn handle-get-all-patients []
  (json-response 200 {:status "success"
                      :patients (get-all-patients)}))