package data.processing;

import java.sql.Timestamp;
import java.util.Map;
import java.util.*;
import java.util.Properties;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hbase.HBaseConfiguration;
import org.apache.hadoop.hbase.client.Get;
import org.apache.hadoop.hbase.client.HConnection;
import org.apache.hadoop.hbase.client.HConnectionManager;
import org.apache.hadoop.hbase.client.HTableInterface;
import org.apache.hadoop.hbase.client.Put;
import org.apache.hadoop.hbase.client.Result;
import org.apache.hadoop.hbase.util.Bytes;
import org.apache.log4j.Logger;
import org.tempuri.ITruckAlertServiceProxy;

import backtype.storm.task.OutputCollector;
import backtype.storm.task.TopologyContext;
import backtype.storm.topology.IRichBolt;
import backtype.storm.topology.OutputFieldsDeclarer;
import backtype.storm.tuple.Fields;
import backtype.storm.tuple.Tuple;
import backtype.storm.tuple.Values;
 import java.io.File;
import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileWriter;
 import java.io.IOException;
 import java.util.Properties;
 import javax.mail.Message;
 import javax.mail.MessagingException;
 import javax.mail.PasswordAuthentication;
 import javax.mail.Session;
 import javax.mail.Transport;
 import javax.mail.internet.InternetAddress;
 import javax.mail.internet.MimeMessage;
 import javax.activation.DataHandler;
 import javax.activation.DataSource;
 import javax.activation.FileDataSource;
 import javax.mail.BodyPart;
 import javax.mail.Message;
 import javax.mail.MessagingException;
 import javax.mail.Multipart;
 import javax.mail.PasswordAuthentication;
 import javax.mail.Session;
 import javax.mail.Transport;
 import javax.mail.internet.InternetAddress;
 import javax.mail.internet.MimeBodyPart;
 import javax.mail.internet.MimeMessage;
 import javax.mail.internet.MimeMultipart;


public class TruckHBaseBolt implements IRichBolt 
{
    private static final long serialVersionUID = 2946379346389650318L;
    private static final Logger LOG = Logger.getLogger(TruckHBaseBolt.class);

    //TABLES
    private static final String EVENTS_TABLE_NAME =  "sensor_events";
    private static final String EVENTS_COUNT_TABLE_NAME = "driver_dangerous_events";

    //CF
    private static final byte[] CF_EVENTS_TABLE = Bytes.toBytes("events");
    private static final byte[] CF_EVENTS_COUNT_TABLE = Bytes.toBytes("count");

    //COL
    private static final byte[] COL_COUNT_VALUE = Bytes.toBytes("value");

    private static final byte[] COL_DRIVER_ID = Bytes.toBytes("d");
    private static final byte[] COL_TRUCK_ID = Bytes.toBytes("t");
    private static final byte[] COL_EVENT_TIME = Bytes.toBytes("tim");
    private static final byte[] COL_EVENT_TYPE = Bytes.toBytes("e");
    private static final byte[] COL_LATITUDE = Bytes.toBytes("la");
    private static final byte[] COL_LONGITUDE = Bytes.toBytes("lo");
	private static final byte[] COL_ISFOGGY = Bytes.toBytes("fog"); 
	private static final byte[] COL_ISRAINY = Bytes.toBytes("rain"); 
	private static final byte[] COL_ISWINDY = Bytes.toBytes("wind");
	private static final byte[] COL_ROUTENAME = Bytes.toBytes("rname");	
	private static final byte[] COL_STATE = Bytes.toBytes("stat");// add here


    private OutputCollector collector;
    private HConnection connection;
    private HTableInterface eventsCountTable;
    private HTableInterface eventsTable;
	static HashMap<String,String> alertFoggyMap = new HashMap<String,String>();
	static HashMap<String,String> alertOverspeed = new HashMap<String,String>();
	static HashMap<String,String> alertBreakDown = new HashMap<String,String>();
    public TruckHBaseBolt(Properties topologyConfig) 
    {

    }

    @Override
    public void prepare(Map stormConf, TopologyContext context,
                    OutputCollector collector) 
    {
        this.collector = collector;
        try 
        {
            this.connection = HConnectionManager.createConnection(constructConfiguration());
            this.eventsCountTable = connection.getTable(EVENTS_COUNT_TABLE_NAME);	
            this.eventsTable = connection.getTable(EVENTS_TABLE_NAME);
        } 
        catch (Exception e) 
        {
            String errMsg = "Error retrievinging connection and access to HBase Tables";
            LOG.error(errMsg, e);
            throw new RuntimeException(errMsg, e);
        }		
    }

    public void sendMail(String msg)
    {
    	Properties props = new Properties();
		props.put("mail.smtp.host", "smtp.gmail.com");
		props.put("mail.smtp.socketFactory.port", "465");
		props.put("mail.smtp.socketFactory.class",
				"javax.net.ssl.SSLSocketFactory");
		props.put("mail.smtp.auth", "true");
		props.put("mail.smtp.port", "465");

		Session session = Session.getDefaultInstance(props,
			new javax.mail.Authenticator() {
				protected PasswordAuthentication getPasswordAuthentication() {
					return new PasswordAuthentication("eshajanani@gmail.com","Esha@1989");
				}
			});

		try {

			Message message = new MimeMessage(session);
			message.setFrom(new InternetAddress("noreply@dunnsolutions.com"));
			message.setRecipients(Message.RecipientType.TO,
					InternetAddress.parse("jeshwaran@dunnsolutions.com"));
			message.setSubject("Truck Alert");
		 message.setText(msg);

			Transport.send(message);
		} catch (MessagingException e) {
			throw new RuntimeException(e);
		}
    }
    
    @Override
    public void execute(Tuple tuple)
    {

        //LOG.info("About to insert tuple["+input +"] into HBase...");

        String driverId = tuple.getStringByField(TruckScheme.FIELD_DRIVER_ID);
        String truckId = tuple.getStringByField(TruckScheme.FIELD_TRUCK_ID);
        Timestamp eventTime = (Timestamp) tuple.getValueByField(TruckScheme.FIELD_EVENT_TIME);
        String eventType = tuple.getStringByField(TruckScheme.FIELD_EVENT_TYPE);
        String longitude = tuple.getStringByField(TruckScheme.FIELD_LONGITUDE);
        String latitude = tuple.getStringByField(TruckScheme.FIELD_LATITUDE);
		String isFoggy = tuple.getStringByField(TruckScheme.FIELD_FOGGY);
		String isRainy = tuple.getStringByField(TruckScheme.FIELD_RAINY);
		String isWindy = tuple.getStringByField(TruckScheme.FIELD_WINDY);
		String routeName = tuple.getStringByField(TruckScheme.FIELD_ROUTENAME);
		String state = tuple.getStringByField(TruckScheme.FIELD_STATE);//ADD HERE
		
        long incidentTotalCount = getInfractionCountForDriver(driverId);

        try 
        {
			//ADD HERE
            Put put = constructRow(EVENTS_TABLE_NAME, driverId, truckId, eventTime, eventType,
                                latitude, longitude,isFoggy,isRainy,isWindy,routeName,state); //add here
            this.eventsTable.put(put);
			

        } 
        catch (Exception e) 
        {
            LOG.error("Error inserting event into HBase table["+EVENTS_TABLE_NAME+"]", e);
        }
		

        if(!eventType.equalsIgnoreCase("normal")) 
        {
            try 
            {
                //long incidentTotalCount = getInfractionCountForDriver(driverId);

                incidentTotalCount = this.eventsCountTable.incrementColumnValue(Bytes.toBytes(driverId), CF_EVENTS_COUNT_TABLE, 
                                                                                                        COL_COUNT_VALUE, 1L);
						BufferedWriter output;
			output = new BufferedWriter(new
			FileWriter("/tmp/alerts/bolt.txt", true));
			output.newLine();
			output.append(tuple.getString(0)+","+tuple.getString(3));
			output.close();																				
                LOG.info("Success inserting event into counts table");

            } 
            catch (Exception e)
            {
               LOG.error("Error inserting violation event into HBase table", e);
            }				
        } 
		
		if(eventType.equalsIgnoreCase("breakdown")) 
        {
		
		if (alertBreakDown.containsKey(truckId))
			{
				
			}
			else
			{
            try 
            {
                //long incidentTotalCount = getInfractionCountForDriver(driverId);

                
			BufferedWriter output1;
			output1 = new BufferedWriter(new
			FileWriter("/tmp/alerts/alertBreakDown.txt", true));
			
			ITruckAlertServiceProxy calService = new ITruckAlertServiceProxy();
			String message1= "Truck number:"+truckId+",has an event :"+eventType+". So kindly expect some delay in delivering your order";
			boolean result =calService.sendPushNotificationMessage(truckId, "breakdown", message1);
			if(result == true)
			{
				BufferedWriter output12;
				output12 = new BufferedWriter(new
				FileWriter("/tmp/alerts/notfiy.txt", true));
				output12.newLine();
				output12.append("Notify result is success");
				output12.close();
			}
			else
			{
				
				BufferedWriter output12;
				output12 = new BufferedWriter(new
				FileWriter("/tmp/alerts/notfiy.txt", true));
				output12.newLine();
				output12.append("Notify result is unsuccessful");
				output12.close();
				
			}
			
			sendMail(message1);
			output1.newLine();
			output1.append("Truck number:"+truckId+",has an event :"+eventType+". So kindly expect some delay in delivering your order");
			output1.close();																				
                LOG.info("Success inserting event into alert into file");
				alertBreakDown.put(truckId,"alertted");

            } 
            catch (Exception e)
            {
               LOG.error("Error inserting violation event into HBase table", e);
            }
		}			
        } 
		if(eventType.equalsIgnoreCase("Overspeed")) 
        {
		
		if (alertOverspeed.containsKey(truckId))
			{
				
			}
			else
			{
            try 
            {
                //long incidentTotalCount = getInfractionCountForDriver(driverId);

                
			BufferedWriter output12;
			output12 = new BufferedWriter(new
			FileWriter("/tmp/alerts/alertOverspeed.txt", true));
			
			
			ITruckAlertServiceProxy calService = new ITruckAlertServiceProxy();
			String message1= "Truck number:"+truckId+",has an event :"+eventType+". Slow down please!!";
			boolean result =calService.sendPushNotificationMessage(truckId, "Overspeed", message1);
			if(result == true)
			{
				BufferedWriter output123;
				output123 = new BufferedWriter(new
				FileWriter("/tmp/alerts/notfiy.txt", true));
				output123.newLine();
				output123.append("Notify result is success from overspeed");
				output123.close();
			}
			else
			{
				
				BufferedWriter output123;
				output123 = new BufferedWriter(new
				FileWriter("/tmp/alerts/notfiy.txt", true));
				output123.newLine();
				output123.append("Notify result is unsuccessful from overspeed");
				output123.close();
				
			}
			
			sendMail(message1);
			
			
			output12.newLine();
			output12.append("Truck number:"+truckId+",has an event :"+eventType+". Sending message to driver :"+driverId+"to slow down.");
			output12.close();																				
                LOG.info("Success inserting event into alert into file");
								alertOverspeed.put(truckId,"alertted");

            } 
            catch (Exception e)
            {
               LOG.error("Error inserting violation event into HBase table", e);
            }	
				}			
        } 
		
		
		if(isFoggy.equalsIgnoreCase("1")) 
        {
			if (alertFoggyMap.containsKey(truckId))
			{
				
			}
			else
			{
            try 
            {
                //long incidentTotalCount = getInfractionCountForDriver(driverId);

                
			BufferedWriter output2;
			String filename = "/tmp/alerts/alertDriverFoggy/"+truckId+".txt";
			output2 = new BufferedWriter(new
			FileWriter("/tmp/alerts/alertDriverFoggy.txt", true));
			
			ITruckAlertServiceProxy calService = new ITruckAlertServiceProxy();
			String message1= "Truck number:"+truckId+",has an event: Foggy Climate ahead. Please be cautious ahead!!";
			boolean result =calService.sendPushNotificationMessage(truckId, "Foggy", message1);
			if(result == true)
			{
				BufferedWriter output123;
				output123 = new BufferedWriter(new
				FileWriter("/tmp/alerts/notfiy.txt", true));
				output123.newLine();
				output123.append("Notify result is success from overspeed");
				output123.close();
			}
			else
			{
				
				BufferedWriter output123;
				output123 = new BufferedWriter(new
				FileWriter("/tmp/alerts/notfiy.txt", true));
				output123.newLine();
				output123.append("Notify result is unsuccessful from overspeed");
				output123.close();
				
			}
			
			sendMail(message1);
			
			
			output2.newLine();
			output2.append("Sending message to Truck number:"+truckId+",to be cautious about foggy climate in this route");
			output2.close();																				
                LOG.info("Success inserting event into alert into file");
				alertFoggyMap.put(truckId,"alertted");
            } 
            catch (Exception e)
            {
               LOG.error("Error inserting violation event into HBase table", e);
            }	
			}// end of else			
        } 
		//add here
        collector.emit(tuple, new Values(driverId, truckId, eventTime, eventType, longitude, latitude, routeName,state,incidentTotalCount));
        //acknowledge even if there is an error
        collector.ack(tuple);
    }


    public static  Configuration constructConfiguration()
    {
        Configuration config = HBaseConfiguration.create();
        return config;
    }	

//Add here
    private Put constructRow(String columnFamily, String driverId, String truckId, 
                Timestamp eventTime, String eventType, String latitude, String longitude,String isFoggy, String isRainy,String isWindy,String routeName,String state) // add here
    {

        String rowKey = consructKey(driverId, truckId, eventTime);
        Put put = new Put(Bytes.toBytes(rowKey));

        put.add(CF_EVENTS_TABLE, COL_DRIVER_ID, Bytes.toBytes(driverId));
        put.add(CF_EVENTS_TABLE, COL_TRUCK_ID, Bytes.toBytes(truckId));

        long eventTimeValue=  eventTime.getTime();
        put.add(CF_EVENTS_TABLE, COL_EVENT_TIME, Bytes.toBytes(eventTimeValue));
        put.add(CF_EVENTS_TABLE, COL_EVENT_TYPE, Bytes.toBytes(eventType));
        put.add(CF_EVENTS_TABLE, COL_LATITUDE, Bytes.toBytes(latitude));
        put.add(CF_EVENTS_TABLE, COL_LONGITUDE, Bytes.toBytes(longitude));
		put.add(CF_EVENTS_TABLE, COL_ISFOGGY, Bytes.toBytes(isFoggy));
		put.add(CF_EVENTS_TABLE, COL_ISRAINY, Bytes.toBytes(isRainy));
		put.add(CF_EVENTS_TABLE, COL_ISWINDY, Bytes.toBytes(isWindy)); 
		put.add(CF_EVENTS_TABLE, COL_ROUTENAME, Bytes.toBytes(routeName));
		put.add(CF_EVENTS_TABLE, COL_STATE, Bytes.toBytes(state));// add here
        return put;
    }


    private String consructKey(String driverId, String truckId, Timestamp ts2)
    {
        long reverseTime = Long.MAX_VALUE - ts2.getTime();
        String rowKey = driverId+"|"+truckId+"|"+reverseTime;
        return rowKey;
    }	


    @Override
    public void cleanup() 
    {
        try 
        {
                eventsCountTable.close();
                eventsTable.close();
                connection.close();
        } 
        catch (Exception  e) 
        {
                LOG.error("Error closing connections", e);
        }
    }

    @Override  //add here
    public void declareOutputFields(OutputFieldsDeclarer declarer) {
            declarer.declare(new Fields(TruckScheme.FIELD_DRIVER_ID, 
                    TruckScheme.FIELD_TRUCK_ID, TruckScheme.FIELD_EVENT_TIME, 
                    TruckScheme.FIELD_EVENT_TYPE, TruckScheme.FIELD_LONGITUDE, 
                    TruckScheme.FIELD_LATITUDE, TruckScheme.FIELD_FOGGY,
					TruckScheme.FIELD_RAINY,TruckScheme.FIELD_WINDY,TruckScheme.FIELD_ROUTENAME,TruckScheme.FIELD_STATE,
					TruckScheme.FIELD_INCIDENT_CNT));
    }

    @Override
    public Map<String, Object> getComponentConfiguration()
    {
            return null;
    }

    private long getInfractionCountForDriver(String driverId)
    {

        try 
        {
            byte[] driver = Bytes.toBytes(driverId);
            Get get = new Get(driver);
            Result result = eventsCountTable.get(get);
            long count = 0;
            if(result != null) 
            {
                byte[] countBytes = result.getValue(CF_EVENTS_COUNT_TABLE, COL_COUNT_VALUE);
                if(countBytes != null) 
                {
                    count = Bytes.toLong(countBytes);
                }
            }
            return count;
        } 
        catch (Exception e) 
        {
            LOG.error("Error getting infraction count", e);
            throw new RuntimeException("Error getting infraction count");
        }
    }	
}
