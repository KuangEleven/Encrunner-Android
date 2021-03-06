package k11.encrunner;

import java.util.ArrayList;
import android.app.ListActivity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
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
	private Encounter encounter;
	private Boolean keepRefresh;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.character);
		
		prefs = PreferenceManager.getDefaultSharedPreferences(this);
		
		keepRefresh = true;
		
		//Click Listeners
        View plusButton = findViewById(R.id.character_plus_button);
        plusButton.setOnClickListener(this);
        View minusButton = findViewById(R.id.character_minus_button);
        minusButton.setOnClickListener(this);
		
		encounter  = new Encounter(PreferenceManager.getDefaultSharedPreferences(this));
		encounter.id=Options.getEncID(PreferenceManager.getDefaultSharedPreferences(this));
		
		EncounterGetTask task = new EncounterGetTask(this);
		task.execute(encounter);
	}
	
	protected void onDestroy() {
		super.onDestroy();
		refreshHandler.removeCallbacks(refreshTimeTask);
		keepRefresh=false;
	}

	protected void onActivityResult(int requestCode, int resultCode, Intent data)
	{
		if (resultCode == RESULT_CANCELED)
		{
			finish();
		}
		else
		{
			MemberInitializeTask task = new MemberInitializeTask(this);
			task.execute(idArray.get(data.getExtras().getInt("k11.encrunner.characterPosition")));
		}
	}
	
	private Runnable refreshTimeTask = new Runnable()
	{
		   public void run() {
		      refresh();
		      //refreshHandler.postDelayed(refreshTimeTask, Options.getRefresh(prefs));
		   }
	};
		
	public void onPause()
	{
		super.onPause();
		refreshHandler.removeCallbacks(refreshTimeTask);
	}
	
	private void refresh()
	{
		MemberGetTask task = new MemberGetTask(this);
		//task.executeOnExecutor(SERIAL_EXECUTOR,currMember);
		task.execute(currMember);
	}

	@Override
	public void onClick(View v) 
	{
    	switch (v.getId())
    	{
	    	case R.id.character_plus_button:
	    		if (((TextView) findViewById(R.id.character_change)).getText().toString().length() > 0)
	    		{
		    		MemberUpdateTask task = new MemberUpdateTask(currMember);
		    		task.execute(Integer.valueOf(((EditText) findViewById(R.id.character_change)).getText().toString()));
		    		((TextView) findViewById(R.id.character_change)).setText("");
	    		}
	    		break;
	    	case R.id.character_minus_button:
	    		if (((TextView) findViewById(R.id.character_change)).getText().toString().length() > 0)
	    		{
		    		MemberUpdateTask task = new MemberUpdateTask(currMember);
		    		task.execute(-1 * Integer.valueOf(((EditText) findViewById(R.id.character_change)).getText().toString()));
		    		((TextView) findViewById(R.id.character_change)).setText("");
	    		}
	    		break;
    	}
	}
	
	class EncounterGetTask extends AsyncTask<Encounter, Void, Encounter> {
		private Encounter encounterCopy;
		private Context currContext;
		
		public EncounterGetTask(Context currContext) {
			this.currContext = currContext;
		}
		
		@Override
		//Network code, run in separate thread
		protected Encounter doInBackground(Encounter... params) {
			encounterCopy = params[0]; //Not sure if required
			encounterCopy.Get();
			return encounterCopy;
		}
		
		@Override
		//After network code ran, run in UI thread
		protected void onPostExecute(Encounter encounterNetwork) {
			encounter = encounterNetwork;
			
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
			
			Intent i = new Intent(currContext, CharacterSelect.class);
			String[] characterArray = nameArray.toArray(new String[nameArray.size()]);
			i.putExtra("k11.encrunner.characterArray", characterArray);
			startActivityForResult(i, 0);
		}
	}
	
	class MemberGetTask extends AsyncTask<Member, Void, Member> {
		private Member MemberCopy;
		private Context currContext;
		
		public MemberGetTask(Context currContext) {
			this.currContext = currContext;
		}
		
		@Override
		//Network code, run in separate thread
		protected Member doInBackground(Member... params) {
			MemberCopy = params[0].clone();
			MemberCopy.Get();
			return MemberCopy;
		}
		
		@Override
		//After network code ran, run in UI thread
		protected void onPostExecute(Member memberNetwork) {
			currMember = memberNetwork;
			((TextView) findViewById(R.id.character_name)).setText(String.valueOf(currMember.name));
			((TextView) findViewById(R.id.character_enchp)).setText(String.valueOf(currMember.enc_hp));
			//Setup List View
			//This dual array hack irks me...
			//Log.d("Marker",Integer.toString(currMember.markers.size()));
			ArrayList<Integer> markerIDArray = new ArrayList<Integer>();
			ArrayList<String> markerNameArray = new ArrayList<String>();
			for (Marker iterMarker : currMember.markers)
			{
					markerNameArray.add(iterMarker.effect);
					markerIDArray.add(iterMarker.id);
			}
			setListAdapter(new ArrayAdapter<String>(currContext, R.layout.listitem,markerNameArray));
			if (keepRefresh) {
				refreshHandler.postDelayed(refreshTimeTask, Options.getRefresh(prefs));
			}
		}
	}
	
	class MemberUpdateTask extends AsyncTask<Integer, Void, Member> { //TODO Don't need to return any value from Network thread
		private Member MemberCopy;
		
		public MemberUpdateTask(Member MemberCopy) {
				this.MemberCopy = MemberCopy;
		}
		
		@Override
		//Network code, run in separate thread
		protected Member doInBackground(Integer... params) {
			MemberCopy.enc_hp += params[0];
			MemberCopy.Update();
			return MemberCopy; //TODO Fix this, we don't need to return anything
		}
		
		@Override
		//After network code ran, run in UI thread
		protected void onPostExecute(Member memberNetwork) {
    		refresh();
		}
	}
	
	class MemberInitializeTask extends AsyncTask<Integer, Void, Member> {
		private Member MemberCopy;
		private Context currContext;
		
		public MemberInitializeTask(Context currContext) {
			this.currContext = currContext;
		}
		
		@Override
		//Network code, run in separate thread
		protected Member doInBackground(Integer... params) {
			MemberCopy = new Member(params[0],PreferenceManager.getDefaultSharedPreferences(currContext));
			return MemberCopy;
		}
		
		@Override
		//After network code ran, run in UI thread
		protected void onPostExecute(Member memberNetwork) {
    		currMember = memberNetwork;
			CharacterInitializeTask task = new CharacterInitializeTask(currContext);
			task.execute(memberNetwork);
		}
	}
	
	class CharacterInitializeTask extends AsyncTask<Member, Void, Character> {
		private Character CharacterCopy;
		private Context currContext;
		
		public CharacterInitializeTask(Context currContext) {
			this.currContext = currContext;
		}
		
		@Override
		//Network code, run in separate thread
		protected Character doInBackground(Member... params) {
			CharacterCopy = new Character(params[0].character_id,PreferenceManager.getDefaultSharedPreferences(currContext));
			return CharacterCopy;
		}
		
		@Override
		//After network code ran, run in UI thread
		protected void onPostExecute(Character characterNetwork) {
    		currCharacter = characterNetwork;
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
}