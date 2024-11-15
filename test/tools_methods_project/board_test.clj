(ns tools-methods-project.board-test
  (:require
   [tools-methods-project.core :refer :all]
   [clojure.string :as str]
   [midje.sweet :refer :all]))

(println "You should expect to see three failures below.")

(facts "about `first-element`"
       (fact "it normally returns the first element"
             (first-element [1 2 3] :default) => 1
             (first-element '(1 2 3) :default) => 1)

       (facts "korisnik nesto hoce sa odecom" =not=> nil
              (reccomend-what-to-wear) => truthy)

       (facts "about my function"
              (fact "it should not return 3"
                    (my-function) =not=> 3))


       (fact "`split` splits strings on regular expressions and returns a vector"
             (str/split "a/b/c" #"/") => ["a" "b" "c"]
             (str/split "" #"irrelevant") => [""]
             (str/split "no regexp matches" #"a+\s+[ab]") => ["no regexp matches"])

  ;; I'm a little unsure how Clojure types map onto the Lisp I'm used to.
       (fact "default value is returned for empty sequences"
             (first-element [] :default) => :default
             (first-element '() :default) => :default
             (first-element nil :default) => :default
             (first-element (filter even? [1 3 5]) :default) => :default))