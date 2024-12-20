(ns tools-methods-project.symptoms-history-test
  (:require [midje.sweet :refer :all]
            [tools-methods-project.use-cases.symptoms-history :refer :all]))

(fact "User want to clear symptoms history"
      (add-to-history ["fever"])
      (clear-history)
      (get-history) => [])

(fact "User wants to view his symptom history"
      (reset! symptom-history [{:symptoms ["fever" "cough"] :date "2024-11-24T10:30:00"}
                               {:symptoms ["headache"] :date "2024-11-24T11:00:00"}])
      (get-history) => [{:symptoms ["fever" "cough"] :date "2024-11-24T10:30:00"}
                        {:symptoms ["headache"] :date "2024-11-24T11:00:00"}])

(fact "User wants to add symptoms to history"
      (reset! symptom-history [])
      (add-to-history ["fever" "chest pain"])
      @symptom-history => [{:symptoms ["fever" "chest pain"] :date anything}])
      ;; Nemam pojma sta treba da bude date, kako da proverimmm

(fact "User want to be able to add multiple symptom entries"
      (clear-history)
      (add-to-history ["fever"])
      (add-to-history ["headache"])
      (count (get-history)) => 2)