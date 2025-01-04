(ns tools-methods-project.handlers.chatbot-handler
  (:require [cheshire.core :as cheshire]
            [tools-methods-project.helpers.json-data :refer [faqs]]
            [tools-methods-project.use-cases.healthcare-chatbot :as chatbot]))

(defn chatbot-handler [request]
  (try
    (let [body (slurp (:body request))
          input-data (cheshire/parse-string body true)
          question (:question input-data)
          answer (chatbot/find-answer-nlp faqs question)]
      {:status 200
       :headers {"Content-Type" "application/json"}
       :body (cheshire/generate-string {:answer answer})})
    (catch Exception e
      {:status 400
       :headers {"Content-Type" "application/json"}
       :body (cheshire/generate-string {:error "Invalid request"
                                        :details (.getMessage e)})})))