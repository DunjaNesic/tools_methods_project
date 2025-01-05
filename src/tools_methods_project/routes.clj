(ns tools-methods-project.routes
  (:require
   [ring.adapter.jetty :refer [run-jetty]]
   [ring.util.response :refer [header response]]
   [tools-methods-project.handlers.personalized-treatment-handler :refer [personalized-treatment]]
   [tools-methods-project.handlers.symptom-checker-handler :refer [check-symptoms-handler]]
   [tools-methods-project.handlers.group-chat-handler :refer [group-chat-handler]]
   [tools-methods-project.handlers.chatbot-handler :refer [chatbot-handler]]
   [tools-methods-project.handlers.specialist-handler :refer [specialist-handler]]
   [tools-methods-project.handlers.specialist-chat-handler :refer [specialist-chat-handler]]))

(defn cors-response [response]
  (-> response
      (header "Access-Control-Allow-Origin" "http://localhost:8280")
      (header "Access-Control-Allow-Methods" "GET, POST, OPTIONS")
      (header "Access-Control-Allow-Headers" "Content-Type, Authorization")
      (header "Access-Control-Allow-Credentials" "true")))

(defn cors-middleware [handler]
  (fn [request]
    (if (= (:request-method request) :options)
      (cors-response (response ""))
      (cors-response (handler request)))))

(defn app [request]
  (case (:uri request)
    "/" {:status 200 :body "Welcome to the Tools&Methods Project!"}
    "/recommendations" (if (= (:request-method request) :post)
                         (personalized-treatment request)
                         {:status 405 :body "Method Not Allowed"})
    "/check-symptoms" (if (= (:request-method request) :post)
                        (check-symptoms-handler request)
                        {:status 405 :body "Method Not Allowed"})
    "/chat" (if (= (:request-method request) :post)
              (specialist-chat-handler request)
              {:status 405 :body "Method Not Allowed"})
    "/group-chat" (if (= (:request-method request) :post)
                    (group-chat-handler request)
                    {:status 405 :body "Method Not Allowed"})
    "/chatbot" (if (= (:request-method request) :post)
                 (chatbot-handler request)
                 {:status 405 :body "Method Not Allowed"})
    "/specialist" (if (= (:request-method request) :get)
                    (specialist-handler request)
                    {:status 405 :body "Method Not Allowed"})
    {:status 404 :body "Not Found"}))

(defn -main []
  (println "Starting server on http://localhost:3000")
  (run-jetty (cors-middleware app) {:port 3000 :join? false})) 
