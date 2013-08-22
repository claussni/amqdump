/*
 * This file is part of Qucosa.
 *
 * Qucosa is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * Qucosa is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 */
package org.qucosa.amqdump;

import com.yourmediashelf.fedora.client.messaging.JMSManager;
import com.yourmediashelf.fedora.client.messaging.MessagingClient;
import com.yourmediashelf.fedora.client.messaging.MessagingException;
import com.yourmediashelf.fedora.client.messaging.MessagingListener;
import org.apache.activemq.jndi.ActiveMQInitialContextFactory;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.TextMessage;
import javax.naming.Context;
import java.util.Properties;

public class Main implements MessagingListener {

	public static final String ACTIVEMQ_CONTEXT_FACTORY_NAME = ActiveMQInitialContextFactory.class.getCanonicalName();

	public static void main(String[] args) throws MessagingException {
		new Main().start();
	}

	public void start() throws MessagingException {
		Properties properties = new Properties();
		properties.setProperty(Context.INITIAL_CONTEXT_FACTORY,
				ACTIVEMQ_CONTEXT_FACTORY_NAME);
		properties.setProperty(Context.PROVIDER_URL, "tcp://localhost:61616");
		properties.setProperty(JMSManager.CONNECTION_FACTORY_NAME, "ConnectionFactory");
		properties.setProperty("topic.fedora", "fedora.apim.*");
		new MessagingClient("amqdump", this, properties, false).start();
	}

	public void onMessage(String clientId, Message message) {
		try {
			String messageText = ((TextMessage) message).getText();
			System.out.printf("Message received: %s on %s\n%s\n",
					message.getStringProperty("methodName"),
					message.getStringProperty("pid"),
					messageText);
		} catch (JMSException e) {
			System.err.println("Error retrieving message " + e.getMessage());
		}
	}

}
