(ns tools-methods-project.use-cases.symptoms-history)

(def symptom-history (atom []))

(defn add-to-history
  "Adds new symptoms to the history along with the current date and time."
  [symptoms]
  (let [timestamp (java.time.LocalDateTime/now)
        entry {:symptoms symptoms :date timestamp}]
    (swap! symptom-history conj entry)))

(defn get-history
  "Retrieves the history of all symptoms ever recorded."
  []
  @symptom-history)

(defn clear-history
  "Clears the symptom history."
  []
  (reset! symptom-history []))
