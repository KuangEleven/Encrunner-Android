package k11.encrunner;

import android.app.Activity;
import android.os.Bundle;
import android.content.Intent;
import android.view.View;
import android.view.View.OnClickListener;

public class MainMenu extends Activity implements OnClickListener {
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        
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
    }
    
    public void onClick(View v) {
    	switch (v.getId()) {
    	case R.id.about_button:
    		Intent i = new Intent(this, About.class);
    		startActivity(i);
    		break;
    	}
    }
}