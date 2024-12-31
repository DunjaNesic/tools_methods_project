# tools_methods_project

A Clojure project designed for symptom checking, diagnosing health conditions, and recommending specialists. This project provides tools to analyze patient symptoms, map them to possible diagnoses, and suggest appropriate medical specialists for further care

## User stories

- User wants to receive possible diagnoses based on their symptoms.
- User wants to receive advice on which specialists they should consult based on their symptoms.
- User wants to see a list of specific specialist doctors ranked by certain priorities (e.g., location/price).
- User wants the ability to chat with a chosen doctor.
- User wants assistance in the form of a chatbot.
- User wants to receive probabilities for the suggested diagnoses.
- User wants to receive personalized treatment recommendations based on genetic predispositions, lifestyle, and previously diagnosed conditions.
- User wants to track the history of their symptoms over time.
- User wants to chat with other people that have similar diagnoses.

## My experience doing this project

At first, I had no idea what to do for my project and I was constantly changing the subject I wanted to work on. I was going back and forth between a game of sinking ships and an application for predicting outcomes in horse racing. I made my mind after I read User stories applied by Mike Cohn.

My main problem when choosing a project topic was that I was looking at everything too broadly. I was thinking about what I needed to implement rather than what the user would want to do in my application. After reading Mike's book, I realized that no user opens an application just to log in, for example. They open it to accomplish a specific task they have in mind or to solve a problem they’re facing, and logging in is just something that happens before that. Once I adopted this mindset of thinking, "What do I, as a user, want to do in the application?" it became much easier for me to come up with a topic. In a much more natural way, this led to the idea of creating a healthcare-related application.

I put myself in the user's position, and as someone who enjoys scaring themselves by typing their symptoms into Google and then reading pessimistic diagnoses from "Dr. Google", I assumed there must be plenty of people like me. With that in mind, I decided that, as a user, I would want the ability to input all my symptoms in one place and have the software return all possible diagnoses based on those symptoms.

With this in mind, the first thing I did was create a map of symptoms and potential illnesses associated with those symptoms. For example, if a user were to input that they have a fever and cough, the application would return potential diagnoses based on those symptoms, such as the flu, COVID-19, or bronchitis.

Then I decided it would be helpful for the user to also receive advice on which specialists they could consult based on the diagnoses - whether that's a neurologist, general practitioner, cardiologist, or similar. I implemented this feature using the same logic. This was one of the first things I wrote in Clojure, so it was really interesting as I was getting used to the syntax and how things work. At first, my functions would return results I didn’t want, but I kept testing them in the REPL until I wrote the code that produced the desired outcome.

After that, the idea of having a chat feature came to mind. I started by implementing a chatbot that would respond to some predefined questions. Then, I worked on implementing a one-on-one chat between the user and a specialist doctor, as well as a group chat—a somewhat toxic group chat where anyone with similar "Dr. Google" diagnoses could join and collectively discuss their ailments. Since I was reading Clojure for the Brave and True at the time, specifically the chapter on core.async, I used that to implement the chat functionality.

After that, I came up with the idea that it would be nice for the user to always have access to their history of symptoms over time and that it would also be helpful for the user to receive personalized treatment suggestions based on their diagnoses—such as which supplements they could take to improve their condition, what types of food they should eat, and whether they should engage in any physical activities, and if so, which ones.

Additionally, I realized it would be important to provide the probability of potential illnesses. For example, it wouldn’t be ideal for the user to receive a diagnosis like either migraines or brain cancer just based on the symptom "headache". So, I implemented this feature on a basic level as well.
    
On Kaggle, I found a CSV file containing symptoms and potential disease diagnoses, and I used it instead of the hardcoded symptoms and diagnoses I had at the very beginning. Honestly, I struggled a lot with this because I tried various approaches and spent about three days just writing and deleting my code. I experimented with various methods, including neural networks and decision trees, but ultimately didn't apply any machine learning algorithms. Despite the frustration, I learned a lot through all the trial and error, even if it cost me a lot of patience. I also got closer to Clojure community since I have spent quite a bit of time on Reddit and Stack Overflow.

Working on this project was very interesting and useful for me because, I’m not used to starting a project without first planning everything out in detail. This was the first time I began with the implementation of the smallest unit needed to realize a user story, and then built upon that code, adding new features, improving the old implementation, and so on. This project was a great starting point for stepping out of existing frameworks and my comfort zone.


## License

Copyright © 2024 FIXME

This program and the accompanying materials are made available under the
terms of the Eclipse Public License 2.0 which is available at
http://www.eclipse.org/legal/epl-2.0.