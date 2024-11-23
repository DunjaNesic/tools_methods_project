(ns tools-methods-project.specialist-chat-test
  (:require [tools-methods-project.specialist-chat :refer :all]
            [midje.sweet :refer :all]))


(fact "User wants to send the message"
      (reset! messages {"pjerkan" [{:message "hi" :sender "dunja"}]})
      (send-message "woo" "dunja" "pjerkan")
      @messages => {"pjerkan" [{:message "hi" :sender "dunja"}
                               {:message "woo" :sender "dunja"}]}

      (send-message "hello" "mika" "zika")
      @messages => {"pjerkan" [{:message "hi" :sender "dunja"}
                               {:message "woo" :sender "dunja"}]
                    "zika" [{:message "hello" :sender "mika"}]})


(fact "User wants to view messages from a specific sender"
      (reset! messages {"pjerkan" [{:message "hi" :sender "dunja"}
                                   {:message "woo" :sender "dunja"}]
                        "zika" [{:message "hi" :sender "mika"}]})

      (show-messages "pjerkan" "dunja") => [{:message "hi" :sender "dunja"}
                                            {:message "woo" :sender "dunja"}]

      (show-messages "zika" "mika") => [{:message "hi" :sender "mika"}]

      (show-messages "pjerkan" "mika") => [])

(fact "User wants to view messages with everybody"
      (reset! messages {"pjerkan" [{:message "hi" :sender "dunja"}
                                   {:message "woo" :sender "dunja"}]
                        "zika" [{:message "hi" :sender "mika"}]})
      (show-all-messages "pjerkan") => [{:message "hi" :sender "dunja"}
                                        {:message "woo" :sender "dunja"}]
      (show-all-messages "") => nil)
