(ns build
  (:require [clojure.tools.build.api :as b]
            [deps-deploy.deps-deploy :as dd]))

(def lib 'ai.bont/axiom-logback-appender)
(def version "1.0.0")
(def class-dir "target/classes")
(def jar-file (format "target/%s-%s.jar" (name lib) version))
(def basis (b/create-basis {:project "deps.edn"}))

(defn clean [_]
      (b/delete {:path "target"}))

(defn compile-clj [_]
      (b/compile-clj {:basis        basis
                      :src-dirs     ["src"]
                      :class-dir    class-dir
                      :compile-opts {:direct-linking true}
                      :ns-compile   '[ai.bont.axiom-logback-appender]}))

(defn jar [_]
      (compile-clj nil)
      (b/jar {:class-dir class-dir
              :lib       lib
              :version   version
              :jar-file  jar-file}))

(defn install [_]
      (jar nil)
      (b/install {:basis     basis
                  :lib       lib
                  :version   version
                  :jar-file  jar-file
                  :class-dir class-dir}))

(defn deploy [_]
      (jar nil)
      (dd/deploy {:installer :remote
                  :artifact  jar-file
                  :pom-file  (b/pom-path {:lib lib :class-dir class-dir})}))
