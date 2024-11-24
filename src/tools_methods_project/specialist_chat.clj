(ns tools-methods-project.specialist-chat
  (:require [clojure.core.async
             :as a
             :refer [>! <! >!! <!! go chan buffer close! thread
                     alts! alts!! timeout]]))

;; >! (send to a channel)
;; <! (receive from a channel)

(def messages (atom
               {"Dunja" [{:message "woo" :sender "Zika"}]
                "Pera" [{:message "hi" :sender "Mika"}]}))

(defn show-messages
  [person1 person2]
  (let [person1-messages (get @messages person1)]
    (filter #(= (:sender %) person2) person1-messages)))

(defn show-all-messages
  [person]
  (get @messages person))

(defn random-message []
  (rand-nth
   ["Hello there!" "How's it going?" "What are you up to?" "That's interesting!"
    "Tell me more!" "I'm just hanging out." "What do you think?" "Omg you are so boring!"
    "What’s new with you?" "Have you seen any good movies lately?" "I can't believe this weather!"
    "Do you like video games?" "Let’s do something fun!" "How about a game of chess?"
    "What’s your favorite food?" "How’s your day been so far?" "I’ve been busy, but good."
    "What's the most exciting thing that's happened to you this week?" "Do you want to grab a coffee sometime?"
    "What’s your opinion on the latest trend?" "How do you feel about the news today?" "What are you passionate about?"
    "I think we should plan a trip!" "What’s the best advice you’ve ever received?" "Do you like to read?"
    "Tell me your favorite joke!" "What’s your dream job?" "Do you believe in aliens?" "What’s your biggest goal right now?"]))

(defn send-message
  [message sender receiver]
  (swap! messages update receiver conj {:message message :sender sender}))

(defn chat-participant [sender input-chan receiver output-chan]
  (go
    (loop []
      (if-let [message (<! input-chan)]
        (do
          (println (str sender ": " message))
          ;; Sending the message to the receiver
          (send-message message sender receiver)
          ;; Responding after a random delay
          (<! (timeout (rand-int 1000)))
          (let [response (random-message)]
            (>! output-chan response))
          (recur))
        (println (str sender " has left the chat"))))))

(defn simulate-chat []
  (let [buffer-size 10
        chan1 (chan buffer-size)
        chan2 (chan buffer-size)]

    (chat-participant "Dunja" chan1 "Pera" chan2)
    (chat-participant "Pera" chan2 "Dunja" chan1)

    (go
      ;; Sending initial messages to start the chat
      (>! chan1 "Hi Pera!")
      (>! chan2 "Hi Dunja!")
      ;; Closing channels after a while to end chat simulation
      (<! (timeout 7000))
      (close! chan1)
      (close! chan2))))

(simulate-chat)

(println "Dunja's Messages: " (show-all-messages "Dunja"))
(println "Pera's Messages With Dunja: " (show-messages "Pera" "Dunja"))
