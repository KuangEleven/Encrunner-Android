package k11.encrunner;

import java.io.IOException;
import java.io.UnsupportedEncodingException;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

public class Character extends Activity {
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.character);
		
		//Posting XML Test
		//String xml = "character[name]=KelithYYY";
		//StringEntity se = null;
		//try {
		//	se = new StringEntity(xml,"UTF-8");
		//} catch (UnsupportedEncodingException e) {
		//	// TODO Auto-generated catch block
		//	e.printStackTrace();
		//}
		//se.setContentType("application/rest+xml");
		HttpPut putRequest = new HttpPut("http://encrunner.heroku.com/encounters/65.xml?api_key=7686f5c264fa75e2db2816ab212ff95c16b2f2cf&encounter[round]=1");
		//putRequest.setEntity(se);
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
		Log.d("HTTPResponse", String.valueOf(response.getStatusLine().getStatusCode()));
		
		SharedPreferences mPrefs = getSharedPreferences("prefs",0);
		Encounter encounter  = new Encounter(mPrefs.getInt("encID", 0));
		TextView debugTextView = (TextView) findViewById(R.id.character_content);
		debugTextView.setText(String.valueOf(encounter.creatureList.get(0).name));
	}
}
