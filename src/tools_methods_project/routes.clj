(ns tools-methods-project.routes
  (:require
   [ring.adapter.jetty :refer [run-jetty]]
   [tools-methods-project.handlers.personalized-treatment-handler :refer [personalized-treatment]]
   [tools-methods-project.handlers.symptom-checker-handler :refer [check-symptoms-handler]]))

(defn app [request]
  (case (:uri request)
    "/" {:status 200 :body "Welcome to the Tools&Methods Project!"}
    "/recommendations" (if (= (:request-method request) :post)
                         (personalized-treatment request)
                         {:status 405 :body "Method Not Allowed"})
    "/check-symptoms" (if (= (:request-method request) :post)
                        (check-symptoms-handler request)
                        {:status 405 :body "Method Not Allowed"})
    {:status 404 :body "Not Found"}))

(defn -main []
  (println "Starting server on http://localhost:3000")
  (run-jetty app {:port 3000 :join? false}))