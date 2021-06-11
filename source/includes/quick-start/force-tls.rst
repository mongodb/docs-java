.. note:: Known connection issue when using TLS v1.3

   If you encounter an error connecting to your MongoDB instance or cluster
   that resembles the following while running your application on Java 11 and
   later, you may need to force your application to use the TLS 1.2 protocol:

   .. code-block:: none
      :copyable: false

      javax.net.ssl.SSLHandshakeException: extension (5) should not be presented in certificate_request

   This exception is a known issue with Java 11 and TLS 1.3.

   To resolve this error, you can force the application to connect using the
   TLS 1.2 protocol instead by specifying the following system property:

   .. code-block:: properties

      jdk.tls.client.protocols=TLSv1.2

   If you run your application from the command line or your IDE, you can
   specify this property as a JVM argument using the following syntax:

   .. code-block:: properties

      -Djdk.tls.client.protocols=TLSv1.2

