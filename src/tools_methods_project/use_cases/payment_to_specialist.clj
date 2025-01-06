(ns tools-methods-project.use-cases.payment-to-specialist
  (:import (java.time Instant ZoneId ZonedDateTime)
           (java.time.format DateTimeFormatter)))


;; (def accountA (atom 200))
;; (def accountB (atom 100))

;; (defn transfer-money
;;   [accountA accountB amount]
;;   (when (< amount @accountA)
;;     (swap! accountA - amount)
;;     (swap! accountB + amount))
;;   {:accountA @accountA :accountB @accountB})

;; (defn transfer [from to amount]
;;   (if (<= 0 amount (@from :balance))
;;     (do (swap! from update :balance - amount)
;;         (swap! to update :balance + amount))
;;     (throw RuntimeException)))

;; (def a (atom 1 :validator pos?))
;; (swap! a #(- % 100))

;; dosync, alter, ref
;; odraditi ovo sa refom 
;; validator!!!
;; chapter 12 i 13, sa posebnom paznjom na multimetode
;; prosiriti svoj rad sa refovima atomima,
;; da nam to bude kao neka "mini baza", da dodamo validatore,
;; implementirati neku sinhronizaciju 


;; u refu imamo nes kao: { [userEmail specialistEmail] {:start-time <ms>, :end-time <ms>, :cost <number>} }
(def chat-sessions (ref {}))

(def price-per-minute 30)

(defn formatted-now []
  (let [formatter (DateTimeFormatter/ofPattern "yyyy-MM-dd HH:mm:ss")
        now       (Instant/now)
        zdt       (ZonedDateTime/ofInstant now (ZoneId/systemDefault))]
    (.format zdt formatter)))

(defn show-chat-session
  [user-email specialist-email]
  (get @chat-sessions [user-email specialist-email]))

(defn start-paid-chat
  [user-email specialist-email]
  (dosync
   (alter chat-sessions assoc [user-email specialist-email]
          {:start-time (System/currentTimeMillis)
           :end-time   nil
           :cost       nil}))
  (println (str "[" (formatted-now) "] Started paid chat for user: " user-email " with specialist: " specialist-email))
  {:domain-status :success
   :message       (str "Paid chat started: " user-email " → " specialist-email)})

(defn end-paid-chat
  "Finds the session, sets end-time, computes cost in RSD and returns a map with cost"
  [user-email specialist-email]
  (dosync
   (let [session           (get @chat-sessions [user-email specialist-email])
         end-time-ms       (System/currentTimeMillis)
         start-time-ms     (:start-time session)
         duration-ms       (when start-time-ms (- end-time-ms start-time-ms))
         duration-minutes  (when duration-ms (Math/ceil (/ duration-ms 60000.0)))
         total-cost        (when duration-minutes (* duration-minutes price-per-minute))
         updated-session   (-> session
                               (assoc :end-time end-time-ms)
                               (assoc :cost total-cost))]
     (alter chat-sessions assoc [user-email specialist-email] updated-session)
     (println (str "[" (formatted-now) "] Ended chat for user: " user-email " with specialist: "
                   specialist-email ". Duration = " duration-minutes " min, cost = " total-cost " RSD."))
     (println "Chat session details: " (show-chat-session user-email specialist-email))
     {:domain-status :success
      :cost          total-cost
      :message       (str "You owe " total-cost " RSD for " duration-minutes " minute(s) of chatting.")})))

(start-paid-chat "aaa@gmail.com" "drHouse@gmail.com")
(end-paid-chat "aaa@gmail.com" "drHouse@gmail.com")
