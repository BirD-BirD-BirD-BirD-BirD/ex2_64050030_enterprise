/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package subscriber;

//import javax.annotation.Resource;
import javax.jms.Topic;
//import java.util.logging.Level;
//import java.util.logging.Logger;
import javax.annotation.Resource;
import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.Message;
//import javax.jms.MessageProducer;
import javax.jms.Queue;
import javax.jms.Session;
import javax.jms.TextMessage;
import javax.jms.MessageConsumer;
/**
 *
 * @author Admin
 */
public class Main {

    /**
     * @param args the command line arguments
     */
    @Resource(mappedName = "jms/SimpleJMSQueue")
    private static Queue queue;
    
    @Resource(mappedName = "jms/SimpleJMSTopic")
    private static Topic topic;

    @Resource(mappedName = "jms/ConnectionFactory")
    private static ConnectionFactory connectionFactory;
    
    public static void main(String[] args) {
        // TODO code application logic here
        String destType = null;
        Connection connection = null;
        Session session = null;
        Destination dest = null;
        MessageConsumer consumer = null;
        TextMessage  message = null;
        
        if(args.length != 1){
            System.err.println("Program takes one argument: <dest_type>");
            System.exit(1);
        }//takes queue or topic as argument
        
        destType = args[0];
        System.out.println("Destination type is " + destType);
        
        if(!(destType.equals("queue") || destType.equals("topic"))) {
            System.err.println("Argument must be \"queue\" or \"topic\"");
            System.exit(1);
        }
        
        try{
            if(destType.equals("queue")){
                dest = (Destination) queue;
            }else{
                dest = (Destination) topic;
            }
        }catch(Exception e){
            System.err.println("Error setting destination: " + e.toString());
            System.exit(1);
        }
        
        try{
            //สร้างการเชื่อมต่อและ session 
            connection = connectionFactory.createConnection();
            session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
            consumer = session.createConsumer(dest);
            connection.start(); //เริ่มการเชื่อมต่อ
            
            while(true){
                Message m = consumer.receive(); //block
                
                
                message = (TextMessage) m;
                System.out.println("Reading message: " + message.getText());
                    
                
            }
        }catch(JMSException e){
            System.err.println("Exception occured: " + e.toString());
        }finally{
            if(connection != null){
                try {
                    connection.close();
                }catch (JMSException e){
                    
                }
            }
        }
    }
    
}
