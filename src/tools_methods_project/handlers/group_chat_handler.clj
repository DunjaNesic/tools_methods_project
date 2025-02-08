(ns tools-methods-project.handlers.group-chat-handler
  (:require [cheshire.core :as cheshire]
            [tools-methods-project.use-cases.toxic-group-chat :as chat]
            [tools-methods-project.helpers.response :refer [json-response, handle-domain-result]]))

(defn group-chat-handler [request]
  (let [body        (slurp (:body request))
        parsed-body (cheshire/parse-string body true)
        {:keys [action user-email message]} parsed-body]
    (case action
      "join"
      (if user-email
        (handle-domain-result (chat/join-group user-email))
        (json-response 400 {:error "Missing user-email to join group chat."}))

      "leave"
      (if user-email
        (handle-domain-result (chat/leave-group user-email))
        (json-response 400 {:error "Missing user-email to leave group chat."}))

      "send"
      (if (and user-email message)
        (handle-domain-result (chat/broadcast-message user-email message))
        (json-response 400 {:error "Need user-email and message to send group message."}))

      "show-group"
      (json-response 200 {:messages (chat/show-group-messages)})
      (json-response 400 {:error (str "Invalid group-chat action: " action)}))))