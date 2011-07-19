package k11.encrunner;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;

import android.app.Activity;
import android.os.Bundle;
import android.widget.TextView;
import android.sax.*;
import javax.xml.parsers.*;

import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import java.net.Authenticator;
import java.net.HttpURLConnection;
import java.net.PasswordAuthentication;
import java.net.URL;

public class Character extends Activity {
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.character);
		RootElement root = new RootElement("note");
		root.getChild("to").setEndTextElementListener(
				new EndTextElementListener() {
					public void end(String body) {
						TextView debugTextView = (TextView) findViewById(R.id.character_content);
						debugTextView.setText(body);
					}
				});
		
//		   Authenticator.setDefault(new Authenticator() {
//			     protected PasswordAuthentication getPasswordAuthentication() {
//			       return new PasswordAuthentication("bay", "bay".toCharArray());
//			     };
//			     }
//		   );
		
		//Open HTTP Connection
		HttpURLConnection urlConnection = null;
		try {
		URL url = new URL("http://www.xmlfiles.com/examples/note.xml");
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
