(ns tools-methods-project.use-cases.specialist-chat
  (:require [clojure.core.async :as a :refer [>! <! go chan close!]]
            [tools-methods-project.user :refer [sessions login-user]])
  (:import (java.time Instant ZoneId ZonedDateTime)
           (java.time.format DateTimeFormatter)))

(def messages (atom {}))

(def user-channels (atom {}))

(defn formatted-now []
  (let [formatter (DateTimeFormatter/ofPattern "yyyy-MM-dd HH:mm:ss")
        now       (Instant/now)
        zdt       (ZonedDateTime/ofInstant now (ZoneId/systemDefault))]
    (.format zdt formatter)))

(defn send-message [message sender receiver]
  (if (contains? @sessions receiver)
    (do
      (swap! messages update
             receiver
             conj
             {:message      message
              :sender       sender
              :timestamp    (System/currentTimeMillis)
              :timestamp-str (formatted-now)})
      (go
        (>! (:sender-channel (get @user-channels receiver)) message))
      {:domain-status :success :message "Message sent"})
    {:domain-status :error :message "Receiver is not online"}))

(defn start-chat-session [sender-email receiver-email]
  (let [input-chan (chan 10)
        output-chan (chan 10)]
    (swap! user-channels assoc sender-email
           {:sender-channel    output-chan
            :receiver-channel  input-chan})
    (swap! user-channels assoc receiver-email
           {:sender-channel    input-chan
            :receiver-channel  output-chan})
    {:domain-status :success
     :message       "Chat session started"}))

(defn end-chat-session [email]
  (when-let [{:keys [sender-channel receiver-channel]}
             (get @user-channels email)]
    (close! sender-channel)
    (close! receiver-channel)
    (swap! user-channels dissoc email)
    {:domain-status :success
     :message "Chat session ended"}))

(defn show-all-messages
  [person]
  (get @messages person))

(defn show-messages
  [person1 person2]
  (let [msgs-person1 (get @messages person1 [])
        msgs-person2 (get @messages person2 [])

        msgs-person2->person1 (filter #(= (:sender %) person2) msgs-person1)
        msgs-person1->person2 (filter #(= (:sender %) person1) msgs-person2)]

    (sort-by :timestamp (concat msgs-person2->person1 msgs-person1->person2))))


(defn chat-participant
  "nisam bas sig dal ce ovo da mi treba al aj"
  [sender receiver]
  (let [input-chan  (:sender-channel (get @user-channels sender))
        output-chan (:sender-channel (get @user-channels receiver))]
    (go
      (loop []
        (if-let [message (<! input-chan)]
          (do
            (println (str sender ": " message))
            (recur))
          (println (str sender " has left the chat.")))))))

(defn simulate-chat [user1 user2]
  (start-chat-session user1 user2)
  (start-chat-session user2 user1)

  (chat-participant user1 user2)
  (chat-participant user2 user1)

  (send-message "Hi there!" user1 user2)
  (send-message "Hello!" user2 user1))

(login-user "aaa@gmail.com" "aaa")
(login-user "bbb@gmail.com" "bbb")

;; (simulate-chat "aaa@gmail.com" "bbb@gmail.com")

;; (send-message "How are you?" "aaa@gmail.com" "bbb@gmail.com")
;; (send-message "I'm doing well!" "bbb@gmail.com" "aaa@gmail.com")

;; (println "==== show-all-messages za aaa@gmail.com ====")
;; (println (show-all-messages "aaa@gmail.com"))

;; (println "==== show-all-messages za bbb@gmail.com ====")
;; (println (show-all-messages "bbb@gmail.com"))

;; (println "==== show-messages (ceo chat) izmedju 'aaa' i 'bbb' ====")
;; (doseq [mess (show-messages "aaa@gmail.com" "bbb@gmail.com")]
;;   (println (str (:sender mess) " -> " (:message mess))))
