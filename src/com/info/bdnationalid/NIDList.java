package com.info.bdnationalid;



import java.util.ArrayList;

import android.app.Activity;
import android.app.SearchManager;
import android.app.AlertDialog.Builder;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.os.Bundle;
import android.util.Log;
import android.util.SparseBooleanArray;
import android.view.ActionMode;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.Toast;
import android.widget.SearchView.OnQueryTextListener;
import android.widget.AbsListView.MultiChoiceModeListener;
import android.widget.AdapterView.OnItemClickListener;

public class NIDList extends Activity implements OnItemClickListener,OnQueryTextListener
{
	private String dbName = SQLiteEntry.DATABASE_NAME;
	private String rowID = SQLiteEntry.KEY_ROWID;
	//private String tableName = SQLiteEntry.DATABASE_TABLE;
	private String myname = SQLiteEntry.KEY_MY_NAME;
	private String fname = SQLiteEntry.KEY_FATHER_NAME;
	private String mname = SQLiteEntry.KEY_MOTHER_NAME;
	private String occupation = SQLiteEntry.KEY_OCCUPATION;
	private String daycol = SQLiteEntry.KEY_DAY;
	private String monthcol = SQLiteEntry.KEY_MONTH;
	private String yearcol = SQLiteEntry.KEY_YEAR;
	private String idno = SQLiteEntry.KEY_ID_NO;
	private String address = SQLiteEntry.KEY_ADDRESS;
	
	private String TableID;
	
	ListView lv;
	PersonListAdapter lvadapter;
	ArrayList<Person> personlist;
	
	boolean flag;
	private SQLiteDatabase newDB;
	private SQLiteEntry helper;
	
	EditText inputSearch;
	Intent intent;
	private String tableName;

	@Override
	protected void onCreate(Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_nidlist);
		
        intent = getIntent();
        
        if(intent.getExtras() != null)
        {
        	TableID = intent.getExtras().get("_table").toString();
        }
        
        tableName = TableID;
		openAndQueryDatabase();

	}

	private void openAndQueryDatabase()
	{

		lv = (ListView) findViewById(R.id.listView);
		lv.setOnItemClickListener(NIDList.this);
		
		personlist = new ArrayList<Person>();
		lvadapter = new PersonListAdapter(this,personlist);
		lv.setAdapter(lvadapter);
		lv.setTextFilterEnabled(true);
		deleteItemFromList();	
		
		try
		{
			helper = new SQLiteEntry(this.getApplicationContext(),tableName);
			newDB = helper.getWritableDatabase();
			Cursor c = newDB.rawQuery("SELECT * FROM "+tableName, null);
			if(c!=null)
			{
				if(c.moveToFirst())
				{
					do
					{
						String primaryKey = c.getString(c.getColumnIndex(rowID));
						String myName =c.getString(c.getColumnIndex(myname));
						String IDNO =c.getString(c.getColumnIndex(idno));
						
						Person person = new Person(primaryKey,myName, IDNO);
						personlist.add(person);
					}
					while(c.moveToNext());
				}
			}
		}
		catch (SQLiteException se ) 
		{
        	Log.e(getClass().getSimpleName(), "Could not create or Open the database");
        }

	}
	
	

	
	private void deleteItemFromList() 
	{
		lv.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE_MODAL);
		
		lv.setMultiChoiceModeListener(new MultiChoiceModeListener() 
		{

			@Override
			public void onItemCheckedStateChanged(ActionMode mode,int position, long id, boolean checked) 
			{
				// Capture total checked items
				final int checkedCount = lv.getCheckedItemCount();
				// Set the CAB title according to total checked items
				mode.setTitle(checkedCount + " Selected");
				// Calls toggleSelection method from ListViewAdapter Class
				lvadapter.toggleSelection(position);
			}

			@Override
			public boolean onActionItemClicked(final ActionMode mode, MenuItem item) 
			{
				switch (item.getItemId()) 
				{
				case R.id.delete:
				{	
					Builder bd = new Builder(NIDList.this);
					bd.setIcon(android.R.drawable.ic_delete);
					bd.setTitle("Delete Information");
					bd.setMessage("Are you sure you want to delete?");
					
					bd.setPositiveButton("Yes", new DialogInterface.OnClickListener() 
					{
						@Override
						public void onClick(DialogInterface dialog, int which) 
						{

							SparseBooleanArray selected = lvadapter.getSelectedIds();
							helper = new SQLiteEntry(NIDList.this,tableName);
							for (int i = (selected.size() - 1); i >= 0; i--) 
							{
								if (selected.valueAt(i)) 
								{
									Person selecteditem = (Person) lvadapter.getItem(selected.keyAt(i));
									lvadapter.remove(selecteditem);
											
									String Pkey = selecteditem.getPrimaryKey();
									helper.DBdelete(Integer.valueOf(Integer.valueOf(Pkey)));
								}
							}

							// Close CAB
							helper.close();
							mode.finish();
						}
					});
					
					bd.setNegativeButton("No", new DialogInterface.OnClickListener() 
					{
						
						@Override
						public void onClick(DialogInterface dialog, int which) 
						{
							// TODO Auto-generated method stub
						}
					});
					bd.create().show(); 
					/*SparseBooleanArray selected = lvadapter.getSelectedIds();
					helper = new SQLiteEntry(NIDList.this,tableName);
					for (int i = (selected.size() - 1); i >= 0; i--) 
					{
						if (selected.valueAt(i)) 
						{
							Person selecteditem = (Person) lvadapter.getItem(selected.keyAt(i));
							lvadapter.remove(selecteditem);
									
							String Pkey = selecteditem.getPrimaryKey();
							helper.DBdelete(Integer.valueOf(Integer.valueOf(Pkey)));
						}
					}

					// Close CAB
					helper.close();
					mode.finish();*/
				}
				return true;
						
				case R.id.selectAll:
					int cntChoice = lv.getCount();
					SparseBooleanArray sparseBooleanArray = lv.getCheckedItemPositions();
					for(int i = 0; i < cntChoice; i++)
					{
						if(!sparseBooleanArray.get(i))
						{
							lv.setItemChecked(i, true);
						}
					}
					return true;
				default:
					return false;
				}
			}

			@Override
			public boolean onCreateActionMode(ActionMode mode, Menu menu) 
			{
				mode.getMenuInflater().inflate(R.menu.main, menu);
				return true;
			}

			@Override
			public void onDestroyActionMode(ActionMode mode) 
			{
				// TODO Auto-generated method stub
				lvadapter.removeSelection();
			}

			@Override
			public boolean onPrepareActionMode(ActionMode mode, Menu menu) 
			{
				// TODO Auto-generated method stub
				return false;
			}
		});
	}



	@Override
	public boolean onCreateOptionsMenu(Menu menu) 
	{
		getMenuInflater().inflate(R.menu.main, menu);
		SearchManager searchManager = (SearchManager) getSystemService( Context.SEARCH_SERVICE );
        SearchView searchView = (SearchView) menu.findItem(R.id.menu_item_search).getActionView();

        searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
        //searchView.setSubmitButtonEnabled(true);
        searchView.setOnQueryTextListener(this);

        return super.onCreateOptionsMenu(menu);
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) 
	{
		int id = item.getItemId();
		return super.onOptionsItemSelected(item);
	}


	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,long id) 
	{
		helper = new SQLiteEntry(this.getApplicationContext(),tableName);
		newDB = helper.getWritableDatabase();
		Cursor c = newDB.rawQuery("SELECT * FROM "+tableName, null);
		c.moveToPosition(position);
		String myName = c.getString(c.getColumnIndex(myname));
		String fName = c.getString(c.getColumnIndex(fname));
		String mName = c.getString(c.getColumnIndex(mname));
		String occupat = c.getString(c.getColumnIndex(occupation));
		String day = c.getString(c.getColumnIndex(daycol));
		String month = c.getString(c.getColumnIndex(monthcol));
		String year = c.getString(c.getColumnIndex(yearcol));
		String IDNO = c.getString(c.getColumnIndex(idno));
		String addr = c.getString(c.getColumnIndex(address));
		
		String output = getString(R.string.Name)+" "+myName+"\n"+getString(R.string.father)+" "+fName+"\n"+getString(R.string.mother)+" "+mName+"\n"+getString(R.string.occup)+" "+occupat+"\n"+getString(R.string.birth)+" "+day+"/"+month+"/"+year+"\n"+getString(R.string.idNo)+" "+IDNO+"\n"+getString(R.string.address)+" "+addr;
		
		Builder bd = new Builder(NIDList.this);
		bd.setIcon(android.R.drawable.ic_dialog_info);
		bd.setTitle("Information");
		bd.setMessage(output);
		
		bd.setPositiveButton("OK", new DialogInterface.OnClickListener() 
		{
			@Override
			public void onClick(DialogInterface dialog, int which) 
			{
			}
		});
		
		bd.create().show();
	}

	@Override
	public boolean onQueryTextSubmit(String query) 
	{
		// TODO Auto-generated method stub
		return false;
	}


	@Override
	public boolean onQueryTextChange(String newText) 
	{
		lvadapter.getFilter().filter(newText);
        
   	 	return true;
	}
}
