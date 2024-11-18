(ns tools-methods-project.symptom-checker-test
  (:require
   [tools-methods-project.symptom-checker :refer :all]
   [midje.sweet :refer :all]))

(facts "about `predict-diagnoses`"
       ;;mora da pise ko, sta i zasto!! a ne ovako: "it returns diagnoses based on input symptoms"
       (fact "User wants to know his diagnosis based on input symptoms"
             (predict-diagnoses ["fever" "cough"]) => (contains ["Flu" "COVID-19" "Bronchitis"])
             (predict-diagnoses ["headache"]) => ["Migraine" "Tension Headache"]
             (predict-diagnoses ["unknown symptom"]) => []))

(facts "about `recommend-specialists`"
       (fact "User wants to know which specialists are good for him based on his potential diagnosis"
             (recommend-specialists ["Flu" "COVID-19"]) => (contains ["General Practitioner" "Infectious Disease Specialist"])
             (recommend-specialists ["Heart Attack"]) => ["Cardiologist"]))

(facts "about `check-symptoms`"
       ;;it combines predictions and recommendations
       (fact "User wants to combine predictions and recommendations"
             (check-symptoms ["fever" "cough"])
             => {:diagnoses (contains ["Flu" "COVID-19" "Bronchitis"])
                 :specialists (contains ["General Practitioner" "Infectious Disease Specialist" "Pulmonologist"])}))