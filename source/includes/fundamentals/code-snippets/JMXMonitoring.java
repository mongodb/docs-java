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

/*
  Class to demonstrate JMX server for connection pool events in MongoDB Java Driver.
  Based heavily off of this example from Oracle:
  https://docs.oracle.com/javase/8/docs/technotes/guides/jmx/examples/Basic/Server.java

  This is the reason the above Oracle
  copyright notice has been included
 */
public class JMXMonitoring {

    private static final String COLLECTION = "compound-test";
    private static final String DATABASE = "test";
    private static final ConnectionString URI = new ConnectionString(System.getenv("DRIVER_URL"));

    public static void main(String[] args) throws InterruptedException {
        // start jmx-example
        JMXConnectionPoolListener connectionPoolListener = new JMXConnectionPoolListener();
        MongoClientSettings settings =
                MongoClientSettings.builder()
                        .applyConnectionString(URI)
                        .applyToConnectionPoolSettings(builder -> builder.addConnectionPoolListener(connectionPoolListener))
                        .build();
        MongoClient mongoClient = MongoClients.create(settings);
        int port = 9999;
        javax.management.MBeanServer server = ManagementFactory.getPlatformMBeanServer();
        try {
            JMXServiceURL url = new JMXServiceURL(
                    String.format("service:jmx:rmi:///jndi/rmi://127.0.0.1:%d/server",
                            port));
            JMXConnectorServer cs =
                    JMXConnectorServerFactory.newJMXConnectorServer(url, null, server);
            // Start the connector server
            System.out.println("Start the connector server...");
            // TODO: RESEARCH THIS LINE OF CODE
            LocateRegistry.createRegistry(port);
            cs.start();
            System.out.println("The connector server started.");
            System.out.println("Press <Enter> to stop the server.");
            waitForEnterPressed();
            // Stop the connector server
            System.out.println("Stopping the connector server...");
            cs.stop();
            System.out.println("Bye! Bye!");
        } catch (Exception e) {
            e.printStackTrace();
        }
        // end jmx-example
    }

    private static void waitForEnterPressed() {
        try {
            System.in.read();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
