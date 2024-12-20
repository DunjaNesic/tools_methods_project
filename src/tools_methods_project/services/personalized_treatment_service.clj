(ns tools-methods-project.services.personalized-treatment-service
  (:require [tools-methods-project.use-cases.personalized-treatment :as use-case]))

(def user-data
  {:medical-conditions ["diabetes" "hypertension"]
   :lifestyle "sedentary"
   :genetic-markers ["APOE-e4"]})

(defn recommend-treatment [user-data]
  (use-case/recommend-treatment user-data))

