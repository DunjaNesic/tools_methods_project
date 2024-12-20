(ns tools-methods-project.routes
  (:require
   [ring.adapter.jetty :refer [run-jetty]]
   [tools-methods-project.handlers.personalized-treatment-handler :refer [personalized-treatment]]))

(defn app [request]
  (case (:uri request)
    "/" {:status 200 :body "Welcome to the Tools&Methods Project!"}
    "/recommendations" (if (= (:request-method request) :post)
                         (personalized-treatment request)
                         {:status 405 :body "Method Not Allowed"})))
(defn -main []
  (println "Starting server on http://localhost:3000")
  (run-jetty app {:port 3000 :join? false}))