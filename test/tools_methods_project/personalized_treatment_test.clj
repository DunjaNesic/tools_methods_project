(ns tools-methods-project.personalized-treatment-test
  (:require
   [tools-methods-project.personalized-treatment :refer :all]
   [midje.sweet :refer :all]))


(facts "about `generate-recommendations` with no inputs"
       ;;it returns empty recommendations when no inputs are given
       (fact "User wants to get lifestyle recommendations based on his lifestyle"
             (generate-recommendations nil nil nil)
             => {:diet [] :exercise [] :medications []}))
