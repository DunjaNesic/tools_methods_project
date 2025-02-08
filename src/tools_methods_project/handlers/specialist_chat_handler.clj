(ns tools-methods-project.handlers.specialist-chat-handler
  (:require [cheshire.core :as cheshire]
            [tools-methods-project.use-cases.payment-to-specialist :as payment]
            [tools-methods-project.use-cases.specialist-chat :as chat]
            [tools-methods-project.helpers.response :refer [json-response handle-domain-result]]))

(defn specialist-chat-handler [request]
  (let [body        (slurp (:body request))
        parsed-body (cheshire/parse-string body true)
        {:keys [action sender receiver message last-checked-timestamp]} parsed-body]
    (case action

      "start"
      (if (and sender receiver)
        (handle-domain-result (chat/start-chat-session sender receiver))
        (json-response 400 {:error "Sender and receiver are required to start a chat."}))

      "send"
      (if (and sender receiver message)
        (handle-domain-result (chat/send-message message sender receiver))
        (json-response 400 {:error "Sender, receiver, and message are required to send a message."}))

      "show-messages"
      (if (and sender receiver)
        (json-response 200 {:messages (chat/show-messages sender receiver)})
        (json-response 400 {:error "Sender and receiver are required to show messages."}))

      "fetch-new-messages"
      (if (and receiver last-checked-timestamp)
        (let [last-checked-timestamp (Long/parseLong last-checked-timestamp)
              msgs                   (chat/fetch-new-messages receiver last-checked-timestamp)]
          (json-response 200 {:messages msgs}))
        (json-response 400 {:error "Receiver and last-checked-timestamp are required to fetch new messages."}))

      "start-charging"
      (if (and sender receiver)
        (handle-domain-result (payment/start-paid-chat sender receiver))
        (json-response 400 {:error "Sender and receiver are required to start charging."}))

      "stop-charging"
      (if (and sender receiver)
        (let [result (payment/end-paid-chat sender receiver)]
          (if (= (:domain-status result) :success)
            (json-response 200 {:message (:message result) :cost (:cost result)})
            (json-response 400 {:error (:message result)})))
        (json-response 400 {:error "Sender and receiver are required to stop charging."}))

      (json-response 400 {:error "Invalid action."}))))
