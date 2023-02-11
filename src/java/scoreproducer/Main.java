/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package scoreproducer;

import javax.annotation.Resource;
import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.MessageProducer;
import javax.jms.Session;
import javax.jms.TextMessage;
import javax.jms.Topic;
import java.util.Scanner;

/**
 *
 * @author siriya_s
 */
public class Main {
    // inject destination
    @Resource(mappedName = "jms/SimpleJMSTopic")
    private static Topic topic;
    @Resource(mappedName = "jms/ConnectionFactory")
    private static ConnectionFactory connectionFactory;
    
    public static void main(String[] args) {
        Scanner input = new Scanner(System.in);
        Connection connection = null;
        Destination dest = (Destination) topic;

        try {
            // create connection from connectionfactory
            connection = connectionFactory.createConnection();

            // create session
            Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);

            // create message producer from session
            MessageProducer producer = session.createProducer(dest);
            // create message from session
            TextMessage message = session.createTextMessage();
            while (true) {
                System.out.print("Enter Live Score (To end program, type Q or q): ");
                String score = input.nextLine();
                if (score.equalsIgnoreCase("q")) {
                    return;
                }
                message.setText(score);
                producer.send(message);
            }
        } catch (JMSException e) {
            System.err.println("Exception occurred: " + e.toString());
        } finally {
            if (connection != null) {
                try {
                    connection.close();
                } catch (JMSException e) {
                }
            }
        }
    }
}
