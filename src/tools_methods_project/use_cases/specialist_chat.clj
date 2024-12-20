(ns tools-methods-project.use-cases.specialist-chat
  (:require [clojure.core.async
             :as a
             :refer [>! <! >!! <!! go chan buffer close! thread
                     alts! alts!! timeout]]
            [tools-methods-project.user :refer [sessions, login-patient]]))

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

(defn send-message [message sender receiver]
  (if (contains? @sessions receiver)
    (do
      (swap! messages update receiver conj {:message message :sender sender})
      {:status :success, :message "Message sent"})
    {:status :error, :message "Receiver is not online"}))

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

(def user-channels (atom {}))

(defn start-chat-session [sender-email receiver-email]
  (let [input-chan (chan 10)
        output-chan (chan 10)]
    (swap! user-channels assoc sender-email {:sender-channel input-chan :receiver-channel output-chan})
    (swap! user-channels assoc receiver-email {:sender-channel output-chan :receiver-channel input-chan})
    {:status :success, :message "Chat session started"}))

(defn end-chat-session [email]
  (when-let [{:keys [sender-channel receiver-channel]} (get @user-channels email)]
    (close! sender-channel)
    (close! receiver-channel)
    (swap! user-channels dissoc email)
    {:status :success, :message "Chat session ended"}))

(defn chat-participant [sender receiver]
  (let [input-chan (:sender-channel (get @user-channels sender))
        output-chan (:sender-channel (get @user-channels receiver))]
    (go
      (loop []
        (if-let [message (<! input-chan)]
          (do
            (println (str sender ": " message))
            (send-message message sender receiver)
            (<! (timeout (rand-int 1000)))
            (>! output-chan (random-message))
            (recur))
          (println (str sender " has left the chat. ")))))))


(defn simulate-chat [user1 user2]
  (let []
    (chat-participant user1 user2)
    (chat-participant user2 user1)

    (go
      (>! (:sender-channel (get @user-channels user1)) "Hi there!")
      (>! (:sender-channel (get @user-channels user2)) "Hello!")

      (<! (timeout 5000))
      (end-chat-session user1)
      (end-chat-session user2))))

;; Log in users
(login-patient "aaa@gmail.com" "aaa")
(login-patient "bbb@gmail.com" "bbb")

;; Start chat sessions
(start-chat-session "aaa@gmail.com" "bbb@gmail.com")

;; Simulate a chat
(simulate-chat "aaa@gmail.com" "bbb@gmail.com")

;; (println "Dunja's Messages: " (show-all-messages "Dunja"))
;; (println "Pera's Messages With Dunja: " (show-messages "Pera" "Dunja"))
