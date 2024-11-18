(ns tools-methods-project.healthcare-chatbot-test
  (:require [tools-methods-project.healthcare-chatbot :refer :all]
            [midje.sweet :refer :all]))

(facts "about `answer-faq`"
       (fact "it returns the correct answer for a known question"
             (answer-faq "What is flu?") => "Flu is a viral infection that attacks your respiratory system.")
       (fact "it returns a default response for an unknown question"
             (answer-faq "Unknown question") => "I'm sorry, I don't have an answer to that question."))