(ns tools-methods-project.personalized-treatment)

(def medical-condition-recommendations
  {"diabetes" {:diet ["Avoid sugar" "Include whole grains"]
               :exercise ["Light cardio" "Yoga"]
               :medications ["Metformin"]}
   "hypertension" {:diet ["Reduce salt" "Eat potassium-rich foods"]
                   :exercise ["Walking" "Swimming"]
                   :medications ["ACE inhibitors" "Beta-blockers"]}
   "obesity" {:diet ["Low-carb diet" "Increase protein intake"]
              :exercise ["High-intensity interval training" "Strength training"]
              :medications ["Orlistat"]}})

(def lifestyle-recommendations
  {"sedentary" {:diet ["Reduce calorie intake" "Add fruits and vegetables"]
                :exercise ["Start with short walks" "Simple stretches"]}
   "active" {:diet ["Maintain a balanced diet" "Increase protein post-workout"]
             :exercise ["Focus on strength training" "Endurance exercises"]}})

(def genetic-marker-recommendations
  {"APOE-e4" {:diet ["Increase omega-3 intake" "Reduce saturated fats"]
              :exercise ["Regular cardio" "Brain-stimulating activities"]}
   "MTHFR" {:diet ["Supplement folate" "Avoid processed foods"]}})

;;(get map key default-value)
(defn generate-recommendations
  [medical-conditions lifestyle genetic-markers]
  (let [medical-rec (map #(get medical-condition-recommendations % {}) medical-conditions)
        lifestyle-rec (get lifestyle-recommendations lifestyle {})
        genetic-rec (map #(get genetic-marker-recommendations % {}) genetic-markers)] {:diet (distinct (mapcat :diet (concat medical-rec [lifestyle-rec] genetic-rec)))
                                                                                       :exercise (distinct (mapcat :exercise (concat medical-rec [lifestyle-rec] genetic-rec)))
                                                                                       :medications (distinct (mapcat :medications medical-rec))}))

(defn recommend-treatment
  "Generating a personalized treatment plan"
  [user-data]
  (let [{:keys [medical-conditions lifestyle genetic-markers]} user-data]
    (generate-recommendations medical-conditions lifestyle genetic-markers)))
