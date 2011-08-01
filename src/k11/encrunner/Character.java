package k11.encrunner;

import android.app.Activity;
import android.os.Bundle;
import android.text.Editable;
import android.view.LayoutInflater;
import android.widget.EditText;
import android.widget.TextView;

public class Character extends Activity {
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.character);
		
		//TextView debugTextView = (TextView)MainMenu.findViewById(R.id.encID_text);
		
		
		Encounter encounter  = new Encounter(65);
		TextView debugTextView = (TextView) findViewById(R.id.character_content);
		debugTextView.setText(String.valueOf(encounter.creatureList.get(0).name));
	}
}
