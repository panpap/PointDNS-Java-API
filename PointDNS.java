package anondns;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.SignatureException;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;

import org.bouncycastle.util.encoders.Base64;

public class PointDNS {
	
	private final String apikey;
	private final String username;

	public PointDNS(String user,String apiKey){
		this.apikey=apiKey;
		this.username=user;
	}
	
    public boolean deleteRecord(String zone,Long recordID){	
    	HttpURLConnection connection=null;
    	try {
	   // Connect to DynDNS
	        connection =initializeHttp(getURL(zone,recordID),"DELETE");
	        OutputStream os = connection.getOutputStream();
	        String update="";
	        os.write(update.getBytes());
	        os.flush();
	
	    // Get Response
	        int responseCode = connection.getResponseCode();
	        BufferedReader br = new BufferedReader(new InputStreamReader((connection.getInputStream())));
	
	    //Print response
	        String output;
	        while ((output = br.readLine()) != null) {
	            if ((responseCode>=200)&&(responseCode<=202)) //succesfull request
	            {
	                String str=output.split("status")[1].split(":")[1].split("}")[0].replace("\"","");	
	                if (str=="OK")
	                	return true;
	            }
	            else
	            	return false;
	        }
			return false;
        } 
        catch (IOException ex) {
            ex.printStackTrace();
        }
        finally {
            if(connection != null) {
                connection.disconnect(); 
            }
        }
    	return false;
    }
	
    private URL getURL(String zone,long recordID) throws MalformedURLException
    {
    	if(zone!=null){
	    	if (recordID==-1)
	    		return new URL("https://pointhq.com/zones/"+zone+"/records/");
	    	else if (recordID==-2)
	    		return new URL("https://pointhq.com/zones/"+zone);
	    	else
	    		return new URL("https://pointhq.com/zones/"+zone+"/records/"+recordID);
    	}
    	else
    		return new URL("https://pointhq.com/zones/");
    }
    
    
    public String getZone(String zone) throws MalformedURLException
    {
    	return pointdnsGET(getURL(zone,-2));
    }
  
    public String getZones() throws MalformedURLException
    {
    	return pointdnsGET(getURL(null,-1));
    }
    
    public String getZoneRecords(String zone) throws MalformedURLException
    {
    	return pointdnsGET(getURL(zone,-1));
    }
    
    public String getRecord(String zone,Long recordID) throws MalformedURLException
    {
    	return pointdnsGET(getURL(zone,recordID));
    }
    
    public boolean updateRecord(String zone,String name,Long recordID,String data,Long ttl) throws InvalidKeyException, SignatureException, NoSuchPaddingException, NoSuchAlgorithmException, IllegalBlockSizeException, BadPaddingException, NoSuchProviderException
    {
        HttpURLConnection connection=null;
        try {
        // Connect to DynDNS
            connection = initializeHttp(getURL(zone,recordID),"PUT");
            OutputStream os = connection.getOutputStream();
            String update="{\"zone_record\":{\"name\":\""+name+"\",\"data\":\""+data+"\",\"ttl\":\""+ttl+"\"}}";
            os.write(update.getBytes());
            os.flush();

       //Print response
            int responseCode = connection.getResponseCode();
            if ((responseCode>=200)&&(responseCode<=202)) //succesfull request
                return true;
            else
            {  
            	System.err.println(responseCode+" "+connection.getResponseMessage());	
            	return false;
            }
        } 
        catch (IOException ex) {
            ex.printStackTrace();
        }
        finally {
            if(connection != null) {
                connection.disconnect(); 
            }
        }
        return false;
    }
    
    private HttpURLConnection initializeHttp(URL url, String Method) throws IOException
    {
    // Connect
    	String credentials=this.username+":"+this.apikey;
    	HttpURLConnection connection = (HttpURLConnection)url.openConnection();
        String encodedAuthorization = Base64.toBase64String(credentials.getBytes());
        connection.setRequestProperty("Authorization", "Basic "+encodedAuthorization);

    //HTTP Headers
        connection.setRequestMethod(Method);
        connection.setRequestProperty("User-Agent", "anonDNS client");
        connection.setRequestProperty("Accept", "application/json");
        connection.setRequestProperty("Content-type","application/json");

    //PUT parameters
        connection.setDoInput(true);
        connection.setDoOutput(true);
        connection.connect();
    	return connection;
    }
    
    //returns the new records id
    public long createRecord(String zone,String name,String data,String type,long ttl) {
    	HttpURLConnection connection=null;
        try {
            connection=initializeHttp(getURL(zone,-1),"POST");
            OutputStream os = connection.getOutputStream();
            String create="{\"zone_record\":{\"name\":\""+name+"\",\"record_type\":\""+type+"\",\"data\":\""+data+"\",\"ttl\":\""+ttl+"\"}}";
            os.write(create.getBytes());
            os.flush();

        // Get Response
            int responseCode = connection.getResponseCode();
            BufferedReader br = new BufferedReader(new InputStreamReader((connection.getInputStream())));
        //Print response
            String output;
            String id = "-1";
            while ((output = br.readLine()) != null) {
            	if((responseCode>=200)&&(responseCode<=202))
            	{
            		System.out.println(output);
            		id=output.split("id")[1].split(":")[1].split(",")[0].replace("\"","");
            	}
            	else
            	{
            		System.err.println(responseCode+" "+connection.getResponseMessage());
            		return -1;
            	}
            }
            return Long.parseLong(id);
        } 
        catch (Exception ex) {
            ex.printStackTrace(); 
        }
        finally {
            if(connection != null) {
                connection.disconnect(); 
            }        
        }
        return -1;
    }
    
    private String pointdnsGET(URL url)
    {
	    HttpURLConnection connection=null;
	    try{
	   // Connect to DynDNS
	        connection = initializeHttp(url,"GET");
	    // Get Response
	        int responseCode = connection.getResponseCode();
	        BufferedReader br = new BufferedReader(new InputStreamReader((connection.getInputStream())));
	
	    //Print response
	        String  str="";
	        String output;
	        while ((output = br.readLine()) != null) {
                if ((responseCode>=200)&&(responseCode<=202)) //succesfull request
                	str+=output;
	        }
	        return str;
        } 
        catch (Exception ex) {
            ex.printStackTrace();
        }
        finally {
            if(connection != null) {
                connection.disconnect(); 
            } 
        }
	    return null;
    }
}
