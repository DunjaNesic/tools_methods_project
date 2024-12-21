(ns tools-methods-project.helpers.csv-data
  (:require [clojure.data.csv :as csv]
            [clojure.java.io :as io]
            [clojure.string :as string]))

(defn load-csv [file]
  (with-open [reader (io/reader file)]
    (let [rows (doall (csv/read-csv reader))]
      (map #(zipmap (map keyword (first rows)) %) (rest rows)))))

(defn process-data
  "Features je vektor nula i jedinica preko koga znamo koje simptome osoba ima/nema,
a labels su one-hot enkodovane oznake za svaku dijagnozu"
  [data symptom-keys label-key]
  (let [features (map (fn [row]
                        (map (fn [symptom]
                               (let [value (get row symptom "0")]
                                 (if (string/blank? value)
                                   0.0
                                   (Double/parseDouble value))))
                             symptom-keys))
                      data)
        labels (map #(keyword (get % label-key)) data)
        unique-labels (vec (distinct labels))
        label-to-index (zipmap unique-labels (range))]
    {:features (vec (map vec features))
     :labels (vec (mapv #(assoc (vec (repeat (count unique-labels) 0))
                                (label-to-index %) 1)
                        labels))
     :label-mapping label-to-index}))


(def training-data (load-csv "/home/dunja/project/tools_methods_project/resources/diagnoses.csv"))
(def test-data (load-csv "/home/dunja/project/tools_methods_project/resources/diagnoses-test.csv"))

(def symptom-keys
  (let [headers (keys (first training-data))]
    (remove #(= % :prognosis) headers)))

(def processed-training (process-data training-data symptom-keys :prognosis))
(def processed-test (process-data test-data symptom-keys :prognosis))

;; (println "First 25 Symptom Keys:" (take 25 symptom-keys))
;; (println "Processed Training Data:" (take 25 processed-training))
;; (println "Processed Test Data:" processed-test) 
