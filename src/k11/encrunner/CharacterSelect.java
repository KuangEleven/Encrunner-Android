package k11.encrunner;

import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.view.View;

public class CharacterSelect extends ListActivity {
	@Override
	public void onCreate(Bundle savedInstanceState) {
	  super.onCreate(savedInstanceState);
	  
	  setListAdapter(new ArrayAdapter<String>(this, R.layout.listitem, getIntent().getExtras().getStringArray("k11.encrunner.characterArray")));

	  ListView lv = getListView();
	  lv.setTextFilterEnabled(true);

	  lv.setOnItemClickListener(new OnItemClickListener() {
	    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
	        Intent intent=new Intent();
	        intent.putExtra("k11.encrunner.characterPosition", position);
	        setResult(RESULT_OK, intent);
	        finish();
	    }
	  });
	}
}
