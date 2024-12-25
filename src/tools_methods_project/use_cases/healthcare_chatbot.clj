(ns tools-methods-project.use-cases.healthcare-chatbot
  (:require [clojure.string :as str]
            [clojure.set :as set]
            [tools-methods-project.helpers.json-data :as json]))

(defn normalize [text]
  (-> text
      (str/lower-case)
      (str/replace #"[^\w\s]" "")))

(defn match-question
  [faqs input-question]
  (let [normalized-input (normalize input-question)
        similarity (fn [q1 q2]
                     (count (set/intersection
                             (set (str/split (normalize q1) #"\s+"))
                             (set (str/split (normalize q2) #"\s+")))))
        scored-faqs (map #(assoc % :score (similarity (:question %) normalized-input)) faqs)
        best-match (first (sort-by :score > scored-faqs))]
    (if (and best-match (> (:score best-match) 0))
      best-match
      nil)))

(defn find-answer-nlp
  "Finds the answer using fuzzy matching"
  [faqs input-question]
  (let [matched (match-question faqs input-question)]
    (if matched
      (:answer matched)
      "I'm sorry, I don't have an answer to that question")))

;;nprr
(find-answer-nlp json/faqs "Medications while I have pregnancy?")
