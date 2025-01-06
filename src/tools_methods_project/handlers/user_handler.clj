(ns tools-methods-project.handlers.user-handler
  (:require [cheshire.core :as cheshire]
            [tools-methods-project.user :refer [login-patient register-patient logout-patient]]))

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

(defn handle-register [request]
  (let [body (slurp (:body request))
        {:keys [email password]} (cheshire/parse-string body true)]
    (if (and email password)
      (let [result (register-patient email password)]
        {:status 200
         :headers {"Content-Type" "application/json"}
         :body (cheshire/generate-string result)})
      {:status 400
       :headers {"Content-Type" "application/json"}
       :body (cheshire/generate-string {:status
                                        :error
                                        :message "Invalid input"})})))

(defn handle-logout [request]
  (let [body (slurp (:body request))
        {:keys [email]} (cheshire/parse-string body true)]
    (if email
      (let [result (logout-patient email)]
        {:status 200
         :headers {"Content-Type" "application/json"}
         :body (cheshire/generate-string result)})
      {:status 400
       :headers {"Content-Type" "application/json"}
       :body (cheshire/generate-string {:status
                                        :error
                                        :message "Email is required"})})))