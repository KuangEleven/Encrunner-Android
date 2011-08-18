package k11.encrunner;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import android.content.SharedPreferences;
import android.sax.EndTextElementListener;
import android.sax.RootElement;
import android.sax.Element;

public class Encounter { //TODO Add Interface for all these entity classes? Would implement constructor(), constructor(id), Get(),Update(),Create(),Delete()
	private Integer id;
	public Integer round;
	public Integer currPosition;
	private SharedPreferences prefs;
	public ArrayList<Member> members; //TODO Make private after appropriate accessors are made
	
	/** Initialized encounter */
	Encounter(SharedPreferences prefs)
	{
		members = new ArrayList<Member>();
		this.prefs = prefs;
	}
	
	/** Initializes encounter with a given encID */
	Encounter(Integer id,SharedPreferences prefs)
	{
		this(prefs);
		this.id=id;
		Get();
	}
	
	/** Gets Encounter data from XML, must have id set */
	public void Get()
	{
		RootElement root = new RootElement("encounter");
		root.getChild("round").setEndTextElementListener(
				new EndTextElementListener() {
					public void end(String body) {
						round = Integer.valueOf(body);
					}
				});
		root.getChild("curr_pos").setEndTextElementListener(
				new EndTextElementListener() {
					public void end(String body) {
						currPosition = Integer.valueOf(body);
					}
				});
		
		Element creatureElement = root.getChild("members").getChild("member"); //XML creature entry
		
		creatureElement.getChild("id").setEndTextElementListener(
				new EndTextElementListener() {
					public void end(String body) {
						members.add(new Member(Integer.valueOf(body),prefs));
					}
				});
		
		//Open HTTP Connection
		HttpURLConnection urlConnection = null;
		try {
		URL url = new URL(Options.getURL(prefs) + "/encounters/" + id + ".xml?api_key=" + Options.getAPIKey(prefs));
		urlConnection = (HttpURLConnection) url.openConnection();
		InputStream in = new BufferedInputStream(urlConnection.getInputStream());
		
		//XML Parsing

		SAXParser myParser = (SAXParserFactory.newInstance()).newSAXParser();
		myParser.parse(in, (DefaultHandler) root.getContentHandler());
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
