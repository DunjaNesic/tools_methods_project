(ns tools-methods-project.handlers.group-chat-handler
  (:require [cheshire.core :as cheshire]
            [tools-methods-project.use-cases.toxic-group-chat :as chat]))

(defn group-chat-handler [request]
  (let [body        (slurp (:body request))
        parsed-body (cheshire/parse-string body true)
        {:keys [action user-email message]} parsed-body]
    (case action
      "join"
      (if user-email
        (let [result (chat/join-group user-email)]
          (case (:domain-status result)
            :success {:status 200
                      :headers {"Content-Type" "application/json"}
                      :body (cheshire/encode {:message (:message result)})}
            :error   {:status 400
                      :headers {"Content-Type" "application/json"}
                      :body (cheshire/encode {:error (:message result)})}))
        {:status 400
         :headers {"Content-Type" "application/json"}
         :body (cheshire/encode {:error "Missing user-email to join group chat."})})

      "leave"
      (if user-email
        (let [result (chat/leave-group user-email)]
          (case (:domain-status result)
            :success {:status 200
                      :headers {"Content-Type" "application/json"}
                      :body (cheshire/encode {:message (:message result)})}
            :error   {:status 400
                      :headers {"Content-Type" "application/json"}
                      :body (cheshire/encode {:error (:message result)})}))
        {:status 400
         :headers {"Content-Type" "application/json"}
         :body (cheshire/encode {:error "Missing user-email to leave group chat."})})

      "send"
      (if (and user-email message)
        (let [result (chat/broadcast-message user-email message)]
          (case (:domain-status result)
            :success {:status 200
                      :headers {"Content-Type" "application/json"}
                      :body (cheshire/encode {:message (:message result)})}
            :error   {:status 400
                      :headers {"Content-Type" "application/json"}
                      :body (cheshire/encode {:error (:message result)})}))
        {:status 400
         :headers {"Content-Type" "application/json"}
         :body (cheshire/encode {:error "Need user-email and message to send group message."})})

      "show-group"
      (let [msgs (chat/show-group-messages)]
        {:status 200
         :headers {"Content-Type" "application/json"}
         :body (cheshire/encode {:messages msgs})})

      {:status 400
       :headers {"Content-Type" "application/json"}
       :body (cheshire/encode {:error (str "Invalid group-chat action: " action)})})))
