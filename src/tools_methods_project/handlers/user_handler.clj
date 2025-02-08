(ns tools-methods-project.handlers.user-handler
  (:require [cheshire.core :as cheshire]
            [tools-methods-project.helpers.response :refer [json-response]]
            [tools-methods-project.user :refer [login-user register-user logout-user]]))

(defn handle-login [request]
  (let [body (slurp (:body request))
        parsed-body (cheshire/parse-string body true)
        email (:email parsed-body)
        password (:password parsed-body)
        login-response (login-user email password)]
    (if (= (:status login-response) :success)
      (json-response 200 {:status "success"
                          :message (:message login-response)
                          :user (select-keys (:user login-response) [:userr/email :userr/id :userr/user_type])})
      (json-response 401 {:status "error"
                          :message (:message login-response)}))))

(defn handle-register [request]
  (try
    (let [body (slurp (:body request))
          {:keys [email password name user_type specialty]} (cheshire/parse-string body true)]
      (if (and email password name user_type
               (or (not= user_type "specialist") specialty))
        (let [result (register-user email password name user_type specialty)]
          (json-response (if (= (:status result) :success) 200 400) result))
        (json-response 400 {:status "error"
                            :message "Invalid input. Missing required fields."})))
    (catch Exception e
      (json-response 500 {:status "error"
                          :message (.getMessage e)}))))

(defn handle-logout [request]
  (let [body (slurp (:body request))
        {:keys [email]} (cheshire/parse-string body true)]
    (if email
      (let [result (logout-user email)]
        (json-response 200 result))
      (json-response 400 {:status "error"
                          :message "Email is required"}))))
