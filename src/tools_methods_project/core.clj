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
    (println (string/join (repeat (count row) "- - ")))
    ))

;; (def board (create-board 5))
;; (print-board board)
