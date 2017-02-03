package data.producer;

/**
 * SensorDataProducer class simulates the real time truck event generation.
 *
 */
import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.sql.Timestamp;
import java.util.*;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import kafka.javaapi.producer.Producer;
import kafka.producer.KeyedMessage;
import kafka.producer.ProducerConfig;

import org.apache.log4j.Logger;


public class SensorDataProducer {

    private static final Logger LOG = Logger.getLogger(SensorDataProducer.class);

    public static void main(String[] args) 
            throws ParserConfigurationException, SAXException, IOException, URISyntaxException 
    {
        if (args.length != 2) 
        {
            
            System.out.println("Usage: SensorDataProducer <broker list> <zookeeper>");
            System.exit(-1);
        }
        
        LOG.debug("Using broker list:" + args[0] +", zk conn:" + args[1]);
        
        // long events = Long.parseLong(args[0]);
        Random rnd = new Random();
        
        Properties props = new Properties();
        props.put("metadata.broker.list", args[0]);
        props.put("zk.connect", args[1]);
        props.put("serializer.class", "kafka.serializer.StringEncoder");
        props.put("request.required.acks", "1");

        String TOPIC = "sensordata"; // change here from truckevent to sensordata
        ProducerConfig config = new ProducerConfig(props);

        Producer<String, String> producer = new Producer<String, String>(config);

        String[] events = {"Normal", "Normal", "Normal", "Normal", "Normal", "Normal", "Lane Departure", 
                           "Overspeed", "Normal", "breakdown", "Normal", "Normal", "Lane Departure","Normal", 
                           "Normal", "Normal", "Normal",  "Unsafe tail distance", "Normal", "Normal", 
                           "Unsafe following distance", "Normal", "Normal", "Normal","Normal","Normal","Normal",
                           "Normal","Normal","Normal","Normal","Normal","Normal","Normal", "Normal", "Overspeed"
                            ,"Normal", "Normal","Normal","Normal","Normal","Normal","Normal", "breakdown"}; // add here
        
        Random random = new Random();

        String finalEvent = "";
        // new york
        String route17 = "route17.kml";
        String[] arrayroute17 = GetKmlLanLangList(route17);
        String route17k = "route17k.kml";
        String[] arrayroute17k = GetKmlLanLangList(route17k);
        String route208 = "route208.kml";
        String[] arrayroute208 = GetKmlLanLangList(route208);
        String route27 = "route27.kml";
        String[] arrayroute27 = GetKmlLanLangList(route27);
        String route120 = "route120.kml";
        String[] arrayroute120 = GetKmlLanLangList(route120);
		String route120a = "route120a.kml";
        String[] arrayroute120a = GetKmlLanLangList(route120a);// add here
		// california
		String calroute2 = "calroute2.kml";
        String[] arraycalroute2 = GetKmlLanLangList(calroute2);
		String calroute27 = "calroute27.kml";
        String[] arraycalroute27 = GetKmlLanLangList(calroute27); 
		String calroute80 = "calroute80.kml";
        String[] arraycalroute80 = GetKmlLanLangList(calroute80);
		
		// add here
		//texas
		
		String texroute10 = "texroute10.kml";
        String[] arraytexroute10 = GetKmlLanLangList(texroute10); // rainy
		String texroute40 = "texroute40.kml";
        String[] arraytexroute40 = GetKmlLanLangList(texroute40);
		String texroute140 = "texroute140.kml";
        String[] arraytexroute140 = GetKmlLanLangList(texroute140);
		// illinois
		
		String illroute35 = "illroute35.kml";
        String[] arrayillroute35 = GetKmlLanLangList(illroute35); //foggy
		String illroute103 = "illroute103.kml";
        String[] arrayillroute103 = GetKmlLanLangList(illroute103);
		String illroute124 = "illroute124.kml";
        String[] arrayillroute124 = GetKmlLanLangList(illroute124); // windy
		
		
		
		
		
        String[] truckIds = {"1", "2", "3","4","5","6","7","8","9","10","11","12","13","14","15"};
        String[] routeName = {"route17", "route17k", "route208", "route27","route120","route120a","calroute2","calroute27","calroute80","texroute10","texroute40","texroute140","illroute35","illroute103","illroute124"};
        String[] driverIds = {"101", "102", "103", "104","105","106","107","108","109","110","111","112","113","114","115"};
        
        int evtCnt = events.length;
        
        //Find max route arraysize.
        int maxarraysize = arrayroute17.length;
        if(maxarraysize < arrayroute17k.length)
        	maxarraysize = arrayroute17k.length;
        if(maxarraysize < arrayroute208.length)
        	maxarraysize = arrayroute208.length;
        if(maxarraysize < arrayroute27.length)
        	maxarraysize = arrayroute27.length;
        if(maxarraysize < arrayroute120.length)
        	maxarraysize = arrayroute120.length;
			if(maxarraysize < arrayroute120a.length)
        	maxarraysize = arrayroute120a.length; // add  here
        for (int i = 0; i < maxarraysize; i++) 
        {
        	
        	if(arrayroute17.length > i) //windy
        	{
				String cur_event = events[random.nextInt(evtCnt)] ;
	        	finalEvent = new Timestamp(new Date().getTime()) + "|"
	                    + truckIds[0] + "|" + driverIds[0] + "|" + cur_event 
	                    + "|" + getLatLong(arrayroute17[i] +"|" + 0 +"|" + 0+ "|" + 1  + "|" +routeName[0]+ "|" +"New York"); 
	        	try {
				/*	if(cur_event.equals("BreakDown"))
					{
					KeyedMessage<String, String> data = new KeyedMessage<String, String>(TOPIC, finalEvent);
	                LOG.info("Sending Messge #: " + routeName[0] + ": " + i +", msg:" + finalEvent);
	                producer.send(data);
					
					arrayroute17= new String[0];
					Thread.sleep(1000);
					}
					else{*/
						
	                KeyedMessage<String,String> data = new KeyedMessage<String, String>(TOPIC, finalEvent);
	                LOG.info("Sending Messge #: " + routeName[0] + ": " + i +", msg:" + finalEvent);
	                producer.send(data);
	                Thread.sleep(1000);
					//}
	            } catch (Exception e) {
	                e.printStackTrace();
	            }
        	}
        	if(arrayroute17k.length > i) //foggy
        	{
				String cur_event = events[random.nextInt(evtCnt)] ;
	        	finalEvent = new Timestamp(new Date().getTime()) + "|"
	                    + truckIds[1] + "|" + driverIds[1] + "|" + cur_event
	                    + "|" + getLatLong(arrayroute17k[i]+"|" + 1+"|" + 0 +"|" + 0 + "|" +routeName[1]+ "|" +"New York");
	        	try {
				/*	if(cur_event.equals("BreakDown"))
					{
					KeyedMessage<String, String> data = new KeyedMessage<String, String>(TOPIC, finalEvent);
	                LOG.info("Sending Messge #: " + routeName[1] + ": " + i +", msg:" + finalEvent);
	                producer.send(data);
						arrayroute17k= new String[0];
						Thread.sleep(1000);
					}
					else{*/
	                KeyedMessage<String, String> data = new KeyedMessage<String, String>(TOPIC, finalEvent);
	                LOG.info("Sending Messge #: " + routeName[1] + ": " + i +", msg:" + finalEvent);
	                producer.send(data);
	                Thread.sleep(1000);
				//	}
	            } catch (Exception e) {
	                e.printStackTrace();
	            }
        	}
        	if(arrayroute208.length > i)
        	{
				String cur_event = events[random.nextInt(evtCnt)] ;
	        	finalEvent = new Timestamp(new Date().getTime()) + "|"
	                    + truckIds[2] + "|" + driverIds[2] + "|" + cur_event 
	                    + "|" + getLatLong(arrayroute208[i]+"|" + 0+"|" + 0 +"|" + 0 + "|" +routeName[2]+ "|" +"New York" );
	        	try {
					/*if(cur_event.equals("BreakDown"))
					{
					KeyedMessage<String, String> data = new KeyedMessage<String, String>(TOPIC, finalEvent);
	                LOG.info("Sending Messge #: " + routeName[2] + ": " + i +", msg:" + finalEvent);
	                producer.send(data);
					arrayroute208= new String[0];
					Thread.sleep(1000);
					}
					else{*/
	                KeyedMessage<String, String> data = new KeyedMessage<String, String>(TOPIC, finalEvent);
	                LOG.info("Sending Messge #: " + routeName[2] + ": " + i +", msg:" + finalEvent);
	                producer.send(data);
	                Thread.sleep(1000);
					//}
	            } catch (Exception e) {
	                e.printStackTrace();
	            }
        	}
        	if(arrayroute27.length > i)
        	{
				String cur_event = events[random.nextInt(evtCnt)] ;
	        	finalEvent = new Timestamp(new Date().getTime()) + "|"
	                    + truckIds[3] + "|" + driverIds[3] + "|" + cur_event 
	                    + "|" + getLatLong(arrayroute27[i]+"|" + 0+"|" + 0 +"|" + 0 + "|" +routeName[3]+ "|" +"New York");
	        	try {
				/*	if(cur_event.equals("BreakDown"))
					{
					KeyedMessage<String, String> data = new KeyedMessage<String, String>(TOPIC, finalEvent);
	                LOG.info("Sending Messge #: " + routeName[3] + ": " + i +", msg:" + finalEvent);
	                producer.send(data);
					arrayroute27= new String[0];
					Thread.sleep(1000);
					}
					
					else{*/
	               
	                KeyedMessage<String, String> data = new KeyedMessage<String, String>(TOPIC, finalEvent);
	                LOG.info("Sending Messge #: " + routeName[3] + ": " + i +", msg:" + finalEvent);
	                producer.send(data);
	                Thread.sleep(1000);
				//	}
	            } catch (Exception e) {
	                e.printStackTrace();
	            }
        	}
			
			if(arrayroute120.length > i)
        	{
				String cur_event = events[random.nextInt(evtCnt)] ;
	        	finalEvent = new Timestamp(new Date().getTime()) + "|"
	                    + truckIds[4] + "|" + driverIds[4] + "|" + cur_event
	                    + "|" + getLatLong(arrayroute120[i]+"|" + 0+"|" + 0 +"|" + 0 + "|" +routeName[4]+ "|" +"New York" );
	        	try {
					/*if(cur_event.equals("BreakDown"))
					{
					KeyedMessage<String, String> data = new KeyedMessage<String, String>(TOPIC, finalEvent);
	                LOG.info("Sending Messge #: " + routeName[4] + ": " + i +", msg:" + finalEvent);
	                producer.send(data);
					arrayroute120= new String[0];
					Thread.sleep(1000);
					}
					else{*/
	                KeyedMessage<String, String> data = new KeyedMessage<String, String>(TOPIC, finalEvent);
	                LOG.info("Sending Messge #: " + routeName[4] + ": " + i +", msg:" + finalEvent);
	                producer.send(data);
	                Thread.sleep(1000);
					//}
	            } catch (Exception e) {
	                e.printStackTrace();
	            }
        	}// end of rout 120
			if(arrayroute120a.length > i) // rainy
        	{
				String cur_event = events[random.nextInt(evtCnt)] ;
	        	finalEvent = new Timestamp(new Date().getTime()) + "|"
	                    + truckIds[5] + "|" + driverIds[5] + "|" + cur_event
	                    + "|" + getLatLong(arrayroute120a[i]+"|" + 0+"|" + 1 +"|" + 0 + "|" +routeName[5]+ "|" +"New York");
	        	try {
					/*if(cur_event.equals("BreakDown"))
					{
					KeyedMessage<String, String> data = new KeyedMessage<String, String>(TOPIC, finalEvent);
	                LOG.info("Sending Messge #: " + routeName[5] + ": " + i +", msg:" + finalEvent);
	                producer.send(data);
					arrayroute120a= new String[0];
					Thread.sleep(1000);
					}
					else{*/
	                KeyedMessage<String, String> data = new KeyedMessage<String, String>(TOPIC, finalEvent);
	                LOG.info("Sending Messge #: " + routeName[5] + ": " + i +", msg:" + finalEvent);
	                producer.send(data);
	                Thread.sleep(1000);
					//}
	            } catch (Exception e) {
	                e.printStackTrace();
	            }
        	}// end of rout 120a
			
			//route cali 2
			
			if(arraycalroute2 .length > i) 
        	{
				String cur_event = events[random.nextInt(evtCnt)] ;
	        	finalEvent = new Timestamp(new Date().getTime()) + "|"
	                    + truckIds[6] + "|" + driverIds[6] + "|" + cur_event
	                    + "|" + getLatLong(arrayillroute124 [i]+"|" + 0+"|" + 0 +"|" + 0 + "|" +routeName[6]+ "|" +"California" );
	        	try {
					/*if(cur_event.equals("BreakDown"))
					{
					KeyedMessage<String, String> data = new KeyedMessage<String, String>(TOPIC, finalEvent);
	                LOG.info("Sending Messge #: " + routeName[6] + ": " + i +", msg:" + finalEvent);
	                producer.send(data);
					arraycalroute2= new String[0];
					Thread.sleep(1000);
					}
					else{*/
	                KeyedMessage<String, String> data = new KeyedMessage<String, String>(TOPIC, finalEvent);
	                LOG.info("Sending Messge #: " + routeName[6] + ": " + i +", msg:" + finalEvent);
	                producer.send(data);
	                Thread.sleep(1000);
					//}
	            } catch (Exception e) {
	                e.printStackTrace();
	            }
        	}// end of rout cali 2
			
			if(arraycalroute27 .length > i) 
        	{
				String cur_event = events[random.nextInt(evtCnt)] ;
	        	finalEvent = new Timestamp(new Date().getTime()) + "|"
	                    + truckIds[7] + "|" + driverIds[7] + "|" + cur_event
	                    + "|" + getLatLong(arrayillroute124 [i]+"|" + 0+"|" + 0 +"|" + 0 + "|" +routeName[7]+ "|" +"California" );
	        	try {
					/*if(cur_event.equals("BreakDown"))
					{
					KeyedMessage<String, String> data = new KeyedMessage<String, String>(TOPIC, finalEvent);
	                LOG.info("Sending Messge #: " + routeName[7] + ": " + i +", msg:" + finalEvent);
	                producer.send(data);
					arraycalroute27= new String[0];
					Thread.sleep(1000);
					}
					else{*/
	                KeyedMessage<String, String> data = new KeyedMessage<String, String>(TOPIC, finalEvent);
	                LOG.info("Sending Messge #: " + routeName[7] + ": " + i +", msg:" + finalEvent);
	                producer.send(data);
	                Thread.sleep(1000);
				//	}
	            } catch (Exception e) {
	                e.printStackTrace();
	            }
        	}// end of rout 120a
			if(arraycalroute80 .length > i)
        	{
				String cur_event = events[random.nextInt(evtCnt)] ;
	        	
						finalEvent = new Timestamp(new Date().getTime()) + "|"
	                    + truckIds[8] + "|" + driverIds[8] + "|" + cur_event
	                    + "|" + getLatLong(arrayillroute124 [i]+"|" + 0+"|" + 0 +"|" + 0 + "|" +routeName[8]+ "|" +"California" );
	        	try {
				/*	if(cur_event.equals("BreakDown"))
					{
					KeyedMessage<String, String> data = new KeyedMessage<String, String>(TOPIC, finalEvent);
	                LOG.info("Sending Messge #: " + routeName[8] + ": " + i +", msg:" + finalEvent);
	                producer.send(data);
						arraycalroute80= new String[0];
						Thread.sleep(1000);
					}
					else{*/
	                KeyedMessage<String, String> data = new KeyedMessage<String, String>(TOPIC, finalEvent);
	                LOG.info("Sending Messge #: " + routeName[8] + ": " + i +", msg:" + finalEvent);
	                producer.send(data);
	                Thread.sleep(1000);
					//}
	            } catch (Exception e) {
	                e.printStackTrace();
	            }
        	}// end of rout 120a
			if(arraytexroute10 .length > i) // rainy
        	{
				String cur_event = events[random.nextInt(evtCnt)] ;
	        	finalEvent = new Timestamp(new Date().getTime()) + "|"
	                    + truckIds[9] + "|" + driverIds[9] + "|" + cur_event 
	                    + "|" + getLatLong(arraytexroute10 [i]+"|" + 0+"|" + 1 +"|" + 0 + "|" +routeName[9]+ "|" +"Texas");
	        	try {
				/*	if(cur_event.equals("BreakDown"))
					{
					KeyedMessage<String, String> data = new KeyedMessage<String, String>(TOPIC, finalEvent);
	                LOG.info("Sending Messge #: " + routeName[9] + ": " + i +", msg:" + finalEvent);
	                producer.send(data);
					arraytexroute10= new String[0];
					Thread.sleep(1000);
					}
					else{*/
	                KeyedMessage<String, String> data = new KeyedMessage<String, String>(TOPIC, finalEvent);
	                LOG.info("Sending Messge #: " + routeName[9] + ": " + i +", msg:" + finalEvent);
	                producer.send(data);
	                Thread.sleep(1000);
					//}
	            } catch (Exception e) {
	                e.printStackTrace();
	            }
        	}// end of rout 120a
			if(arraytexroute40 .length > i) // rainy
        	{
				String cur_event = events[random.nextInt(evtCnt)] ;
	        	finalEvent = new Timestamp(new Date().getTime()) + "|"
	                    + truckIds[10] + "|" + driverIds[10] + "|" + cur_event
	                    + "|" + getLatLong(arraytexroute40 [i]+"|" + 0+"|" + 0 +"|" + 0 + "|" +routeName[10]+ "|" +"Texas");
	        	try {
				/*	if(cur_event.equals("BreakDown"))
					{
					KeyedMessage<String, String> data = new KeyedMessage<String, String>(TOPIC, finalEvent);
	                LOG.info("Sending Messge #: " + routeName[10] + ": " + i +", msg:" + finalEvent);
	                producer.send(data);
					arraytexroute40= new String[0];
					Thread.sleep(1000);
					}
					else{*/
	                KeyedMessage<String, String> data = new KeyedMessage<String, String>(TOPIC, finalEvent);
	                LOG.info("Sending Messge #: " + routeName[10] + ": " + i +", msg:" + finalEvent);
	                producer.send(data);
	                Thread.sleep(1000);
					//}
	            } catch (Exception e) {
	                e.printStackTrace();
	            }
        	}// end of rout 120a
			if(arraytexroute140 .length > i) // rainy
        	{
				String cur_event = events[random.nextInt(evtCnt)] ;
	        	finalEvent = new Timestamp(new Date().getTime()) + "|"
	                    + truckIds[11] + "|" + driverIds[11] + "|" + cur_event
	                    + "|" + getLatLong(arraytexroute140 [i]+"|" + 0+"|" + 0 +"|" + 0 + "|" +routeName[11]+ "|" +"Texas" );
	        	try {
					/*if(cur_event.equals("BreakDown"))
					{
					KeyedMessage<String, String> data = new KeyedMessage<String, String>(TOPIC, finalEvent);
	                LOG.info("Sending Messge #: " + routeName[11] + ": " + i +", msg:" + finalEvent);
	                producer.send(data);
					arraytexroute140= new String[0];
					Thread.sleep(1000);
					}
					else{*/
	                KeyedMessage<String, String> data = new KeyedMessage<String, String>(TOPIC, finalEvent);
	                LOG.info("Sending Messge #: " + routeName[11] + ": " + i +", msg:" + finalEvent);
	                producer.send(data);
	                Thread.sleep(1000);
					//}
	            } catch (Exception e) {
	                e.printStackTrace();
	            }
        	}// end of rout 120a
			if(arrayillroute35 .length > i) // foggy
        	{
				String cur_event = events[random.nextInt(evtCnt)] ;
	      		finalEvent = new Timestamp(new Date().getTime()) + "|"
	                    + truckIds[12] + "|" + driverIds[12] + "|breakdown"
	                    + "|" + getLatLong(arrayillroute124 [i]+"|" + 1+"|" + 0 +"|" + 0 + "|" +routeName[12]+ "|" +"Illinois" );
	        	try {
					/*if(cur_event.equals("BreakDown"))
					{
					KeyedMessage<String, String> data = new KeyedMessage<String, String>(TOPIC, finalEvent);
	                LOG.info("Sending Messge #: " + routeName[12] + ": " + i +", msg:" + finalEvent);
	                producer.send(data);
					arrayillroute35= new String[0];
					Thread.sleep(1000);
					}
					else{*/
	                KeyedMessage<String, String> data = new KeyedMessage<String, String>(TOPIC, finalEvent);
	                LOG.info("Sending Messge #: " + routeName[12] + ": " + i +", msg:" + finalEvent);
	                producer.send(data);
	                Thread.sleep(1000);
					//}
	            } catch (Exception e) {
	                e.printStackTrace();
	            }
        	}// end of rout 120a
			if(arrayillroute103 .length > i) 
        	{
				String cur_event = events[random.nextInt(evtCnt)] ;
	        	
				finalEvent = new Timestamp(new Date().getTime()) + "|"
	                    + truckIds[13] + "|" + driverIds[13] + "|" + cur_event
	                    + "|" + getLatLong(arrayillroute124 [i]+"|" + 0+"|" + 0 +"|" + 0 + "|" +routeName[13]+ "|" +"Illinois" );
	        	try {
					/*if(cur_event.equals("BreakDown"))
					{
					KeyedMessage<String, String> data = new KeyedMessage<String, String>(TOPIC, finalEvent);
	                LOG.info("Sending Messge #: " + routeName[13] + ": " + i +", msg:" + finalEvent);
	                producer.send(data);
					arrayillroute103= new String[0];
					Thread.sleep(1000);
					}
					else{*/
	                KeyedMessage<String, String> data = new KeyedMessage<String, String>(TOPIC, finalEvent);
	                LOG.info("Sending Messge #: " + routeName[13] + ": " + i +", msg:" + finalEvent);
	                producer.send(data);
	                Thread.sleep(1000);
				//	}
	            } catch (Exception e) {
	                e.printStackTrace();
	            }
        	}// end of rout 120a
			if(arrayillroute124 .length > i) // rainy
        	{
				String cur_event = events[random.nextInt(evtCnt)] ;
	        	finalEvent = new Timestamp(new Date().getTime()) + "|"
	                    + truckIds[14] + "|" + driverIds[14] + "|" + cur_event
	                    + "|" + getLatLong(arrayillroute124 [i]+"|" + 0+"|" + 0 +"|" + 1 + "|" +routeName[14]+ "|" +"Illinois" );
	        	try {
					/*if(cur_event.equals("BreakDown"))
					{
					KeyedMessage<String, String> data = new KeyedMessage<String, String>(TOPIC, finalEvent);
	                LOG.info("Sending Messge #: " + routeName[14] + ": " + i +", msg:" + finalEvent);
	                producer.send(data);
					arrayillroute124=new String[0];
					Thread.sleep(1000);
					}
					else{*/
	                KeyedMessage<String, String> data = new KeyedMessage<String, String>(TOPIC, finalEvent);
	                LOG.info("Sending Messge #: " + routeName[14] + ": " + i +", msg:" + finalEvent);
	                producer.send(data);
	                Thread.sleep(1000);
				//	}
	            } catch (Exception e) {
	                e.printStackTrace();
	            }
        	}// end of rout 120a
			
			
			
			
			
			
			
			
			
			
			
			
			
        }// end of for i
        
//        for (int i = 0; i < arrayroute17.length; i++) 
//        {
//        	finalEvent = new Timestamp(new Date().getTime()) + "|"
//                    + truckIds[0] + "|" + driverIds[0] + "|" + events[random.nextInt(evtCnt)] 
//                    + "|" + getLatLong(arrayroute17[i]);
//        	try {
//                KeyedMessage<String, String> data = new KeyedMessage<String, String>(TOPIC, finalEvent);
//                LOG.info("Sending Messge #: " + routeName[0] + ": " + i +", msg:" + finalEvent);
//                producer.send(data);
//                Thread.sleep(3000);
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
//        }
//        for (int j = 0; j < arrayroute17k.length; j++) 
//        {
//        	finalEvent = new Timestamp(new Date().getTime()) + "|"
//        			+ truckIds[1] + "|" + driverIds[1] + "|" + events[random.nextInt(evtCnt)] 
//                    + "|" + getLatLong(arrayroute17k[j]);
//        	try {
//                KeyedMessage<String, String> data = new KeyedMessage<String, String>(TOPIC, finalEvent);
//                LOG.info("Sending Messge #: " + routeName[1] + ": " + j +", msg:" + finalEvent);
//                producer.send(data);
//                Thread.sleep(3000);
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
//        }
//
//        for (int k = 0; k < arrayroute208.length; k++) 
//        {
//        	finalEvent = new Timestamp(new Date().getTime()) + "|" 
//                    + truckIds[2] + "|" + driverIds[2] + "|" + events[random.nextInt(evtCnt)] 
//                    + "|" + getLatLong(arrayroute208[k]);
//        	try {
//                KeyedMessage<String, String> data = new KeyedMessage<String, String>(TOPIC, finalEvent);
//                LOG.info("Sending Messge #: " + routeName[2] + ": " + k +", msg:" + finalEvent);
//                producer.send(data);
//                Thread.sleep(3000);
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
//        }
//        
//        for (int l = 0; l < arrayroute27.length; l++) 
//        {
//        	finalEvent = new Timestamp(new Date().getTime()) + "|" 
//                    + truckIds[3] + "|" + driverIds[3] + "|" + events[random.nextInt(evtCnt)] 
//                    + "|" + getLatLong(arrayroute27[l]);
//        	try {
//                KeyedMessage<String, String> data = new KeyedMessage<String, String>(TOPIC, finalEvent);
//                LOG.info("Sending Messge #: " + routeName[3] + ": " + l +", msg:" + finalEvent);
//                producer.send(data);
//                Thread.sleep(3000);
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
//        }
        
        producer.close();
    }

    private static String getLatLong(String str)
    {
        str=str.replace("\t", "");
        str=str.replace("\n", "");
        
        String[] latLong = str.split("|");
        
        if (latLong.length == 2)
        {
            return latLong[1].trim() + "|" + latLong[0].trim();
        } 
        else 
        {
            return str;    
        }
    }
    
	public static String[] GetKmlLanLangList(String urlString) throws ParserConfigurationException, SAXException, IOException {
        String[] array = null;
        String[] array2 = null;
        
        Document doc = null;
        String pathConent = "";
        File fXmlFile = new File(urlString);
        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        DocumentBuilder db = dbf.newDocumentBuilder();
        doc = db.parse(ClassLoader.getSystemResourceAsStream(urlString));
        doc.getDocumentElement().normalize();
        NodeList nList = doc.getElementsByTagName("LineString");
        System.out.println(nList.getLength());
        int j=0;
        for (int temp = 0; temp < nList.getLength();temp++) {
        	
                Node nNode = nList.item(temp);
                if (nNode.getNodeType() == Node.ELEMENT_NODE) {
                    Element eElement = (Element) nNode;
                    String strLatLon = eElement.getElementsByTagName("coordinates").item(0).getTextContent().toString();
                    array = strLatLon.split(" ");
                    array2 = new String[array.length];
                    for(int i = 0; i< array.length; i++)
                    {
                    	array2[i]=array[i].replace(',' ,'|');
                    }
                                        
                }
            
        }
        return array2;
    }
}
