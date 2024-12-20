(ns tools-methods-project.use-cases.specialists-by-specialty)

(def specialists
  {"Andjela Mircetic" "General Practitioner"
   "Petar Nikodijevic" "Infectious Disease Specialist"
   "Mladen Mijailovic" "Pulmologist"
   "Dunja Nesic" "Neurologist"
   "Sara Djokic" "Neurologist"
   "Sandra Kovacevic" "Cardiologist"
   "Iva Djokovic" "Gastroenterologist"})

(defn get-specialists-by-specialty
  [specialty]
  (filter #(= (val %) specialty) specialists))

(doseq [specialist (get-specialists-by-specialty "Neurologist")]
  (println (key specialist)))