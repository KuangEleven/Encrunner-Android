package k11.encrunner;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceActivity;
import android.preference.PreferenceManager;

public class Options extends PreferenceActivity {
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		addPreferencesFromResource(R.xml.options);
	}
	
	public static Integer getRefresh(SharedPreferences sharedPreferences)
	{
		return Integer.valueOf(sharedPreferences.getString("refresh", "100"));
	}
	
	public static String getURL(SharedPreferences sharedPreferences)
	{
		return sharedPreferences.getString("url", "http://encrunner.heroku.com");
	}
	
	public static Integer getEncID(SharedPreferences sharedPreferences)
	{
		return sharedPreferences.getInt("encID", 0);
	}
	
	public static void setEncID(Integer encID,SharedPreferences sharedPreferences)
	{
        SharedPreferences.Editor ed = sharedPreferences.edit();
        ed.putInt("encID", encID);
        ed.commit();
	}
}
