package k11.encrunner;

import java.util.ArrayList;
import android.app.Activity;
import android.app.ListActivity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.TextView;

public class CharacterView extends ListActivity implements OnClickListener {
	private Member currMember;
	private Character currCharacter;
	private ArrayList<Integer> idArray;
	private SharedPreferences prefs;
	private Handler refreshHandler = new Handler();
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.character);
		
		prefs = PreferenceManager.getDefaultSharedPreferences(this);
		
		//Click Listeners
        View plusButton = findViewById(R.id.character_plus_button);
        plusButton.setOnClickListener(this);
        View minusButton = findViewById(R.id.character_minus_button);
        minusButton.setOnClickListener(this);
		
		Encounter encounter  = new Encounter(Options.getEncID(PreferenceManager.getDefaultSharedPreferences(this)),PreferenceManager.getDefaultSharedPreferences(this));
		ArrayList<String> nameArray = new ArrayList<String>();
		idArray = new ArrayList<Integer>();
		//Log.d("TEST",Integer.toString(Options.getEncID(PreferenceManager.getDefaultSharedPreferences(this))));
		for (Member iterMember : encounter.members)
		{
			if (iterMember.character_id != null)
			{
				nameArray.add(iterMember.name);
				idArray.add(iterMember.id);
				//Log.d("TEST",iterMember.name);
			}
		}
		
		Intent i = new Intent(this, CharacterSelect.class);
		String[] characterArray = nameArray.toArray(new String[nameArray.size()]);
		i.putExtra("k11.encrunner.characterArray", characterArray);
		startActivityForResult(i, 0);
		//Log.d("TEST","Returned properly");
		
		
		//TextView debugTextView = (TextView) findViewById(R.id.character_content);
		//debugTextView.setText(String.valueOf(encounter.members.get(0).name));
	}

	protected void onActivityResult(int requestCode, int resultCode, Intent data)
	{
		if (resultCode == RESULT_CANCELED)
		{
			finish();
		}
		else
		{
			currMember = new Member(idArray.get(data.getExtras().getInt("k11.encrunner.characterPosition")),PreferenceManager.getDefaultSharedPreferences(this));
			
			currCharacter = new Character(currMember.character_id,PreferenceManager.getDefaultSharedPreferences(this));
			((TextView) findViewById(R.id.character_hp)).setText(String.valueOf(currCharacter.hp));
			((TextView) findViewById(R.id.character_ac)).setText(String.valueOf(currCharacter.ac));
			((TextView) findViewById(R.id.character_insight)).setText(String.valueOf(currCharacter.insight));
			((TextView) findViewById(R.id.character_speed)).setText(String.valueOf(currCharacter.speed));
			((TextView) findViewById(R.id.character_fort)).setText(String.valueOf(currCharacter.fortitude));
			((TextView) findViewById(R.id.character_perception)).setText(String.valueOf(currCharacter.perception));
			((TextView) findViewById(R.id.character_init)).setText(String.valueOf(currCharacter.initiative));
			((TextView) findViewById(R.id.character_reflex)).setText(String.valueOf(currCharacter.reflex));
			((TextView) findViewById(R.id.character_will)).setText(String.valueOf(currCharacter.will));
			refresh();
			refreshHandler.postDelayed(refreshTimeTask, Options.getRefresh(prefs));
		}
	}
	
	private Runnable refreshTimeTask = new Runnable()
	{
		   public void run() {
		      refresh();
		      refreshHandler.postDelayed(refreshTimeTask, Options.getRefresh(prefs));
		   }
	};
		
	public void onPause()
	{
		super.onPause();
		refreshHandler.removeCallbacks(refreshTimeTask);
	}
	
	private void refresh()
	{
		currMember.Get();
		((TextView) findViewById(R.id.character_name)).setText(String.valueOf(currMember.name));
		((TextView) findViewById(R.id.character_enchp)).setText(String.valueOf(currMember.enc_hp));
		//Setup List View
		//This dual array hack irks me...
		ArrayList<Integer> markerIDArray = new ArrayList<Integer>();
		ArrayList<String> markerNameArray = new ArrayList<String>();
		for (Marker iterMarker : currMember.markers)
		{
				markerNameArray.add(iterMarker.effect);
				markerIDArray.add(iterMarker.id);
		}
		setListAdapter(new ArrayAdapter<String>(this, R.layout.listitem,markerNameArray));
	}

	@Override
	public void onClick(View v) 
	{
    	switch (v.getId())
    	{
	    	case R.id.character_plus_button:
	    		if (((TextView) findViewById(R.id.character_change)).getText().toString().length() > 0)
	    		{
		    		currMember.enc_hp += Integer.valueOf(((EditText) findViewById(R.id.character_change)).getText().toString());
		    		currMember.Update();
		    		((TextView) findViewById(R.id.character_change)).setText("");
		    		refresh();
	    		}
	    		break;
	    	case R.id.character_minus_button:
	    		if (((TextView) findViewById(R.id.character_change)).getText().toString().length() > 0)
	    		{
		    		currMember.enc_hp -= Integer.valueOf(((EditText) findViewById(R.id.character_change)).getText().toString());
		    		currMember.Update();
		    		((TextView) findViewById(R.id.character_change)).setText("");
		    		refresh();
	    		}
	    		break;
    	}
	}
}