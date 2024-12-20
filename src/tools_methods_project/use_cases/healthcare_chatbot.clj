(ns tools-methods-project.use-cases.healthcare-chatbot)

;;until I integrate app with database
(def faq-db
  {"What is flu?" "Flu is a viral infection that attacks your respiratory system."
   "How do I treat a headache?" "For mild headaches, rest, hydration, and over-the-counter pain relievers can help."
   "What are the symptoms of COVID-19?" "Common symptoms include fever, cough, and difficulty breathing."
   "What medications are safe during pregnancy?" "Consult a doctor for personalized advice, but avoid self-medicating."})

(defn answer-faq
  "Retrieves an answer from the FAQ database"
  [question]
  (get faq-db question "I'm sorry, I don't have an answer to that question."))
