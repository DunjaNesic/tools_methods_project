(ns tools-methods-project.use-cases.symptom-checker
  (:require [tools-methods-project.use-cases.symptoms-history :as history]))

(def symptom-disease-map
  {"fever" ["Flu" "COVID-19" "Malaria" "Dengue"]
   "cough" ["Bronchitis" "COVID-19" "Pneumonia" "Tuberculosis"]
   "headache" ["Migraine" "Tension Headache" "Cluster Headache" "Sinusitis" "Brain cancer"]
   "chest pain" ["Heart Attack" "GERD" "Angina" "Panic Attack"]
   "fatigue" ["Anemia" "Hypothyroidism" "Chronic Fatigue Syndrome" "Depression"]
   "sore throat" ["Strep Throat" "Flu" "COVID-19" "Tonsillitis"]
   "shortness of breath" ["Asthma" "COPD" "COVID-19" "Pneumonia"]
   "rash" ["Allergy" "Chickenpox" "Measles" "Eczema"]
   "abdominal pain" ["Appendicitis" "IBS" "Gastritis" "Gallstones"]
   "dizziness" ["Vertigo" "Low Blood Pressure" "Dehydration" "Anemia"]})


(def disease-specialist-map
  {"Flu" "General Practitioner"
   "COVID-19" "Infectious Disease Specialist"
   "Malaria" "Infectious Disease Specialist"
   "Dengue" "Infectious Disease Specialist"
   "Bronchitis" "Pulmonologist"
   "Pneumonia" "Pulmonologist"
   "Tuberculosis" "Pulmonologist"
   "Migraine" "Neurologist"
   "Brain cancer" "Neurologist"
   "Tension Headache" "General Practitioner"
   "Cluster Headache" "Neurologist"
   "Sinusitis" "ENT Specialist"
   "Heart Attack" "Cardiologist"
   "GERD" "Gastroenterologist"
   "Angina" "Cardiologist"
   "Panic Attack" "Psychiatrist"
   "Anemia" "Hematologist"
   "Hypothyroidism" "Endocrinologist"
   "Chronic Fatigue Syndrome" "General Practitioner"
   "Depression" "Psychiatrist"
   "Strep Throat" "ENT Specialist"
   "Tonsillitis" "ENT Specialist"
   "Asthma" "Pulmonologist"
   "COPD" "Pulmonologist"
   "Allergy" "Allergist"
   "Chickenpox" "Dermatologist"
   "Measles" "Infectious Disease Specialist"
   "Eczema" "Dermatologist"
   "Appendicitis" "Surgeon"
   "IBS" "Gastroenterologist"
   "Gastritis" "Gastroenterologist"
   "Gallstones" "Gastroenterologist"
   "Vertigo" "Neurologist"
   "Low Blood Pressure" "Cardiologist"
   "Dehydration" "General Practitioner"})

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

(def valid-symptom?
  (fn [symptom]
    (contains? symptom-disease-map symptom)))

(defn validate-symptoms
  [symptoms]
  (every? valid-symptom? symptoms))

(defn check-symptoms
  "Accepting symptoms and calling functions for diagnoses and specialists."
  [symptoms]
  (if (validate-symptoms symptoms)
    (do
      (history/add-to-history symptoms)
      (let [diagnoses (predict-diagnoses symptoms)
            specialists (recommend-specialists diagnoses)]
        {:diagnoses diagnoses
         :specialists specialists}))
    {:error "Invalid symptoms provided. Please check your input."}))