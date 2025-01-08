(ns tools-methods-project.handlers.user-handler
  (:require [cheshire.core :as cheshire]
            [tools-methods-project.user :refer [login-user register-user logout-user]]))

(defn handle-login [request]
  (let [body (slurp (:body request))
        parsed-body (cheshire/parse-string body true)
        email (:email parsed-body)
        password (:password parsed-body)
        login-response (login-user email password)]
    (if (= (:status login-response) :success)
      {:status 200
       :headers {"Content-Type" "application/json"}
       :body (cheshire/generate-string {:status "success"
                                        :message (:message login-response)
                                        :user (select-keys (:user login-response) [:userr/email :userr/id :userr/user_type])})}
      {:status 401
       :headers {"Content-Type" "application/json"}
       :body (cheshire/generate-string {:status "error"
                                        :message (:message login-response)})})))

(defn handle-register [request]
  (try
    (let [body (slurp (:body request))
          {:keys [email password name user_type specialty]} (cheshire/parse-string body true)]
      (if (and email password name user_type
               (or (not= user_type "specialist") specialty))
        (let [result (register-user email password name user_type specialty)]
          (if (= (:status result) :success)
            {:status 200
             :headers {"Content-Type" "application/json"}
             :body (cheshire/generate-string result)}
            {:status 400
             :headers {"Content-Type" "application/json"}
             :body (cheshire/generate-string result)}))
        {:status 400
         :headers {"Content-Type" "application/json"}
         :body (cheshire/generate-string {:status "error"
                                          :message "Invalid input. Missing required fields."})}))
    (catch Exception e
      {:status 500
       :headers {"Content-Type" "application/json"}
       :body (cheshire/generate-string {:status "error"
                                        :message (.getMessage e)})})))

(defn handle-logout [request]
  (let [body (slurp (:body request))
        {:keys [email]} (cheshire/parse-string body true)]
    (if email
      (let [result (logout-user email)]
        {:status 200
         :headers {"Content-Type" "application/json"}
         :body (cheshire/generate-string result)})
      {:status 400
       :headers {"Content-Type" "application/json"}
       :body (cheshire/generate-string {:status
                                        :error
                                        :message "Email is required"})})))

