(ns tools-methods-project.payment-to-specialist-test
  (:require [tools-methods-project.use-cases.payment-to-specialist :refer :all]
            [midje.sweet :refer :all]))

;; (fact "User wants to transfer money from accountA to accountB"
;;       (reset! accountA 200)
;;       (reset! accountB 100)

;;       (transfer-money accountA accountB 5) => {:accountA 195, :accountB 105}
;;       @accountA => 195
;;       @accountB => 105)

;; (fact "User wants to send more money then he has as balance"
;;       (reset! accountA 200)
;;       (reset! accountB 100)

;;       (transfer-money accountA accountB 205)
;;       @accountA => 200
;;       @accountB => 100)

;; (fact "User wants to never get nil"
;;       (transfer-money accountA accountB 10) =not=> nil)

;; (fact ""
;;       (transfer {:balance 100} {:balance 50} 55)  =not=> nil)

;; (fact ""
;;       (transfer {:balance 40} {:balance 50} 500)  throws IllegalArgumentException)

;; (fact ""
;;       (let [pera (atom {:balance 100})
;;             mika (atom {:balance 50})]
;;         (transfer pera mika 10) =not=> nil
;;         @pera => {:balance 90}
;;         @mika => {:balance 60}))
