(ns tools-methods-project.helpers.csv-data
  (:require [clojure.data.csv :as csv]
            [clojure.java.io :as io]))

(defn load-csv [file]
  (with-open [reader (io/reader file)]
    (let [rows (doall (csv/read-csv reader))]
      (map #(zipmap (map keyword (first rows)) %) (rest rows)))))

(def test-data (load-csv "/home/dunja/project/tools_methods_project/resources/diagnoses-test.csv"))

(def symptom-keys
  (let [headers (keys (first test-data))]
    (remove #(= % :prognosis) headers)))