(ns tools-methods-project.specialist-chat)

(def messages (atom
               {"pjerkan" [{:message "hi" :sender "dunja"}]
                "zika" [{:message "hi" :sender "mika"}]}))

(defn send-message
  [message sender receiver]
  (swap! messages update receiver conj {:message message :sender sender}))
;;pokusala sam da dodam timestamp ali ne znam kako da proverim to u test klasi

(defn show-messages
  [person1 person2]
  (let [person1-messages (get @messages person1)]
    (filter #(= (:sender %) person2) person1-messages)))

(defn show-all-messages
  [person]
  (get @messages person))