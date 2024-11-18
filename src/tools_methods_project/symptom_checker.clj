(ns tools-methods-project.symptom-checker)

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
  "Accepting symptoms and calling functions for diagnoses and specialists"
  [symptoms]
  (let [diagnoses (predict-diagnoses symptoms)
        specialists (recommend-specialists diagnoses)]
    {:diagnoses diagnoses
     :specialists specialists}))

;;to dooooo :)
;; sta bih mogla da dodam da korisniku bude lakse - da se specijalista pronadje
;; na osnovu grada u kom korisnik zivi ili nekih gradova u blizini 
;;trenutno stanje je takvo da se korisniku preporucuje koji tip lekara njemu treba
;;a sad zelim i tacno nekog lekara koj ima svoju ordinaciju u gradu iz kojeg je korisnik
;;kada se pronadju ti lekari, korisnik dobija opcija i da se javi lekarima preko ceta i
;;zakaze svoj pregled`

