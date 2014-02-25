(ns
  ^{:author cWX205128}
  test.Main
  (:import [org.apache.http.impl.client HttpClients BasicCredentialsProvider]
           [org.apache.http.client.methods HttpGet]
           [org.apache.http HttpHost]
           [org.apache.http.impl.conn DefaultProxyRoutePlanner]
           [org.apache.http.auth AuthScope UsernamePasswordCredentials]
           [java.security KeyStore]
           [java.io FileInputStream File]
           [org.apache.http.conn.ssl SSLContexts TrustSelfSignedStrategy SSLConnectionSocketFactory]
           [org.apache.http.client.utils URIBuilder]))

(def gitHubToken "256293739d62438c92cf1a45debe281f740ac532") ;是

(def trustStore (. KeyStore (getInstance (. KeyStore getDefaultType))))
(. trustStore (load (FileInputStream. (File. "C:\\Users\\gavin\\.keystore")) (. "changeit" toCharArray)))
(def sslcontext (.. SSLContexts custom (loadTrustMaterial trustStore (TrustSelfSignedStrategy.)) build))
(def sslsf (SSLConnectionSocketFactory. sslcontext (. SSLConnectionSocketFactory ALLOW_ALL_HOSTNAME_VERIFIER)))
(def credsProvider (.. (BasicCredentialsProvider.) (setCredentials (AuthScope. "openproxy.huawei.com",8080) (UsernamePasswordCredentials. "cwx205128", "@Edn1aEf"))))

(def hy_proxy (HttpHost. "localhost",8087))
(def routePlanner (DefaultProxyRoutePlanner. hy_proxy))

(def http_client (.. HttpClients custom
                   ;(setDefaultCredentialsProvider credsProvider)
                   (setRoutePlanner routePlanner)
                   (setSSLSocketFactory sslsf)
                   build))

(def httpGet (HttpGet. "https://api.github.com/user"))
(. httpGet (addHeader "Authorization" "token 256293739d62438c92cf1a45debe281f740ac532"))

(def response (. http_client execute httpGet))

(println (.. response getStatusLine toString))

(def inputStream (.. response getEntity getContent))
(with-open [r inputStream] (loop [c (.read r)] (if (not= c -1) (do (print (char c)) (recur (.read r))))))

