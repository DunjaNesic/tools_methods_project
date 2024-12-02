(ns tools-methods-project.routes
  (:require
   [tools-methods-project.personalized-treatment-handler :refer [personalized-treatment]]))

(defn app [request]
  (case (:uri request)
    "/" {:status 200 :body "Welcome to the Tools&Methods Project!"}
    "/recommendations" (if (= (:request-method request) :post)
                         (personalized-treatment request)
                         {:status 405 :body "Method Not Allowed"})))
