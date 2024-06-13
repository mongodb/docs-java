.. note::

   The ``authMechanismProperties`` connection string option takes a list
   of key-value pairs. A *value* in any pair must not contain a comma
   (``,``) character, to prevent the driver from parsing your
   connection string ambiguously. A value can contain a colon (``:``)
   character, however.

   For example, the following code shows a connection string that the
   driver might parse incorrectly:

   .. code-block:: java
      :emphasize-lines: 4

      String uri =
          "mongodb://<hostname>:<port>/?" +
          "authMechanism=MONGODB-OIDC" +
          "&authMechanismProperties=ENVIRONMENT:azure,TOKEN_RESOURCE:abc,def";
   
   If a value contains a comma character, you must provide your
   credentials in a ``MongoCredential`` instance, as demonstrated in the
   :guilabel:`MongoCredential` tab.
