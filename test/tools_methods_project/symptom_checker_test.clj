(ns tools-methods-project.symptom-checker-test
  (:require
   [tools-methods-project.use-cases.symptom-checker :refer :all]
   [midje.sweet :refer :all]))

(facts "User wants to get some diagnosis predictions"
       (fact "User wants to know his diagnosis based on input symptoms"
             (predict-diagnoses ["fever" "cough"]) => (contains ["Flu" "COVID-19" "Malaria" "Dengue" "Bronchitis" "Pneumonia" "Tuberculosis"])
             (predict-diagnoses ["headache"]) => ["Migraine" "Tension Headache" "Cluster Headache" "Sinusitis" "Brain cancer"]
             (predict-diagnoses ["unknown symptom"]) => []))

(facts "User wants to get some specialist recommendantions"
       (fact "User wants to know which specialists are good for him based on his potential diagnosis"
             (recommend-specialists ["Flu" "COVID-19"]) => (contains ["General Practitioner" "Infectious Disease Specialist"])
             (recommend-specialists ["Heart Attack"]) => ["Cardiologist"]))
