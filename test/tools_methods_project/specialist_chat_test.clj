(ns tools-methods-project.specialist-chat-test
  (:require [tools-methods-project.specialist-chat :refer :all]
            [midje.sweet :refer :all]))


(facts "User wants to send message"
       (fact "User sends the message"
             (send-message "hi" "dunja" "pjerkan" {"pjerkan" [{:message "hi" :receiver "dunja"}]}) =not=> nil
             (send-message "hi" "dunja" "pjerkan" nil) => {"pjerkan" [{:message "hi" :sender "dunja"}]}
             (send-message "woo" "dunja" "pjerkan" {"pjerkan" [{:message "hi" :sender "dunja"}]}) => {"pjerkan" [{:message "hi" :sender "dunja"} {:message "woo" :sender "dunja"}]}
             (send-message "woo" "dunja" "pjerkan" {"pjerkan" [{:message "hi" :sender "dunja"}]
                                                    "zika" [{:message "hi" :sender "mika"}]}) => {"pjerkan" [{:message "hi" :sender "dunja"} {:message "woo" :sender "dunja"}] "zika" [{:message "hi" :sender "mika"}]}))