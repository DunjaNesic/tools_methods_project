(ns tools-methods-project.core
  (:require
   [clojure.math :as math]
   [clojure.string :as string]
   [criterium.core :refer [quick-bench]]))

;;Korisnik zeli da na osnovu svojih simptoma dobije moguce dijagnoze DONE
;;Korisnik zeli da na osnovu svojih simptoma dobije savet kojim bi sve specijalistima trebalo da se obrati DONE
;;Korisnik zeli da dobije konkretne lekare specijaliste poredjane po nekom prioritetu (grad/cena) DONE
;;Korisnik zeli mogucnost četovanja sa izabranim lekarom DONE
;;Korisnik zeli da mu bude pruzena pomoc u vidu chatbota DONE
;;Korisnik zeli da dobije verovatnoce za postavljene dijagnoze DONE
;;Korisnik zeli da dobije personalizovanu terapiju na osnovu genetičkih predispozicija, stila zivota i oboljenja koja su mu dosad dijagnostifikovana DONE
;;Korisnik zeli da prati istoriju svojih simptoma tokom vremena DONE

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

(defn sleep-print-update
  [sleep-time thread-name update-fn]
  (fn [state]
    (Thread/sleep sleep-time)
    (println (str thread-name ": " state))
    (update-fn state)))
(def counter (ref 0))
(future (dosync (commute counter (sleep-print-update 100 "Thread A" inc))))
(future (dosync (commute counter (sleep-print-update 150 "Thread B" inc))))

(def ponderi {:sareno -1
              :svetlo 0
              :tamno  1})

(defn classify-color
  "stavljamo nase boje u neke kategorije"
  [color]
  (case color
    :crvena :sareno
    :plava  :tamno
    :zelena :sareno
    :bela   :svetlo
    :crna   :tamno
    :zuta   :svetlo))

(defn evaluate-outfit [clothing-items]
  (let [categories (map classify-color clothing-items)
        score (reduce + (map ponderi categories))]
    (cond
      (> score 0) "Kombinacija je dobra!"
      (< score 0) "Kombinacija je loša!"
      :else "Kombinacija je neutralna.")))

(def possible-bottoms
  {"t-shirt" ["jeans" "shorts" "sweatpants"]
   "sweater" ["pants" "sweatpants" "skirt"]
   "shirt" ["jeans" "pants" "shorts"]})

(defn random-element [collection]
  (nth collection (rand-int (count collection))))

(defn suggest-bottom
  [top]
  (if-let [bottoms (possible-bottoms top)]
    (str "We suggest: " (random-element bottoms))
    "We don't have a suggestion for this top."))

(println (suggest-bottom "t-shirt"))
(println (suggest-bottom "sweater"))
(println (suggest-bottom "jacket"))

;; Predefined RGB values for colors
(def color-rgb-map
  {"red" [255 0 0]
   "blue" [0 0 255]
   "green" [0 255 0]
   "yellow" [255 255 0]
   "black" [0 0 0]
   "white" [255 255 255]
   "orange" [255 165 0]
   "purple" [128 0 128]})

;; Euclidean distance in RGB space
(defn rgb-distance [rgb1 rgb2]
  (math/sqrt (reduce + (map #(Math/pow (- %1 %2) 2) rgb1 rgb2))))

;; Normalizing coefficient to a 0-1 scale
(defn match-coefficient [distance max-distance]
  (- 1 (/ distance max-distance)))

;; If coef is small then the colors are different, if its big or close to 1 then they are identical
(defn color-coefficient
  [color1 color2]
  (let [rgb1 (color-rgb-map color1)
        rgb2 (color-rgb-map color2)
        max-distance (rgb-distance [0 0 0] [255 255 255])
        distance (rgb-distance rgb1 rgb2)]
    (match-coefficient distance max-distance)))

;; Sa CIEDE2000 formulom bi bilo vise accurate, ali ne bih znala kako da iskucam to

(println (color-coefficient "red" "blue"))
(println (color-coefficient "red" "yellow"))
(println (color-coefficient "red" "red"))

(defn group-by-type [items]
  (vals (group-by type items)))

(defn group-by-type2 [items]
  (vals
   (reduce
    (fn [acc e]
      (let [item-type (type e)]
        (update acc item-type #(conj (or % []) e))))
    {}
    items)))

(def s
  (take 100000 (repeatedly #(rand-int 2000))))

(count s)

(time (group-by-type s))


(def a
  (into-array s))

(time (group-by-type a))

(def f1
  (fn [col]
    (loop [typeset (set (map type col)) result []]
      (if (empty? typeset)
        result
        (recur (rest typeset) (conj result (filter #(= (type %) (first typeset)) col)))))))

(def f2
  (fn [c]
    (vals (group-by class c))))

(def f3
  (fn [xs]
    (set (map (fn [t] (filter #(= (type %) t) xs)) (distinct (map type xs))))))

(def f4
  (fn [l]
    (map reverse
         (vals
          (loop [l (apply list l) nl {}]
            (if-not (seq l)
              nl
              (recur (pop l) (assoc nl (type (first l)) (conj (nl (type (first l))) (first l))))))))))

(def f5
  (fn [coll]
    (loop [c coll m {}]
      (if (empty? c) (vals m)
          (recur (rest c)
                 (let [v (first c) t (type v)]
                   (assoc m t
                          (conj (get m t []) v))))))))

(def f6
  (fn [coll]
    (let [types (set (map type coll))]
      (map (fn [t] (filter #(= t (type %)) coll))
           types))))

(def f7
  (fn [s]
    (vals (reduce #(assoc %1 (type %2) (conj (get %1 (type %2) []) %2)) {} s))))

(def f8
  #(loop [xs % result {}]
     (let [f (first xs) t (type f)]
       (if (empty? xs)
         (map reverse (vals result))
         (recur (rest xs) (assoc result t (conj (result t) f)))))))

(def sample-data [1 2 3 "hello" "world" :keyword1 :keyword2 true false 4.5 6.7 nil [1 2] '(3 4)])

;; (println "Benchmarking f1:")
;; (quick-bench (f1 sample-data))

;; (println "Benchmarking f2:")
;; (quick-bench (f2 sample-data))

;; (println "Benchmarking f3:")
;; (quick-bench (f3 sample-data))

;; (println "Benchmarking f4:")
;; (quick-bench (f4 sample-data))

;; (println "Benchmarking f5:")
;; (quick-bench (f5 sample-data))

;; (println "Benchmarking f6:")
;; (quick-bench (f6 sample-data))

;; (println "Benchmarking f7:")
;; (quick-bench (f7 sample-data))

;; (println "Benchmarking f8:")
;; (quick-bench (f8 sample-data))

;; domaci: isprobati clojure goes fast projektice i ubaciti u nas projekat ukoliko je pogodno