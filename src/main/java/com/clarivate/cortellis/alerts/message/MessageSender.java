package com.clarivate.cortellis.alerts.message;

import com.clarivate.cortellis.alerts.exceptions.MessageProducerException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.jms.*;

public class MessageSender {

	private static final Logger logger = LoggerFactory.getLogger(MessageSender.class);

	private static int TTL = 0;

	private String url;
	private String topicName;
	private String message;
    private ConnectionFactory connFactory;
    private Topic topic;
    private Session session;
    private Connection connection;

	public MessageSender(String url, String topicName, String message) {
		this.url = url;
		this.topicName = topicName;
		this.message = message;
	}

	public void send() throws JMSException,MessageProducerException {
		try {
            connFactory = getConnectionFactory(url);
            connection = connFactory.createConnection();
			session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
			topic = session.createTopic(topicName);
			MessageProducer producer = session.createProducer(topic);
			producer.setDeliveryMode(DeliveryMode.PERSISTENT);
			TextMessage msg = session.createTextMessage();
			msg.setJMSType("System");
			msg.setText(message);
			logger.debug("Sending Message of type: " + "System"
					+ ". Message content: \n" + msg.getText()
					+ ". Topic: "+topicName
					+ ". URL: "+url);
			producer.setTimeToLive(TTL);
			producer.send(msg);
		} catch (JMSException jmse) {
			logger.error("Exception: "+jmse.getMessage());
			throw new JMSException(jmse.getMessage());

		}catch (Exception e){
            throw new MessageProducerException(e);
        }finally {
            if (session!=null) session.close();
            if(connection!=null)connection.close();
        }

	}

	private ConnectionFactory getConnectionFactory(String URL) {
		return new org.apache.activemq.ActiveMQConnectionFactory(URL);
	}

}
