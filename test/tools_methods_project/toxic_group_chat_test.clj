(ns tools-methods-project.toxic-group-chat-test
  (:require [midje.sweet :refer :all]
            [tools-methods-project.toxic-group-chat :refer :all]
            [tools-methods-project.user :refer [sessions]]
            [clojure.core.async :as a]))

(fact "By registering user is able to have a group chat"
      (let [email "test@gmail.com"]
        (register-user-channel email) => truthy
        (contains? @user-channels email) => true))

(fact "User wants to have a group chat history"
      (let [email "user1@gmail.com"]
        (swap! sessions assoc email {:status :online})
        (register-user-channel email)
        (broadcast-message email "Hello, group!")
        (last @group-messages) => {:sender email :message "Hello, group!"}))

(fact "User wants to send the message to all other online users"
      (let [sender "user1@gmail.com"
            receiver "user2@gmail.com"]
        (swap! sessions assoc sender {:status :online})
        (swap! sessions assoc receiver {:status :online})
        (register-user-channel sender)
        (let [receiver-chan (register-user-channel receiver)]
          (broadcast-message sender "Hi, everyone!")
          (a/<!! receiver-chan) => "Hi, everyone!")))

(fact "Unauthorized users cannot broadcast messages"
      (let [email "unauthorized@gmail.com"]
        (broadcast-message email "This should not work") => nil
        (last @group-messages) =not=> {:sender email :message "This should not work"}))