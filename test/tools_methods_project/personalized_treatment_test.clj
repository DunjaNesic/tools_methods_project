(ns tools-methods-project.personalized-treatment-test
  (:require
   [tools-methods-project.personalized-treatment :refer :all]
   [midje.sweet :refer :all]))

(facts "User wants to get personalized treatment"
       (fact "User hasn't entered any information, so no recommendations are given"
             (generate-recommendations nil nil nil)
             => {:diet [] :exercise [] :medications []})

       (fact "User with diabetes wants to get personalized treatment"
             (generate-recommendations ["diabetes"] nil nil)
             => {:diet ["Avoid sugar" "Include whole grains"]
                 :exercise ["Light cardio" "Yoga"]
                 :medications ["Metformin"]})

       (fact "User with an active lifestyle wants to get personalized treatment"
             (generate-recommendations nil "active" nil)
             => {:diet ["Maintain a balanced diet" "Increase protein post-workout"]
                 :exercise ["Focus on strength training" "Endurance exercises"]
                 :medications []}))
