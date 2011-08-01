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

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import android.sax.EndElementListener;
import android.sax.EndTextElementListener;
import android.sax.RootElement;
import android.sax.Element;
import android.sax.StartElementListener;

public class Encounter {
	private Integer encID;
	public Integer round;
	public Integer currPosition;
	public ArrayList<Creature> creatureList; //TODO Make private after appropriate accessors are made
	
	private Creature currCreature;
	
	/** Initializes encounter, calls Update */
	Encounter(Integer encID_arg)
	{
		encID = encID_arg;
		creatureList = new ArrayList<Creature>();
		Update();
	}
	
	/** Gets and parses XML from encrunner website */
	public void Update()
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
		
		//Element creatureElementList = root.getChild("members"); //List of creatures
		Element creatureElement = root.getChild("members").getChild("member"); //XML creature entry
		
		creatureElement.setStartElementListener(
				new StartElementListener() {
					public void start(Attributes attributes) {
						currCreature = new Creature();
					}
				});
		
		creatureElement.getChild("id").setEndTextElementListener(
				new EndTextElementListener() {
					public void end(String body) {
						currCreature.id = Integer.valueOf(body);
					}
				});
		
		creatureElement.getChild("character_id").setEndTextElementListener( //TODO Only called if creature is a PC. Should cast to PlayerCharacter
				new EndTextElementListener() {
					public void end(String body) {
						if (body != "")
						{
							//currCreature = (PlayerCharacter)currCreature;
							currCreature.character_id = Integer.valueOf(body);
						}
					}
				});
		
		creatureElement.getChild("monster_id").setEndTextElementListener( //TODO Only called if creature is a NPC. Should cast to NonPlayerCharacter
				new EndTextElementListener() {
					public void end(String body) {
						if (body != "")
						{
							//currCreature = (NonPlayerCharacter)currCreature;
							currCreature.monster_id = Integer.valueOf(body);
						}
					}
				});
		
		creatureElement.getChild("name").setEndTextElementListener(
				new EndTextElementListener() {
					public void end(String body) {
						currCreature.name = body;
					}
				});
		
		creatureElement.getChild("enc_hp").setEndTextElementListener(
				new EndTextElementListener() {
					public void end(String body) {
						currCreature.enc_hp = Integer.valueOf(body);
					}
				});
		
		creatureElement.getChild("position").setEndTextElementListener(
				new EndTextElementListener() {
					public void end(String body) {
						currCreature.position = Integer.valueOf(body); //should always equal index +1 of creatureList
					}
				});
		
		//TODO Handle markers
		
		creatureElement.setEndElementListener(
				new EndElementListener() {
					public void end() {
						creatureList.add(currCreature);
					}
				});
		
		//Open HTTP Connection
		HttpURLConnection urlConnection = null;
		try {
		URL url = new URL("http://encrunner.heroku.com/encounters/" + encID + ".xml?api_key=2d88e8bcff1615e6c6fab893feba041c053fdbe1"); //TODO Pass in API Key properly
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
