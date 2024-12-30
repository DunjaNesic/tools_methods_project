(ns tools-methods-project.use-cases.toxic-group-chat
  (:require [clojure.core.async :as a]
            [tools-methods-project.user :refer [sessions]])
  (:import  (java.time Instant ZoneId ZonedDateTime)
            (java.time.format DateTimeFormatter)))

(def user-channels (atom {}))
(def group-messages (atom []))


(defn formatted-now []
  (let [formatter (DateTimeFormatter/ofPattern "yyyy-MM-dd HH:mm:ss")
        now       (Instant/now)
        zdt       (ZonedDateTime/ofInstant now (ZoneId/systemDefault))]
    (.format zdt formatter)))

(defn create-user-channel []
  (a/chan 10))

(defn user-listen
  [user-email user-chan]
  (a/go-loop []
    (when-let [message (a/<! user-chan)]
      (println (str "User " user-email " received message: " message))
      (recur))))

(defn join-group
  [user-email]
  (if (contains? @sessions user-email)
    (do
      (if (not (contains? @user-channels user-email))
        (let [ch (create-user-channel)]
          (swap! user-channels assoc user-email ch)
          (user-listen user-email ch)))
      {:domain-status :success
       :message (str user-email " joined group chat!")})
    {:domain-status :error
     :message (str "User " user-email " not found in sessions or offline")}))

(defn leave-group
  [user-email]
  (if-let [ch (get @user-channels user-email)]
    (do
      (a/close! ch)
      (swap! user-channels dissoc user-email)
      {:domain-status :success
       :message (str "User " user-email " left group chat!")})
    {:domain-status :error
     :message (str "User " user-email " is not in the group chat.")}))

(defn save-group-message
  "Append the message to group-messages atom with a timestamp"
  [sender message]
  (swap! group-messages conj
         {:sender sender
          :message message
          :timestamp (System/currentTimeMillis)
          :timestamp-str (formatted-now)}))

(defn broadcast-message
  [user-email message]
  (if (contains? @sessions user-email)
    (do
      (save-group-message user-email message)
      (doseq [[email {:keys [status]}] @sessions]
        (when (and (= status :online)
                   (not= email user-email))
          (when-let [ch (get @user-channels email)]
            (a/put! ch (str user-email " says: " message)))))
      {:domain-status :success
       :message (str "Broadcasted message from " user-email)})
    {:domain-status :error
     :message (str "Unauthorized user " user-email " tried to send a message!")}))

(defn show-group-messages
  "Returns the entire group chat history from the atom"
  []
  @group-messages)
