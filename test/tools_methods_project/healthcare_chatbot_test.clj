(ns tools-methods-project.healthcare-chatbot-test
  (:require [tools-methods-project.use-cases.healthcare-chatbot :refer :all]
            [midje.sweet :refer :all]))

;; (facts "User wants help from chatbot"
;;        (fact "User wants the answer for a known question"
;;              (answer-faq "What is flu?") => "Flu is a viral infection that attacks your respiratory system.")
;;        (fact "User wants the answer for an unknown question"
;;              (answer-faq "Unknown question") => "I'm sorry, I don't have an answer to that question."))