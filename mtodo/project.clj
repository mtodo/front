(defproject mtodo "0.1.0-SNAPSHOT"
  :description "FIXME: write description"
  :url "http://example.com/FIXME"
  :license {:name "MIT License"
            :url "https://raw.githubusercontent.com/mtodo/front/master/LICENSE"}

  :dependencies [[org.clojure/clojure "1.7.0"]
                 [ring-server "0.4.0"]
                 [reagent "0.5.1"
                  :exclusions [org.clojure/tools.reader cljsjs/react]]
                 [reagent-forms "0.5.13"]
                 [reagent-utils "0.1.7"]
                 [cljsjs/react-with-addons "0.13.3-0"]
                 [cljs-react-test "0.1.3-SNAPSHOT"]
                 [ring "1.4.0"]
                 [ring/ring-defaults "0.1.5"]
                 [compojure "1.4.0"]
                 [hiccup "1.0.5"]
                 [environ "1.0.1"]
                 [org.clojure/clojurescript "1.7.170" :scope "provided"]
                 [secretary "1.2.3"]
                 [venantius/accountant "0.1.6"
                  :exclusions [org.clojure/tools.reader]]
                 
                 ]

  :plugins [[lein-environ "1.0.1"]
            [lein-cljsbuild "1.1.1"]
            [lein-asset-minifier "0.2.4"
             :exclusions [org.clojure/clojure]]]

  :ring {:handler mtodo.handler/app
         :uberwar-name "mtodo.war"}

  :min-lein-version "2.5.0"

  :uberjar-name "mtodo.jar"

  :main mtodo.server

  :clean-targets ^{:protect false} [:target-path
                                    [:cljsbuild :builds :app :compiler :output-dir]
                                    [:cljsbuild :builds :app :compiler :output-to]]

  :source-paths ["src/clj" "src/cljc"]
  :resource-paths ["resources" "target/cljsbuild"]

  :minify-assets
  {:assets
   {"resources/public/css/site.min.css" "resources/public/css/site.css"}}

  :cljsbuild {:builds {:app {:source-paths ["src/cljs" "src/cljc"]
                             :compiler {:output-to "target/cljsbuild/public/js/app.js"
                                        :output-dir "target/cljsbuild/public/js/out"
                                        :asset-path   "js/out"
                                        :optimizations :none
                                        :pretty-print  true}}}}

  :profiles {:dev {:repl-options {:init-ns mtodo.repl}

                   :dependencies [[ring/ring-mock "0.3.0"]
                                  [ring/ring-devel "1.4.0"]
                                  [prone "0.8.3"]
                                  [lein-figwheel "0.5.0-2"
                                   :exclusions [org.clojure/core.memoize
                                                ring/ring-core
                                                org.clojure/clojure
                                                org.ow2.asm/asm-all
                                                org.clojure/data.priority-map
                                                org.clojure/tools.reader
                                                org.clojure/clojurescript
                                                org.clojure/core.async
                                                org.clojure/tools.analyzer.jvm]]
                                  [org.clojure/clojurescript "1.7.170"
                                   :exclusions [org.clojure/clojure org.clojure/tools.reader]]
                                  [org.clojure/tools.nrepl "0.2.12"]
                                  [com.cemerick/piggieback "0.2.1"]
                                  [pjstadig/humane-test-output "0.7.1"]
                                  ]

                   :source-paths ["env/dev/clj"]
                   :plugins [[lein-figwheel "0.5.0-2"
                              :exclusions [org.clojure/core.memoize
                                           ring/ring-core
                                           org.clojure/clojure
                                           org.ow2.asm/asm-all
                                           org.clojure/data.priority-map
                                           org.clojure/tools.reader
                                           org.clojure/clojurescript
                                           org.clojure/core.async
                                           org.clojure/tools.analyzer.jvm]]
                             [org.clojure/clojurescript "1.7.170"]
                             ]

                   :injections [(require 'pjstadig.humane-test-output)
                                (pjstadig.humane-test-output/activate!)]

                   :figwheel {:http-server-root "public"
                              :server-port 3449
                              :nrepl-port 7002
                              :nrepl-middleware ["cemerick.piggieback/wrap-cljs-repl"
                                                 ]
                              :css-dirs ["resources/public/css"]
                              :ring-handler mtodo.handler/app}

                   :env {:dev true}

                   :cljsbuild {:builds {:app {:source-paths ["env/dev/cljs" "test/cljs"]
                                              :figwheel {:on-jsload "mtodo.test/run"}
                                              :compiler {:main "mtodo.dev"
                                                         :source-map true}}

                                        :test {:source-paths ["src/cljs" "src/cljc" "test/cljs"]
                                               :compiler {:output-to "resources/test/test.js"
                                                          :optimizations :whitespace
                                                          :pretty-print true}}

                                        }
                               :test-commands {"unit" ["./script/phantomjs"
                                                       "resources/test/phantom-runner.js"
                                                       "resources/test/test.html"]}
                               }}

             :uberjar {:hooks [minify-assets.plugin/hooks]
                       :source-paths ["env/prod/clj"]
                       :prep-tasks ["compile" ["cljsbuild" "once"]]
                       :env {:production true}
                       :aot :all
                       :omit-source true
                       :cljsbuild {:jar true
                                   :builds {:app
                                            {:source-paths ["env/prod/cljs"]
                                             :compiler
                                             {:optimizations :advanced
                                              :pretty-print false}}}}}})
