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

(defn login
  "Login with username"
  [username]
  (if (seq username)
    (do
      (println "Login successful!")
      username)
    (println "Please enter a valid username.")))

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

(defn my-map-rec [funkcija sekvenca]
  (if (seq sekvenca)
    (cons (funkcija (first sekvenca)) (my-map-rec funkcija (rest sekvenca)))
    sekvenca))

(defn my-map-lazy-rec [funkcija sekvenca]
  (lazy-seq
   (if (seq sekvenca)
     (cons (funkcija (first sekvenca)) (my-map-lazy-rec funkcija (rest sekvenca)))
     nil)))

(defn my-map-when-let [funkcija sekvenca]
  (lazy-seq
   (when-let [s (seq sekvenca)]
     (cons (funkcija (first s))
           (my-map-when-let funkcija (rest s))))))

(defn my-map-recur-conj [funkcija sekvenca]
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

(defn my-frequencies [vector]
  (reduce (fn [resulting-map item]
            (assoc resulting-map item (inc (get resulting-map item 0))))
          {}
          vector))

(defn my-frequencies-rec-letfn [vector]
  (letfn [(count-items [counts remaining]
            (if (empty? remaining)
              counts
              (let [item (first remaining)
                    current-count (if (contains? counts item) (counts item) 0)]
                (count-items (conj counts [item (inc current-count)]) (rest remaining)))))]
    (count-items {} vector)))

(defn my-frequencies-rec [vector counts]
  (if (empty? vector)
    counts
    (let [item (first vector)
          current-count (get counts item 0)]
      (my-frequencies-rec (rest vector) (assoc counts item (inc current-count))))))

(defn my-frequencies-rec2 [vector]
  (if (empty? vector)
    {}
    (let [item (first vector)
          rest-frequencies (my-frequencies-rec2 (rest vector))]
      (assoc rest-frequencies item (inc (get rest-frequencies item 0))))))


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

(defn sum-list [numbers]
  (loop [remaining-numbers numbers
         total 0]
    (if (empty? remaining-numbers)
      total
      (let [[head & remaining] remaining-numbers]
        (recur remaining (+ total head))))))

(defn sum-without-recur
  ([vals] (sum-without-recur vals 0))
  ([vals accumulating-total]
   (if (empty? vals)
     accumulating-total
     (sum-without-recur (rest vals) (+ (first vals) accumulating-total)))))

(def calories [2.3 4.6 6.6 2.1])
(def fat [1 2.2 3 8.1])
(defn merge-food-stuff
  [calories fats]
  {:calories calories
   :fat fats})

(defmacro my-print
  [expression]
  (list 'let ['result expression]
        (list 'println 'result)
        'result))

(defn reccomend-what-to-wear
  "This function will recommend clothing based on conditions (to be implemented)."
  []
  :todo)

(defn my-function [] 3)

(defn first-element [sequence default]
  (if (nil? sequence)
    default
    (first sequence)))

;;procitati nes iz user story applied
;;naci na nekom sajtu slucajeve koriscenja 
;;user would like to get a reccomendation about what to wear //sta do gde??
;;moramo da imamo integracione testove, funkciponalne testove...
;;biblioteka midje koja se ubaci u lajningen i pomaze nam da pisemo testove 
;;test driven development 
;;za svoj domen uraditi 5 slucajeva koriscenja sa test driven stvarima 

;;moj domen:
;; 1. placing ships
;; 2. taking a turn
;; 3. playing with a friend
;; 4. playing with AI
;; 5. replay the game
;; 6. view stats from that and all previous games

(defn sleep-print-update
  [sleep-time thread-name update-fn]
  (fn [state]
    (Thread/sleep sleep-time)
    (println (str thread-name ": " state))
    (update-fn state)))
(def counter (ref 0))
(future (dosync (commute counter (sleep-print-update 100 "Thread A" inc))))
(future (dosync (commute counter (sleep-print-update 150 "Thread B" inc))))

;;kupujemprodajem
;; Kao korisnik, želim da mogu da pretražujem oglase po kategorijama kako bih lako pronašao proizvode koji me interesuju.
;; Kao prodavac, želim da mogu da postavim oglas sa slikama proizvoda kako bih privukao više potencijalnih kupaca.
;; Kao kupac, želim da mogu da filtriram oglase prema cenama i lokaciji kako bih našao proizvode u svom budžetu i bliže mom mestu.
;; Kao korisnik, želim da mogu da sačuvam oglase u listu favorita kako bih se kasnije vratio i razmotrio proizvode koji mi se dopadaju.
;; Kao korisnik, želim da mogu da šaljem privatne poruke prodavcima kako bih postavio dodatna pitanja pre nego što obavim kupovinu.