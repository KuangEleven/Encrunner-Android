package k11.encrunner;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.sax.EndTextElementListener;
import android.sax.RootElement;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.Toast;

public class Login extends Activity implements OnClickListener {
	private SharedPreferences sharedPreferences;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.login);
		
		sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
		
        View loginButton = findViewById(R.id.login_button);
        loginButton.setOnClickListener(this);
	}

	public void onClick(View v)
	{
		//Setup textboxes
		EditText username = (EditText) findViewById(R.id.username_text);
		EditText password = (EditText) findViewById(R.id.password_text);
		
		ConnectionTask task = new ConnectionTask(username.getText().toString(),password.getText().toString());
		task.execute(Options.getURL(sharedPreferences));
	}
	
	class ConnectionTask extends AsyncTask<String, Void, String> {
		private String username;
		private String password;
		private String apiKeyOutput;
		
		public ConnectionTask(String username,String password) {
			this.username = username;
			this.password = password;
		}
		
		@Override
		//Network code, run in separate thread
		protected String doInBackground(String... params) {
			apiKeyOutput = null;
			RootElement root = new RootElement("user");
			root.getChild("api_key").setEndTextElementListener(
					new EndTextElementListener() {
						public void end(String body) {
							//Options.setAPIKey(body,sharedPreferences);
							//Toast.makeText(getApplicationContext(), "Login Successful", Toast.LENGTH_SHORT).show();
							apiKeyOutput=body;
						}
					});
			
			//Open HTTP Connection
			HttpURLConnection urlConnection = null;
			try {
			URL url = new URL(params[0] + "/login/get_api_key.xml?login=" + username + "&password=" + password);
			Log.d("URL", url.toString());
			urlConnection = (HttpURLConnection) url.openConnection();
			InputStream in = new BufferedInputStream(urlConnection.getInputStream());
			
			//XML Parsing

			SAXParser myParser = (SAXParserFactory.newInstance()).newSAXParser();
			myParser.parse(in, (DefaultHandler) root.getContentHandler());
			}catch (ParserConfigurationException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				return null;
			} catch (SAXException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				return null;
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				//Toast.makeText(getApplicationContext(), "Bad Login", Toast.LENGTH_SHORT).show();
				return null;
			}
		    finally {
			     urlConnection.disconnect();
			    }
			return apiKeyOutput; //Here, I really hope that my Listeners have triggered, as I believe that parse will block until it has triggered all listeners
		}
		
		@Override
		//After network code ran, run in UI thread
		protected void onPostExecute(String apiKey) {
			if (apiKey == null) {
				Toast.makeText(getApplicationContext(), "Bad Login", Toast.LENGTH_SHORT).show();
			}
			else {
				Options.setAPIKey(apiKey,sharedPreferences);
				Toast.makeText(getApplicationContext(), "Login Successful", Toast.LENGTH_SHORT).show();
				Log.d("API Key", apiKey);
				finish();
			}
		}
	}
}