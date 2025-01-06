(ns tools-methods-project.handlers.login-handler
  (:require [cheshire.core :as cheshire]
            [tools-methods-project.user :refer [login-patient]]))

(defn handle-login [request]
  (let [body (slurp (:body request))
        parsed-body (cheshire/parse-string body true)
        email (:email parsed-body)
        password (:password parsed-body)
        login-response (login-patient email password)]
    (if (= (:status login-response) :success)
      {:status 200
       :headers {"Content-Type" "application/json"}
       :body (cheshire/generate-string {:status "success"
                                        :message (:message login-response)
                                        :user (select-keys (:patient login-response) [:patient/email])})}
      {:status 401
       :headers {"Content-Type" "application/json"}
       :body (cheshire/generate-string {:status "error"
                                        :message (:message login-response)})})))