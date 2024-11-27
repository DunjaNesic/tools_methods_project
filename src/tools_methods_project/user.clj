(ns tools-methods-project.user
  (:require [next.jdbc :as jdbc]
            [tools-methods-project.db :as db]
            [buddy.hashers :as hashers]))

(defn get-all-patients []
  (let [query "SELECT * FROM PATIENT"]
    (jdbc/execute! db/datasource [query])))

(defn register-patient [email password]
  (let [hashed-password (hashers/derive password)
        query "INSERT INTO PATIENT (email, password) VALUES (?, ?)"]
    (try
      (jdbc/execute! db/datasource [query email hashed-password])
      {:status :success, :message "Patient registered successfully"}
      (catch Exception e
        {:status :error, :message (.getMessage e)}))))

(def sessions (atom {}))

(defn login-patient [email password]
  (let [query "SELECT * FROM PATIENT WHERE email = ?"
        result (jdbc/execute! db/datasource [query email])]
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

;; Not sure how to write tests for this