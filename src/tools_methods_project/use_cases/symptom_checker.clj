(ns tools-methods-project.use-cases.symptom-checker
  (:require [tools-methods-project.use-cases.symptoms-history :as history]
            [tools-methods-project.helpers.csv-data :as csv]))

(defn build-symptom-disease-map
  "Creating a map of symptoms with associated diseases from a CSV file"
  [data symptom-keys label-key]
  (reduce (fn [acc row]
            (reduce (fn [inner-acc symptom]
                      (let [value (get row symptom "0")
                            disease (get row label-key)]
                        (if (and disease (= value "1"))
                          (update inner-acc symptom
                                  (fnil conj #{})
                                  disease)
                          inner-acc)))
                    acc
                    symptom-keys))
          {}
          data))

(def symptom-disease-map
  (build-symptom-disease-map csv/test-data csv/symptom-keys :prognosis))


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
  [symptoms]
  (let [disease-counts (frequencies (mapcat #(get symptom-disease-map % []) symptoms))]
    (if (empty? disease-counts)
      {"Unknown" 1.0}
      (let [total (reduce + (vals disease-counts))]
        (into {}
              (map (fn [[disease count]]
                     [disease (/ count total)])
                   disease-counts))))))

(defn recommend-specialists
  "Recommending specialist based on diagnoses"
  [diagnoses]
  (distinct (remove nil? (map #(get disease-specialist-map %) (keys diagnoses)))))

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

;;(check-symptoms [:back_pain :mood_swings])
;;(check-symptoms [:continuous_sneezing :shivering :chills :cough :watering_from_eyes])
