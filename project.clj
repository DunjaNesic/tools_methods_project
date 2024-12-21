(defproject tools_methods_project "0.1.0-SNAPSHOT"
  :description "FIXME: write description"
  :url "http://example.com/FIXME"
  :license {:name "EPL-2.0 OR GPL-2.0-or-later WITH Classpath-exception-2.0"
            :url "https://www.eclipse.org/legal/epl-2.0/"}
  :dependencies [[org.clojure/clojure "1.11.1"]
                 [org.clojure/core.async "1.6.681"]
                 [org.clojure/data.json "2.5.1"]
                 [metosin/reitit "0.7.2"]
                 [ring/ring-core "1.13.0"]
                 [ring/ring-jetty-adapter "1.13.0"]
                 [org.clojure/java.jdbc "0.7.12"]
                 [seancorfield/next.jdbc "1.2.659"]
                 [org.postgresql/postgresql "42.5.0"]
                 [buddy/buddy-hashers "2.0.167"]
                 [criterium "0.4.6"]
                 [com.clojure-goes-fast/clj-async-profiler "1.5.1"]
                 [org.clojure/data.json "2.5.1"]
                 [org.clojure/data.csv "1.1.0"]
                 [org.deeplearning4j/deeplearning4j-core "1.0.0-M2.1"]
                 [org.nd4j/nd4j-native-platform "1.0.0-M2.1"]
                 [org.deeplearning4j/deeplearning4j-datasets "1.0.0-M2.1"]
                 [org.nd4j/nd4j-common "1.0.0-M2"]]
  :profiles {:dev {:dependencies [[midje "1.10.10" :exclusions [org.clojure/clojure]]]
                   :plugins [[lein-midje "3.2.1"]]}}
  :repl-options {:init-ns tools-methods-project.core})
