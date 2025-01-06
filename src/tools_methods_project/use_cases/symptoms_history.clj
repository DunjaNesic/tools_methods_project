(ns tools-methods-project.use-cases.symptoms-history
  (:require [next.jdbc :as jdbc]
            [honey.sql :as sql]
            [clojure.string]
            [tools-methods-project.db :as db]))

(def symptom-history (atom []))

(defn format-symptoms
  "Converting a list of symptom keywords into a comma-separated string"
  [symptoms]
  (clojure.string/join ", " (map name symptoms)))

;; (defn add-to-history
;;   "Adds new symptoms to the history along with the current date and time."
;;   [symptoms]
;;   (let [timestamp (java.time.LocalDateTime/now)
;;         entry {:symptoms symptoms :date timestamp}]
;;     (swap! symptom-history conj entry)))

(defn add-to-history
  "Adds new symptoms to the history along with the current date and time"
  [symptoms user-id]
  (let [formatted-symptoms (format-symptoms symptoms)
        timestamp (java.time.LocalDateTime/now)
        query (sql/format {:insert-into :SYMPTOMS_HISTORY
                           :columns [:user_id :symptom :date_time]
                           :values [[user-id formatted-symptoms timestamp]]})]
    (try
      (jdbc/execute! db/datasource query)
      {:status :success, :message "Symptoms added to history"}
      (catch Exception e
        {:status :error, :message (.getMessage e)}))))

;; (defn get-history
;;   "Retrieves the history of all symptoms ever recorded."
;;   []
;;   @symptom-history)

(defn get-history
  "Retrieves the history of all symptoms ever recorded"
  [user-id]
  (let [query (sql/format {:select [:*]
                           :from [:SYMPTOMS_HISTORY]
                           :where [:= :user_id user-id]})]
    (try
      (jdbc/execute! db/datasource query)
      (catch Exception e
        {:status :error, :message (.getMessage e)}))))

;; (defn clear-history
;;   "Clears the symptom history."
;;   []
;;   (reset! symptom-history []))

(defn clear-history
  "Clears the symptom history"
  [user-id]
  (let [query (sql/format {:delete-from :SYMPTOMS_HISTORY
                           :where [:= :user_id user-id]})]
    (try
      (jdbc/execute! db/datasource query)
      {:status :success, :message "Symptom history cleared"}
      (catch Exception e
        {:status :error, :message (.getMessage e)}))))