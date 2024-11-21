(ns tools-methods-project.specialist-chat)

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

;;hmmm ovo bih zapravo mogla u neki drugi nejmspejs al neka ga ovde za sad


;;u seminarskom nikako ne smemo da imamo globalnu!!
(def messages
  {"pjerkan" [{:message "hi" :sender "dunja"}]
   "zika" [{:message "hi" :sender "mika"}]})

;; (defn send-message
;;   [message sender receiver messages]
;;   (if (messages receiver)
;;     (conj (get messages receiver) {:message message :sender sender})
;;     (assoc messages receiver {:message message :sender sender})))

(defn send-message
  [message sender receiver messages]
  (update messages receiver conj {:message message :sender sender}))

;;prikazati sve poruke iz sanduceta za konkretnog korisnika

;; (defn show-messages
;;   [person1 person2]
;;   (let [podskup (get messages person1)]
;;     (doseq [v (get podskup person2)])))

(defn show-messages
  [person1 person2]
  (let [podskup (get messages person1)]
    (filter #(= (:sender %) person2) podskup)))


;;brave clojure chapter 11
;;nesto za svoj projekat
;;dodatno nesto za chat