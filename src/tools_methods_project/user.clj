(ns tools-methods-project.user
  (:require [next.jdbc :as jdbc]
            [tools-methods-project.db :as db]
            [buddy.hashers :as hashers]
            [honey.sql :as sql]))

(defn get-all-patients []
  (let [query (sql/format {:select [:*] :from [:PATIENT]})]
    (jdbc/execute! db/datasource query)))

(defn register-patient [email password]
  (let [hashed-password (hashers/derive password)
        query (sql/format {:insert-into :PATIENT
                           :columns [:email :password]
                           :values [[email hashed-password]]})]
    (try
      (jdbc/execute! db/datasource query)
      {:status :success, :message "Patient registered successfully"}
      (catch Exception e
        {:status :error, :message (.getMessage e)}))))

(def sessions (atom {}))

(defn login-patient [email password]
  (let [query (sql/format {:select [:*]
                           :from [:PATIENT]
                           :where [:= :email email]})
        result (jdbc/execute! db/datasource query)]
    (if-let [patient (first result)]
      (let [stored-password (:patient/password patient)]
        (if (hashers/check password stored-password)
          (do
            (swap! sessions assoc email (assoc patient :status :online))
            {:status :success, :message "Login successful", :patient patient})
          {:status :error, :message "Invalid password"}))
      {:status :error, :message "Email not found"})))

(defn logout-patient [email]
  (swap! sessions dissoc email)
  {:status :success, :message "Logged out successfully"})
