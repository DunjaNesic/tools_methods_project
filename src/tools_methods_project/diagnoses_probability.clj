(ns tools-methods-project.diagnoses-probability)

(def symptom-disease-map-probs
  {"fever" {"Flu" 0.5, "COVID-19" 0.3, "Malaria" 0.15, "Dengue" 0.05}
   "cough" {"Bronchitis" 0.35, "COVID-19" 0.4, "Pneumonia" 0.2, "Tuberculosis" 0.05}
   "headache" {"Migraine" 0.6, "Tension Headache" 0.25, "Cluster Headache" 0.1, "Sinusitis" 0.04 "Brain cancer" 0.01}
   "chest pain" {"Heart Attack" 0.7, "GERD" 0.15, "Angina" 0.1, "Panic Attack" 0.05}
   "fatigue" {"Anemia" 0.5, "Hypothyroidism" 0.3, "Chronic Fatigue Syndrome" 0.1, "Depression" 0.1}
   "sore throat" {"Strep Throat" 0.45, "Flu" 0.35, "COVID-19" 0.15, "Tonsillitis" 0.05}
   "shortness of breath" {"Asthma" 0.5, "COPD" 0.3, "COVID-19" 0.15, "Pneumonia" 0.05}
   "rash" {"Allergy" 0.6, "Chickenpox" 0.2, "Measles" 0.15, "Eczema" 0.05}
   "abdominal pain" {"Appendicitis" 0.4, "IBS" 0.35, "Gastritis" 0.2, "Gallstones" 0.05}
   "dizziness" {"Vertigo" 0.45, "Low Blood Pressure" 0.3, "Dehydration" 0.2, "Anemia" 0.05}})

(defn decision-tree-probability
  [symptoms]
  (let [probabilities (apply merge-with +
                             (map (fn [symptom]
                                    (get symptom-disease-map-probs symptom {}))
                                  symptoms))]
    (if (empty? probabilities)
      {"Unknown" 1.0}
      (let [total (reduce + (vals probabilities))]
        (into {}
              (map (fn [[disease prob]]
                     [disease (/ prob total)])
                   probabilities))))))
