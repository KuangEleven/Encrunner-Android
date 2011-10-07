package k11.encrunner;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Date;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import android.content.SharedPreferences;
import android.sax.EndTextElementListener;
import android.sax.RootElement;

public class Marker {
	public String color; //Make actual color type of some sort
	public Date created_at; //Not Implemented
	public String effect;
	public Integer id;
	public Integer member_id;
	public Integer round_added;
	public Integer source_member_id;
	public Boolean sustains;
	public Date updated_at; //Not Implemented
	
	private SharedPreferences prefs;
	
	/** Initializes a Character to null values (Date fields as current date) */
	Marker(SharedPreferences prefs)
	{
		created_at = new Date();
		updated_at = new Date();
		this.prefs = prefs;
	}
	
	/** Initializes a Member with the given id and gets data */
	Marker(Integer id,SharedPreferences prefs)
	{
		this(prefs);
		this.id=id;
		Get();
	}
	
	/** Gets Marker data from XML, must have id set */ //TODO Enforce id with exception handling
	public void Get()
	{
		//Setup listeners for XML handling
		RootElement memberElement = new RootElement("character");
		
		memberElement.getChild("color").setEndTextElementListener(
				new EndTextElementListener() {
					public void end(String body) {
						if (body != "")
						{
							color = body;
						}
					}
				});
		
		memberElement.getChild("effect").setEndTextElementListener(
				new EndTextElementListener() {
					public void end(String body) {
						if (body != "")
						{
							effect = body;
						}
					}
				});
		
		memberElement.getChild("member-id").setEndTextElementListener(
				new EndTextElementListener() {
					public void end(String body) {
						if (body != "")
						{
							member_id = Integer.valueOf(body);
						}
					}
				});
		
		memberElement.getChild("round-added").setEndTextElementListener( //TODO throw all this common code into another class, RubyXMLParser?
				new EndTextElementListener() {
					public void end(String body) {
						if (body != "")
						{
							round_added = Integer.valueOf(body);
						}
					}
				});
		
		memberElement.getChild("source-member-id").setEndTextElementListener( //TODO throw all this common code into another class, RubyXMLParser?
				new EndTextElementListener() {
					public void end(String body) {
						if (body != "")
						{
							source_member_id = Integer.valueOf(body);
						}
					}
				});
		
		memberElement.getChild("sustains").setEndTextElementListener( //TODO throw all this common code into another class, RubyXMLParser?
				new EndTextElementListener() {
					public void end(String body) {
						if (body != "")
						{
							sustains = Boolean.valueOf(body);
						}
					}
				});
		
		//Open HTTP Connection
		HttpURLConnection urlConnection = null;
		try {
		URL url = new URL(Options.getURL(prefs) + "/markers/" + id + ".xml?api_key=" + Options.getAPIKey(prefs));
		urlConnection = (HttpURLConnection) url.openConnection();
		InputStream in = new BufferedInputStream(urlConnection.getInputStream());
		
		//XML Parsing

		SAXParser myParser = (SAXParserFactory.newInstance()).newSAXParser();
		myParser.parse(in, (DefaultHandler) memberElement.getContentHandler());
		}catch (ParserConfigurationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SAXException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	    finally {
		     urlConnection.disconnect();
		    }
	}
	
	/**Updates Marker XML with current object data */ //TODO Requires id
	public void Update()
	{
		String xml = "<marker>";
		xml += "<color>" + color + "</color>";
		xml += "<effect>" + effect + "</effect>";
		xml += "<member-id>" + member_id + "</member-id>";
		xml += "<round-added>" + round_added + "</round-added>";
		xml += "<source-member-id>" + source_member_id + "</source-member-id>";
		xml += "<sustains>" + sustains + "</sustains>";
		xml += "</marker>";
		StringEntity se = null;
		try {
			se = new StringEntity(xml,"UTF-8");
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		se.setContentType("application/xml");
		HttpPut putRequest = new HttpPut(Options.getURL(prefs) + "/markers/" + id + ".xml?api_key=" + Options.getAPIKey(prefs));
		putRequest.setEntity(se);
		HttpClient httpclient = new DefaultHttpClient();
		HttpResponse response = null;
		try {
			response = httpclient.execute(putRequest);
		} catch (ClientProtocolException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
