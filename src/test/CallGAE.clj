(ns
  ^{:author cWX205128}
  test.CallGAE
  (:import [org.apache.http.impl.client HttpClients BasicCredentialsProvider]
   [org.apache.http.client.methods HttpGet]
   [org.apache.http HttpHost]
   [org.apache.http.impl.conn DefaultProxyRoutePlanner]
   [org.apache.http.auth AuthScope UsernamePasswordCredentials]
   [java.security KeyStore]
   [java.io FileInputStream File]
   [org.apache.http.conn.ssl SSLContexts TrustSelfSignedStrategy SSLConnectionSocketFactory]
   [org.apache.http.client.utils URIBuilder]))


(def hy_proxy (HttpHost. "proxyse-rd.huawei.com",8080))
(def routePlanner (DefaultProxyRoutePlanner. hy_proxy))

(def credsProvider (BasicCredentialsProvider.))
(.. credsProvider (setCredentials
                    (AuthScope. "proxyse-rd.huawei.com",8080)
                    (UsernamePasswordCredentials. "cwx205128", "@Edn1aEf")))

(def http_client
  (.. HttpClients custom
                  (setDefaultCredentialsProvider credsProvider)
                  (setRoutePlanner routePlanner)
                  build))

(def httpGet (HttpGet. "http://clarkhillm2.appspot.com"))

(def response (. http_client execute httpGet))

(println (.. response getStatusLine toString))

(with-open [r (.. response getEntity getContent)]
  (loop [c (.read r)] (if (not= c -1) (do (print (char c)) (recur (.read r))))))
