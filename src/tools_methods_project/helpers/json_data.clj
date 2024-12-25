(ns tools-methods-project.helpers.json-data
  (:require [clojure.java.io :as io]
            [cheshire.core :as cheshire]))

(defn load-faqs [file-name]
  (try
    (with-open [reader (io/reader file-name)]
      (doall (cheshire/parse-stream reader true))) ;;moram ovo jer se u suprotnom stream zatvori
    (catch Exception e
      (println "Error reading file:" (.getMessage e))
      nil)))

(def faqs (load-faqs "/home/dunja/project/tools_methods_project/resources/faq.json"))
