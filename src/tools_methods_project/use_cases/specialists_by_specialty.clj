(ns tools-methods-project.use-cases.specialists-by-specialty
  (:require [tools-methods-project.db :refer [datasource]]
            [next.jdbc :as jdbc]
            [honey.sql :as sql]))

(defn get-specialists-by-specialty
  [specialty]
  (let [query (sql/format {:select [:name :specialty]
                           :from   [:specialist]
                           :where  [:= :specialty specialty]})]
    (jdbc/execute! datasource query)))

(doseq [specialist (get-specialists-by-specialty "Neurologist")]
  (println specialist))