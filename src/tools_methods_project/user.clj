(ns tools-methods-project.user
  (:require [next.jdbc :as jdbc]
            [tools-methods-project.db :as db]
            [buddy.hashers :as hashers]
            [honey.sql :as sql]))

(defn get-all-specialists []
  (let [query (sql/format {:select [:*]
                           :from [:USERR]
                           :where [:= :user_type "specialist"]})]
    (jdbc/execute! db/datasource query)))

(defn register-user [email password name user-type specialty]
  (let [hashed-password (hashers/derive password)
        columns [:email :password :name :user_type]
        values [email hashed-password name user-type]

        [columns values] (if (= user-type "specialist")
                           [(conj columns :specialty) (conj values specialty)]
                           [columns values])
        query (sql/format {:insert-into :USERR
                           :columns columns
                           :values [values]})]
    (try
      (jdbc/execute! db/datasource query)
      {:status :success, :message "User registered successfully"}
      (catch Exception e
        {:status :error, :message (.getMessage e)}))))


(def sessions (atom {}))

(defn login-user [email password]
  (let [query (sql/format {:select [:*]
                           :from [:USERR]
                           :where [:= :email email]})
        result (jdbc/execute! db/datasource query)]
    (if-let [user (first result)]
      (let [stored-password (:userr/password user)]
        (if (hashers/check password stored-password)
          (do
            (swap! sessions assoc email (assoc user :status :online))
            {:status :success, :message "Login successful", :user user})
          {:status :error, :message "Invalid password"}))
      {:status :error, :message "Email not found"})))

(defn logout-user [email]
  (swap! sessions dissoc email)
  {:status :success, :message "Logged out successfully"})
