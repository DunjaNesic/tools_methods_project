(ns tools-methods-project.symptom-checker-test
  (:require
   [tools-methods-project.symptom-checker :refer :all]
   [midje.sweet :refer :all]))

(facts "User wants to get some diagnosis predictions"
       (fact "User wants to know his diagnosis based on input symptoms"
             (predict-diagnoses ["fever" "cough"]) => (contains ["Flu" "COVID-19" "Bronchitis"])
             (predict-diagnoses ["headache"]) => ["Migraine" "Tension Headache"]
             (predict-diagnoses ["unknown symptom"]) => []))

(facts "User wants to get some specialist recommendantions"
       (fact "User wants to know which specialists are good for him based on his potential diagnosis"
             (recommend-specialists ["Flu" "COVID-19"]) => (contains ["General Practitioner" "Infectious Disease Specialist"])
             (recommend-specialists ["Heart Attack"]) => ["Cardiologist"]))


;;ovo vrv nece da postoji u samoj app, al aj neka ga za sad
(facts "User wants predictions and recommendations combined"
       (fact "User wants to know predictions and recommendations"
             (check-symptoms ["fever" "cough"])
             => {:diagnoses (contains ["Flu" "COVID-19" "Bronchitis"])
                 :specialists (contains ["General Practitioner" "Infectious Disease Specialist" "Pulmonologist"])}))