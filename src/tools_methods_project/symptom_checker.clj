(ns tools-methods-project.symptom-checker
  (:require [tools-methods-project.symptoms-history :as history]))

;;until I integrate with AI
(def symptom-disease-map
  {"fever" ["Flu" "COVID-19"]
   "cough" ["Bronchitis" "COVID-19"]
   "headache" ["Migraine" "Tension Headache"]
   "chest pain" ["Heart Attack" "GERD"]})

(def disease-specialist-map
  {"Flu" "General Practitioner"
   "COVID-19" "Infectious Disease Specialist"
   "Bronchitis" "Pulmologist"
   "Migraine" "Neurologist"
   "Heart Attack" "Cardiologist"
   "Tummy Ache" "Gastroenterologist"})

;;(get map key default-value)
(defn predict-diagnoses
  "Predicting possible diagnoses based on symptoms"
  [symptoms]
  (let [diagnoses (mapcat #(get symptom-disease-map % []) symptoms)]
    (distinct diagnoses)))

(defn recommend-specialists
  "Recommending specialist based on diagnoses"
  [diagnoses]
  (distinct (map #(get disease-specialist-map %) diagnoses)))

(defn check-symptoms
  "Accepting symptoms and calling functions for diagnoses and specialists."
  [symptoms]
  (history/add-to-history symptoms)
  (let [diagnoses (predict-diagnoses symptoms)
        specialists (recommend-specialists diagnoses)]
    {:diagnoses diagnoses
     :specialists specialists}))