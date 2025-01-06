(ns tools-methods-project.handlers.specialist-chat-handler
  (:require [cheshire.core :as cheshire]
            [tools-methods-project.use-cases.specialist-chat :as chat]))

(defn specialist-chat-handler [request]
  (let [body        (slurp (:body request))
        parsed-body (cheshire/parse-string body true)
        {:keys [action sender receiver message]} parsed-body]
    (case action

      "start"
      (if (and sender receiver)
        (let [result (chat/start-chat-session sender receiver)]
          (case (:domain-status result)
            :success {:status 200
                      :headers {"Content-Type" "application/json"}
                      :body (cheshire/encode {:message (:message result)})}
            :error   {:status 400
                      :headers {"Content-Type" "application/json"}
                      :body (cheshire/encode {:error (:message result)})}

            {:status 500
             :headers {"Content-Type" "application/json"}
             :body (cheshire/encode {:error "Unknown domain status."})}))
        {:status 400
         :headers {"Content-Type" "application/json"}
         :body (cheshire/encode {:error "Sender and receiver are required to start a chat."})})

      "send"
      (if (and sender receiver message)
        (let [result (chat/send-message message sender receiver)]
          (case (:domain-status result)
            :success {:status 200
                      :headers {"Content-Type" "application/json"}
                      :body    (cheshire/encode {:message (:message result)})}
            :error   {:status 400
                      :headers {"Content-Type" "application/json"}
                      :body    (cheshire/encode {:error (:message result)})}))
        {:status 400
         :headers {"Content-Type" "application/json"}
         :body (cheshire/encode {:error "Sender, receiver, and message are required to send a message."})})

      "show-messages"
      (if (and sender receiver)
        (let [msgs (chat/show-messages sender receiver)]
          {:status 200
           :headers {"Content-Type" "application/json"}
           :body (cheshire/encode {:messages msgs})})
        {:status 400
         :headers {"Content-Type" "application/json"}
         :body (cheshire/encode {:error "Sender and receiver are required to show messages."})})

      {:status 400
       :headers {"Content-Type" "application/json"}
       :body (cheshire/encode {:error "Invalid action."})})))