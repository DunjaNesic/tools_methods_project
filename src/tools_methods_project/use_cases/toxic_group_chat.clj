(ns tools-methods-project.use-cases.toxic-group-chat
  (:require [clojure.core.async :as a]
            [tools-methods-project.user :refer [sessions]]))

(defn create-user-channel []
  (a/chan))

(def user-channels (atom {}))

(def group-messages (atom []))

(defn register-user-channel [user-email]
  (let [user-chan (create-user-channel)]
    (swap! user-channels assoc user-email user-chan)
    user-chan))

(defn save-message [sender message]
  "Saving a message to the group chat history"
  (swap! group-messages conj {:sender sender :message message}))

(defn broadcast-message [user-email message]
  (if (contains? @sessions user-email)
    (do
      (save-message user-email message)
      (doseq [[email {:keys [status]}] @sessions]
        (when (and (= status :online)
                   (not= email user-email))
          (let [user-chan (get @user-channels email)]
            (when user-chan
              (a/put! user-chan message))))))
    (println (str "Unauthorized user " user-email " attempted to send a message!"))))

(defn user-listen [user-email]
  (let [user-chan (or (get @user-channels user-email)
                      (register-user-channel user-email))]
    (a/go-loop []
      (when-let [message (a/<! user-chan)]
        (println (str "User " user-email " received message: " message))
        (recur)))))

(doseq [[email {:keys [status]}] @sessions]
  (when (= status :online)
    (user-listen email)))

(defn show-group-messages []
  @group-messages)

(broadcast-message "aaa@gmail.com" "Hello from aaa@gmail.com")
(broadcast-message "sandra@gmail.com" "Hello from sandra@gmail.com")

(println "Group Chat Messages:" (show-group-messages))
