/*
 * Copyright (c) 2004, Oracle and/or its affiliates. All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions
 * are met:
 *
 *   - Redistributions of source code must retain the above copyright
 *     notice, this list of conditions and the following disclaimer.
 *
 *   - Redistributions in binary form must reproduce the above copyright
 *     notice, this list of conditions and the following disclaimer in the
 *     documentation and/or other materials provided with the distribution.
 *
 *   - Neither the name of Oracle or the names of its
 *     contributors may be used to endorse or promote products derived
 *     from this software without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS
 * IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO,
 * THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR
 * PURPOSE ARE DISCLAIMED.  IN NO EVENT SHALL THE COPYRIGHT OWNER OR
 * CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL,
 * EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO,
 * PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR
 * PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF
 * LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING
 * NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */

package com.mycompany.app;
import com.mongodb.ConnectionString;
import com.mongodb.MongoClientSettings;
import com.mongodb.client.*;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.UpdateOptions;
import com.mongodb.client.model.Updates;
import com.mongodb.management.JMXConnectionPoolListener;
import org.bson.Document;
import org.bson.conversions.Bson;

import javax.management.remote.JMXConnectorServer;
import javax.management.remote.JMXConnectorServerFactory;
import javax.management.remote.JMXServiceURL;
import java.io.IOException;
import java.lang.management.ManagementFactory;
import java.rmi.registry.LocateRegistry;


public class JMXMonitoring {
/*
  Class to demonstrate JMX server.
  Based heavily off of this example from Oracle:
  https://docs.oracle.com/javase/8/docs/technotes/guides/jmx/examples/Basic/Server.java

  This is the reason the above Oracle
  copyright notice has been included
 */

    private static final String COLLECTION = "compound-test";
    private static final String DATABASE = "test";

    public static void main(String[] args) throws InterruptedException {
        String connect_uri = System.getenv("DRIVER_URL");
        System.out.println(connect_uri);
        ConnectionString uri = new ConnectionString("mongodb+srv://test:driver-test@cluster0.ewikg.mongodb.net/");
        JMXConnectionPoolListener connectionPoolListener = new JMXConnectionPoolListener();
        MongoClientSettings settings =
                MongoClientSettings.builder()
                        .applyConnectionString(uri)
                        .applyToConnectionPoolSettings(builder -> builder.addConnectionPoolListener(connectionPoolListener))
                        .build();
        MongoClient mongoClient = MongoClients.create(settings);
        MongoDatabase database = mongoClient.getDatabase(DATABASE);
        MongoCollection<Document> collection = database.getCollection(COLLECTION);
        javax.management.MBeanServer server = ManagementFactory.getPlatformMBeanServer();
        try {
            JMXServiceURL url = new JMXServiceURL(
                    "service:jmx:rmi:///jndi/rmi://127.0.0.1:9999/server");
            JMXConnectorServer cs =
                    JMXConnectorServerFactory.newJMXConnectorServer(url, null, server);
            // Start the RMI connector server
            //
            echo("\nStart the RMI connector server");
            // TODO: RESEARCH THIS LINE OF CODE
            LocateRegistry.createRegistry(9999);
            cs.start();
            echo("\nThe RMI connector server successfully started");
            echo("and is ready to handle incoming connections");
            echo("\nStart the client on a different window and");
            echo("press <Enter> once the client has finished");
            waitForEnterPressed();

            // Stop the RMI connector server
            //
            echo("\nStop the RMI connector server");
            cs.stop();
            System.out.println("\nBye! Bye!");
        } catch (Exception e) {
          e.printStackTrace();
        }
    }

    private static void waitForEnterPressed() {
        try {
            echo("\nPress <Enter> to continue...");
            System.in.read();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void echo(String msg) {
        System.out.println(msg);
    }
}
