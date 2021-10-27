.. code-block:: java

   MongoCredential credential = MongoCredential.createAwsCredential("<username>", "<password>".toCharArray());

   MongoClient mongoClient = MongoClients.create(
       MongoClientSettings.builder()
           .applyToClusterSettings(builder ->
                   builder.hosts(Arrays.asList(new ServerAddress("<atlasUri>"))))
           .credential(credential)
           .build());

