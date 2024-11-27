(ns tools-methods-project.payment-to-specialist)

(def accountA (atom 200))
(def accountB (atom 100))

(defn transfer-money
  [accountA accountB amount]
  (when (< amount @accountA)
    (swap! accountA - amount)
    (swap! accountB + amount))
  {:accountA @accountA :accountB @accountB})

(defn transfer [from to amount]
  (if (<= 0 amount (@from :balance))
    (do (swap! from update :balance - amount)
        (swap! to update :balance + amount))
    (throw RuntimeException)))

(def a (atom 1 :validator pos?))
(swap! a #(- % 100))

;; dosync, alter, ref
;; odraditi ovo sa refom 
;; validator!!!
;; chapter 12 i 13, sa posebnom paznjom na multimetode
;; prosiriti svoj rad sa refovima atomima,
;; da nam to bude kao neka "mini baza", da dodamo validatore,
;; implementirati neku sinhronizaciju 