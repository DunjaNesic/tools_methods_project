(ns tools-methods-project.core-test
  (:require [tools-methods-project.core :refer :all]
            [midje.sweet :refer :all]))

;;User wants to test wether his clothing mathes.
(fact "User chooses two flamboyant pieces
       - yellow and red. We want to tell him that is a disaster"
      (evaluate-outfit [:crvena :zuta]) => "Kombinacija je loša!")
(fact "User chooses two light pieces - white and white."
      (evaluate-outfit [:bela :bela]) => "Kombinacija je neutralna!")


(facts "about `first-element`"
       (fact "it normally returns the first element"
             (first-element [1 2 3] :default) => 1
             (first-element '(1 2 3) :default) => 1)

       (facts "korisnik nesto hoce sa odecom" =not=> nil
              (reccomend-what-to-wear) => truthy)

       (facts "about `my function`"
              (fact "it should not return 3"
                    (my-function) =not=> 3))

       (fact "default value is returned for empty sequences"
             (first-element [] :default) => :default
             (first-element '() :default) => :default
             (first-element nil :default) => :default
             (first-element (filter even? [1 3 5]) :default) => :default))