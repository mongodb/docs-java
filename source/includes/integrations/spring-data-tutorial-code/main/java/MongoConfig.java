package com.mongodb.examples.springdatabulkinsert;

import com.mongodb.ConnectionString;
import com.mongodb.MongoClientSettings;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.core.MongoTemplate;

import javax.net.ssl.SSLContext;
import java.nio.file.Paths;
import nl.altindag.ssl.SSLFactory;

// start-mongoconfig
@Configuration
public class MongoConfig   {
    @Value("${mongodb.uri}")
    private String uri;
    @Value("${mongodb.database}")
    private String databaseName;

    @Value("${truststore.path}")
    private String trustStorePath;
    @Value("${truststore.pwd}")
    private String trustStorePwd;

    @Value("${mongodb.atlas}")
    private boolean atlas;

    @Bean
    public MongoClient mongo() {

        ConnectionString connectionString = new ConnectionString(uri);

            MongoClientSettings mongoClientSettings = MongoClientSettings.builder()
                    .applyConnectionString(connectionString)
                    .applyToSslSettings(builder -> {
                        if (!atlas) {
                            // Use SSLContext if a trustStore has been provided
                            if (!trustStorePath.isEmpty()) {
                                SSLFactory sslFactory = SSLFactory.builder()
                                        .withTrustMaterial(Paths.get(trustStorePath), trustStorePwd.toCharArray())
                                        .build();
                                SSLContext sslContext = sslFactory.getSslContext();
                                builder.context(sslContext);
                                builder.invalidHostNameAllowed(true);
                            }
                        }
                        builder.enabled(true);
                    })
                    .build();


        return MongoClients.create(mongoClientSettings);
    }

    @Bean
    public MongoTemplate mongoTemplate() throws Exception {
        return new MongoTemplate(mongo(), databaseName);
    }
}
// end-mongoconfig
