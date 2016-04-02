package com.info.bdnationalid;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class LauncherActivity extends Activity implements OnClickListener
{

	Button male,female,about;
	@Override
	protected void onCreate(Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_launcher);
		
		male = (Button) findViewById(R.id.maleinfo);
		female = (Button) findViewById(R.id.femaleinfo);
		about = (Button) findViewById(R.id.about);
		
		male.setOnClickListener(this);
		female.setOnClickListener(this);
		about.setOnClickListener(this);
		
	}

	@Override
	public void onClick(View v) 
	{
		switch(v.getId())
		{
		case R.id.maleinfo:
			Intent i = new Intent(this,InputFieldActivity.class);
			i.putExtra("_table","MaleTable");
			startActivity(i);
			//finish();
			break;
		case R.id.femaleinfo:
			Intent j = new Intent(this,InputFieldActivity.class);
			j.putExtra("_table","FemaleTable");
			startActivity(j);
			//finish();
			break;
		case R.id.about:
			Intent k = new Intent(this,AboutDeveloper.class);
			startActivity(k);
			break;
		}
	}
	
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) 
	{
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.launcher, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
	
	
	
}
