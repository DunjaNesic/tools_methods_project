(ns tools-methods-project.handlers.chatbot-handler
  (:require [cheshire.core :as cheshire]
            [tools-methods-project.helpers.response :refer [json-response]]
            [tools-methods-project.helpers.json-data :refer [faqs]]
            [tools-methods-project.use-cases.healthcare-chatbot :as chatbot]))

(defn chatbot-handler [request]
  (try
    (let [body       (slurp (:body request))
          input-data (cheshire/parse-string body true)
          question   (:question input-data)
          answer     (chatbot/find-answer-nlp faqs question)]
      (json-response 200 {:answer answer}))
    (catch Exception e
      (json-response 400 {:error   "Invalid request"
                          :details (.getMessage e)}))))