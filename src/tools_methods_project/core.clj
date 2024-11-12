(ns tools-methods-project.core
  (:require
   [clojure.string :as string]))

(defn create-board
  "Creates a board (matrix) of hearts based on the size provided by the user."
  [size]
  (vec (repeat size (vec (repeat size "♥")))))

(defn print-board
  "Prints the board inside a grid"
  [board]
  (doseq [row board]
    (println (string/join " | " row))
    (println (string/join (repeat (count row) "- - ")))))

;; (def board (create-board 5))
;; (print-board board)

(defn my-map-conj [funkcija sekvenca]
  (reduce (fn [akumulator element]
            (conj akumulator (funkcija element)))
          []
          sekvenca))

(defn my-map-apply [funkcija sekvenca]
  (reduce (fn [akumulator element]
            (conj akumulator (apply funkcija [element])))
          []
          sekvenca))

(defn my-map-cons [funkcija sekvenca]
  (reduce (fn [akumulator element]
            (cons (funkcija element) akumulator))
          nil
          sekvenca))

(defn my-map-cons-reverse [funkcija sekvenca]
  (reverse (reduce (fn [akumulator element]
                     (cons (funkcija element) akumulator))
                   nil
                   sekvenca)))

(defn my-map-lazy_recursion [funkcija sekvenca]
  (lazy-seq
   (if (seq sekvenca)
     (cons (funkcija (first sekvenca)) (my-map-lazy_recursion funkcija (rest sekvenca)))
     nil)))

(defn my-map-when-let [funkcija sekvenca]
  (lazy-seq
   (when-let [s (seq sekvenca)]
     (cons (funkcija (first s))
           (my-map-when-let funkcija (rest s))))))

(defn my-map-rec [funkcija sekvenca]
  (if (seq sekvenca)
    (cons (funkcija (first sekvenca)) (my-map-rec funkcija (rest sekvenca)))
    sekvenca))

(defn my-map-recur [funkcija sekvenca]
  (loop [remaining sekvenca
         result []]
    (if (seq remaining)
      (recur (rest remaining) (conj result (funkcija (first remaining))))
      result)))

(defn my-map-recur-cons [funkcija sekvenca]
  (loop [remaining sekvenca
         result '()]
    (if (seq remaining)
      (recur (rest remaining) (cons (funkcija (first remaining)) result))
      (reverse result))))

(defn my-map-recur-next [funkcija sekvenca]
  (loop [remaining sekvenca
         result '()]
    (if (seq remaining)
      (recur (next remaining) (cons (funkcija (first remaining)) result))
      (reverse result))))


;; Problem 55, Count Occurences
;; Difficulty: medium
;; Write a function which returns a map containing
;; the number of occurences of each distinct item in a sequence.

;; (= (__ [1 1 2 3 2 1 1]) {1 4, 2 2, 3 1})
;; (= (__ [:b :a :b :a :b]) {:a 2, :b 3})
;; (= (__ '([1 2] [1 3] [1 3])) {[1 2] 1, [1 3] 2})

;; sa get se prvo pitam da li item, odnosno prvi clan vektora, postoji
;; unutar resulting-map-a. ako ne postoji, vratim 0
;; ako postoji, vratim vrednost koja je vezana za taj key (item)
;; onda inkrementiramo taj broj za 1 i dodelimo ga sa assoc da to bude
;; nova vrednost sa kojom asociramo taj item 

(defn my-frequencies [vector]
  (reduce (fn [resulting-map item]
            (assoc resulting-map item (inc (get resulting-map item 0))))
          {}
          vector))

(defn my-frequencies-rec [vector]
  (letfn [(count-items [counts remaining]
            (if (empty? remaining)
              counts
              (let [item (first remaining)
                    current-count (if (contains? counts item) (counts item) 0)]
                (count-items (conj counts [item (inc current-count)]) (rest remaining)))))]
    (count-items {} vector)))

(defn my-frequencies-recur [vector]
  (loop [counts {} remaining vector]
    (if (empty? remaining)
      counts
      (let [item (first remaining)
            current-count (if (contains? counts item) (counts item) 0)]
        (recur (assoc counts item (inc current-count)) (rest remaining))))))

(defn my-frequencies-distinct [vector]
  (into {}
        (map (fn [item]
               [item (count (filter #(= % item) vector))])
             (distinct vector))))
