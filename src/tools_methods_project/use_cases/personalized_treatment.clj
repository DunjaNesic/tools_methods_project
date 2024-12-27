(ns tools-methods-project.use-cases.personalized-treatment
  (:require [clojure.edn :as edn]))

(def recommendations (edn/read-string (slurp "/home/dunja/project/tools_methods_project/resources/recommendations.edn")))
(def medical-condition-recommendations (:medical-condition-recommendations recommendations))
(def lifestyle-recommendations (:lifestyle-recommendations recommendations))
(def genetic-marker-recommendations (:genetic-marker-recommendations recommendations))

(defn merge-recommendations [categories & sources]
  (reduce (fn [acc category]
            (assoc acc category
                   (distinct (mapcat #(get % category []) (apply concat sources)))))
          {}
          categories))

(defn validate-input
  [input valid-options]
  (if (and input (not (every? (set valid-options) input)))
    (throw (ex-info "Invalid input detected. Please write your input nicely and without any typos" {:input input}))
    input))

(defn generate-recommendations
  [medical-conditions lifestyle genetic-markers]
  (let [categories [:diet :exercise :medications]
        medical-rec (map #(get medical-condition-recommendations % {}) medical-conditions)
        lifestyle-rec (if lifestyle
                        (get lifestyle-recommendations lifestyle {})
                        {})
        genetic-rec (map #(get genetic-marker-recommendations % {}) genetic-markers)]
    (merge-recommendations categories
                           (concat medical-rec [lifestyle-rec] genetic-rec))))


(defn recommend-treatment
  [user-data]
  (let [{:keys [medical-conditions lifestyle genetic-markers]} user-data]
    (validate-input medical-conditions (keys medical-condition-recommendations))
    (when (some? lifestyle)
      (validate-input [lifestyle] (keys lifestyle-recommendations)))
    (validate-input genetic-markers (keys genetic-marker-recommendations))
    (generate-recommendations medical-conditions lifestyle genetic-markers)))
