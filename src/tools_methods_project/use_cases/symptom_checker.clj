(ns tools-methods-project.use-cases.symptom-checker
  (:require [tools-methods-project.use-cases.symptoms-history :as history]
            [tools-methods-project.helpers.csv-data :as csv]
            [clj-yaml.core :as yaml]
            [clojure.java.io :as io]))

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


(defn load-disease-specialist-map
  "Loads the disease-specialist map from a YAML file."
  [yaml-file]
  (let [parsed-yaml (yaml/parse-string (slurp (io/resource yaml-file)))]
    (reduce (fn [acc {:keys [name diseases]}]
              (reduce (fn [inner-acc disease]
                        (assoc inner-acc disease name))
                      acc
                      diseases))
            {}
            (:specialists parsed-yaml))))

(def disease-specialist-map
  (load-disease-specialist-map "disease-specialist.yaml"))

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
  [symptoms user-id]
  (if (validate-symptoms symptoms)
    (do
      (history/add-to-history symptoms user-id)
      (let [diagnoses (predict-diagnoses symptoms)
            specialists (recommend-specialists diagnoses)]
        {:status :success
         :diagnoses diagnoses
         :specialists specialists}))
    {:status :error
     :message "Invalid symptoms provided. Please check your input."}))


;; (check-symptoms [:back_pain :mood_swings])
;; (check-symptoms [:continuous_sneezing :shivering :chills :cough :watering_from_eyes])
