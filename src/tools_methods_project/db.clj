(ns tools-methods-project.db
  (:require [next.jdbc :as jdbc]))

(def db-spec
  {:dbtype   "postgresql"
   :dbname   "healthcare-db"
   :host     "localhost"
   :port     5432
   :user     "postgres"
   :password "postgres"})

(def datasource (jdbc/get-datasource db-spec))