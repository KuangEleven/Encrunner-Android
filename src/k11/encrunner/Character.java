package k11.encrunner;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Date;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import android.content.SharedPreferences;
import android.sax.EndTextElementListener;
import android.sax.RootElement;

public class Character {
	public Integer ac;
	public String char_class;
	public Integer char_level;
	public Integer charisma;
	public Integer constitution;
	public Date created_at; //Not Implemented
	public Integer dextarity; //sic
	public String extended_name;
	public Integer fortitude;
	public Integer hp;
	public Integer id;
	public Integer initiative;
	public Integer insight;
	public Integer intelligence;
	public String name;
	public String notes;
	public String owner_name;
	public Integer perception;
	public String race;
	public Integer reflex;
	public Integer speed;
	public Integer strength;
	public Date updated_at; //Not Implemented
	public Integer user_id;
	public Integer will;
	public Integer wisdom;
	
	private SharedPreferences prefs;
	
	/** Initializes a Character to null values (Date fields as current date) */
	Character(SharedPreferences prefs)
	{
		created_at = new Date();
		updated_at = new Date();
		this.prefs = prefs;
	}
	
	/** Initializes a Member with the given id and gets data */
	Character(Integer id,SharedPreferences prefs)
	{
		this(prefs);
		this.id=id;
		Get();
	}
	
	/** Gets Character data from XML, must have id set */ //TODO Enforce id with exception handling
	public void Get()
	{
		//Setup listeners for XML handling
		RootElement memberElement = new RootElement("character");
		
		memberElement.getChild("ac").setEndTextElementListener( //TODO throw all this common code into another class, RubyXMLParser?
				new EndTextElementListener() {
					public void end(String body) {
						if (body != "")
						{
							ac = Integer.valueOf(body);
						}
					}
				});
		
		memberElement.getChild("char-class").setEndTextElementListener(
				new EndTextElementListener() {
					public void end(String body) {
						if (body != "")
						{
							char_class = body;
						}
					}
				});
		
		memberElement.getChild("char-level").setEndTextElementListener(
				new EndTextElementListener() {
					public void end(String body) {
						if (body != "")
						{
							char_level = Integer.valueOf(body);
						}
					}
				});
		
		memberElement.getChild("charisma").setEndTextElementListener( //TODO throw all this common code into another class, RubyXMLParser?
				new EndTextElementListener() {
					public void end(String body) {
						if (body != "")
						{
							charisma = Integer.valueOf(body);
						}
					}
				});
		
		memberElement.getChild("constitution").setEndTextElementListener( //TODO throw all this common code into another class, RubyXMLParser?
				new EndTextElementListener() {
					public void end(String body) {
						if (body != "")
						{
							constitution = Integer.valueOf(body);
						}
					}
				});
		
		memberElement.getChild("dextarity").setEndTextElementListener( //TODO throw all this common code into another class, RubyXMLParser?
				new EndTextElementListener() {
					public void end(String body) {
						if (body != "")
						{
							dextarity = Integer.valueOf(body);
						}
					}
				});
		
		memberElement.getChild("extended-name").setEndTextElementListener( //TODO throw all this common code into another class, RubyXMLParser?
				new EndTextElementListener() {
					public void end(String body) {
						if (body != "")
						{
							extended_name = body;
						}
					}
				});
		
		memberElement.getChild("fortitude").setEndTextElementListener( //TODO throw all this common code into another class, RubyXMLParser?
				new EndTextElementListener() {
					public void end(String body) {
						if (body != "")
						{
							fortitude = Integer.valueOf(body);
						}
					}
				});
		
		memberElement.getChild("hp").setEndTextElementListener( //TODO throw all this common code into another class, RubyXMLParser?
				new EndTextElementListener() {
					public void end(String body) {
						if (body != "")
						{
							hp = Integer.valueOf(body);
						}
					}
				});
		
		memberElement.getChild("initiative").setEndTextElementListener( //TODO throw all this common code into another class, RubyXMLParser?
				new EndTextElementListener() {
					public void end(String body) {
						if (body != "")
						{
							initiative = Integer.valueOf(body);
						}
					}
				});
		
		memberElement.getChild("insight").setEndTextElementListener( //TODO throw all this common code into another class, RubyXMLParser?
				new EndTextElementListener() {
					public void end(String body) {
						if (body != "")
						{
							insight = Integer.valueOf(body);
						}
					}
				});
		
		memberElement.getChild("intelligence").setEndTextElementListener( //TODO throw all this common code into another class, RubyXMLParser?
				new EndTextElementListener() {
					public void end(String body) {
						if (body != "")
						{
							intelligence = Integer.valueOf(body);
						}
					}
				});
		
		memberElement.getChild("name").setEndTextElementListener( //TODO throw all this common code into another class, RubyXMLParser?
				new EndTextElementListener() {
					public void end(String body) {
						if (body != "")
						{
							name = body;
						}
					}
				});
		
		memberElement.getChild("notes").setEndTextElementListener( //TODO throw all this common code into another class, RubyXMLParser?
				new EndTextElementListener() {
					public void end(String body) {
						if (body != "")
						{
							notes = body;
						}
					}
				});
		
		memberElement.getChild("owner-name").setEndTextElementListener( //TODO throw all this common code into another class, RubyXMLParser?
				new EndTextElementListener() {
					public void end(String body) {
						if (body != "")
						{
							owner_name = body;
						}
					}
				});
		
		memberElement.getChild("perception").setEndTextElementListener( //TODO throw all this common code into another class, RubyXMLParser?
				new EndTextElementListener() {
					public void end(String body) {
						if (body != "")
						{
							perception = Integer.valueOf(body);
						}
					}
				});
		
		memberElement.getChild("race").setEndTextElementListener( //TODO throw all this common code into another class, RubyXMLParser?
				new EndTextElementListener() {
					public void end(String body) {
						if (body != "")
						{
							race = body;
						}
					}
				});
		
		memberElement.getChild("reflex").setEndTextElementListener( //TODO throw all this common code into another class, RubyXMLParser?
				new EndTextElementListener() {
					public void end(String body) {
						if (body != "")
						{
							reflex = Integer.valueOf(body);
						}
					}
				});
		
		memberElement.getChild("speed").setEndTextElementListener( //TODO throw all this common code into another class, RubyXMLParser?
				new EndTextElementListener() {
					public void end(String body) {
						if (body != "")
						{
							speed = Integer.valueOf(body);
						}
					}
				});
		
		memberElement.getChild("strength").setEndTextElementListener( //TODO throw all this common code into another class, RubyXMLParser?
				new EndTextElementListener() {
					public void end(String body) {
						if (body != "")
						{
							strength = Integer.valueOf(body);
						}
					}
				});
		
		memberElement.getChild("user-id").setEndTextElementListener( //TODO throw all this common code into another class, RubyXMLParser?
				new EndTextElementListener() {
					public void end(String body) {
						if (body != "")
						{
							user_id = Integer.valueOf(body);
						}
					}
				});
		
		memberElement.getChild("will").setEndTextElementListener( //TODO throw all this common code into another class, RubyXMLParser?
				new EndTextElementListener() {
					public void end(String body) {
						if (body != "")
						{
							will = Integer.valueOf(body);
						}
					}
				});
		
		memberElement.getChild("wisdom").setEndTextElementListener( //TODO throw all this common code into another class, RubyXMLParser?
				new EndTextElementListener() {
					public void end(String body) {
						if (body != "")
						{
							wisdom = Integer.valueOf(body);
						}
					}
				});
		
		//Open HTTP Connection
		HttpURLConnection urlConnection = null;
		try {
		URL url = new URL(Options.getURL(prefs) + "/characters/" + id + ".xml?api_key=2d88e8bcff1615e6c6fab893feba041c053fdbe1"); //TODO Pass in API Key properly
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
}
