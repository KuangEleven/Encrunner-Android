package k11.encrunner;

import android.app.Activity;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;

public class MainMenu extends Activity implements OnClickListener {
    //private SharedPreferences mPrefs;
    private EditText encIDTextView;
	
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        
        encIDTextView = (EditText) findViewById(R.id.encID_text);
        //SharedPreferences sharedPreference = PreferenceManager.getDefaultSharedPreferences(this);
        if (Options.getEncID(PreferenceManager.getDefaultSharedPreferences(this)) != 0)
        	encIDTextView.setText(String.valueOf(Options.getEncID(PreferenceManager.getDefaultSharedPreferences(this))));
        
        //Click listeners
        View partyButton = findViewById(R.id.party_button);
        partyButton.setOnClickListener(this);
        View characterButton = findViewById(R.id.character_button);
        characterButton.setOnClickListener(this);
        View tableButton = findViewById(R.id.table_button);
        tableButton.setOnClickListener(this);
        View scoreboardButton = findViewById(R.id.scoreboard_button);
        scoreboardButton.setOnClickListener(this);
        View optionsButton = findViewById(R.id.options_button);
        optionsButton.setOnClickListener(this);
        View aboutButton = findViewById(R.id.about_button);
        aboutButton.setOnClickListener(this);
        View loginButton = findViewById(R.id.login_button);
        loginButton.setOnClickListener(this);
    }
    
    public void onPause()
    {
    	super.onPause();
    	
    	Options.setEncID(Integer.valueOf(encIDTextView.getText().toString()),PreferenceManager.getDefaultSharedPreferences(this));
    	
//        SharedPreferences.Editor ed = mPrefs.edit();
//        ed.putInt("encID", Integer.valueOf(encIDTextView.getText().toString()));
//        ed.commit();

    }
    
    public void onClick(View v)
    {
    	Intent i;
    	switch (v.getId())
    	{
    	case R.id.about_button:
    		i = new Intent(this, About.class);
    		startActivity(i);
    		break;
    	case R.id.character_button:
    		i = new Intent(this, CharacterView.class);
    		startActivity(i);
    		break;
    	case R.id.options_button:
    		i = new Intent(this, Options.class);
    		startActivity(i);
    		break;
    	case R.id.login_button:
    		i = new Intent(this, Login.class);
    		startActivity(i);
    		break;
//    	case R.id.party_button:
//    		i = new Intent(this, CharacterSelect.class);
//    		String[] characterArray = new String[] {"Kelith","Baern","Mubb","Shifty Githyanki"};
//    		i.putExtra("k11.encrunner.characterArray", characterArray);
//    		startActivityForResult(i, 0);
//    		Log.d("TEST","Returned properly");
//    		break;
    	}
    }
}