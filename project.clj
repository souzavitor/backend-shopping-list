(defproject sandbox "0.1.0-SNAPSHOT"
  :description "FIXME: write description"
  :url "http://example.com/FIXME"
  :license {:name "Eclipse Public License"
            :url "http://www.eclipse.org/legal/epl-v10.html"}
  :plugins [[lein-midje "3.2.1"]]
  :dependencies [[org.clojure/clojure "1.10.0"]
                 [io.pedestal/pedestal.service "0.5.7"]
                 [io.pedestal/pedestal.jetty "0.5.7"]
                 [http-kit "2.2.0"]
                 [nubank/matcher-combinators "1.2.4"]
                 [ch.qos.logback/logback-classic "1.1.8" :exclusions [org.slf4j/slf4j-api]]
                 [org.slf4j/jul-to-slf4j "1.7.22"]
                 [org.slf4j/jcl-over-slf4j "1.7.22"]
                 [org.slf4j/log4j-over-slf4j "1.7.22"]
                 [com.walmartlabs/lacinia-pedestal "0.12.0"]
                 [com.walmartlabs/lacinia "0.35.0"]]
  :resource-paths ["config", "resources"]
  :profiles {:dev {:aliases {"run-dev" ["trampoline" "run" "-m" "sandbox.server/run-dev"]}
                   :dependencies [[midje "1.9.8"]
                                  [io.pedestal/pedestal.service-tools "0.5.7"]]}
             :uberjar {:aot [sandbox.server]}}
  :main ^{:skip-aot true} sandbox.server)
