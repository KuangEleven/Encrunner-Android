package k11.encrunner;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.TextView;

public class CharacterView extends Activity implements OnClickListener {
	private Member currMember;
	private Character currCharacter;
	private ArrayList<Integer> idArray;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.character);
		
		//Click Listeners
        View partyButton = findViewById(R.id.character_refresh_button);
        partyButton.setOnClickListener(this);
        View characterButton = findViewById(R.id.character_plus_button);
        characterButton.setOnClickListener(this);
        View tableButton = findViewById(R.id.character_minus_button);
        tableButton.setOnClickListener(this);
		
		Encounter encounter  = new Encounter(Options.getEncID(PreferenceManager.getDefaultSharedPreferences(this)),PreferenceManager.getDefaultSharedPreferences(this));
		ArrayList<String> nameArray = new ArrayList<String>();
		idArray = new ArrayList<Integer>();
		for (Member iterMember : encounter.members)
		{
			if (iterMember.character_id != null)
			{
				nameArray.add(iterMember.name);
				idArray.add(iterMember.id);
			}
		}
		
		Log.d("TEST",String.valueOf(nameArray.size()));
		
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
		currMember = new Member(idArray.get(data.getExtras().getInt("k11.encrunner.characterPosition")),PreferenceManager.getDefaultSharedPreferences(this));
		currCharacter = new Character(currMember.character_id,PreferenceManager.getDefaultSharedPreferences(this));
		Log.d("TEST",currMember.name + String.valueOf(currMember.id));
		refresh();
	}

	private void refresh()
	{
		Log.d("TEST","REFRESH");
		currMember.Update();
		((TextView) findViewById(R.id.character_name)).setText(String.valueOf(currMember.name));
		((TextView) findViewById(R.id.character_enchp)).setText(String.valueOf(currMember.enc_hp));
	}

	@Override
	public void onClick(View v) 
	{
    	switch (v.getId())
    	{
	    	case R.id.character_refresh_button:
	    		refresh();
	    		break;
	    	case R.id.character_plus_button:
	    		currMember.enc_hp += Integer.valueOf(((EditText) findViewById(R.id.character_change)).getText().toString());
	    		currMember.Update();
	    		((TextView) findViewById(R.id.character_change)).setText("");
	    		refresh();
	    		break;
	    	case R.id.character_minus_button:
	    		currMember.enc_hp -= Integer.valueOf(((EditText) findViewById(R.id.character_change)).getText().toString());
	    		currMember.Update();
	    		((TextView) findViewById(R.id.character_change)).setText("");
	    		refresh();
	    		break;
    	}
	}
}