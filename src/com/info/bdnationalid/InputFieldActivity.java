package com.info.bdnationalid;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import com.opencsv.CSVWriter;

import android.app.Activity;
import android.app.Dialog;
import android.app.AlertDialog.Builder;
import android.content.ActivityNotFoundException;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.Environment;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;


public class InputFieldActivity extends Activity implements OnClickListener
{

	private String dbName = SQLiteEntry.DATABASE_NAME;
	private String Myname = SQLiteEntry.KEY_MY_NAME;
	private String Fname = SQLiteEntry.KEY_FATHER_NAME;
	private String Mname = SQLiteEntry.KEY_MOTHER_NAME;
	private String Occupation = SQLiteEntry.KEY_OCCUPATION;
	private String daycol = SQLiteEntry.KEY_DAY;
	private String monthcol = SQLiteEntry.KEY_MONTH;
	private String yearcol = SQLiteEntry.KEY_YEAR;
	private String Idno = SQLiteEntry.KEY_ID_NO;
	private String Address = SQLiteEntry.KEY_ADDRESS;
	
	
	
	EditText myname,fname,mname,idno,address,occupation;
	int day,month,year;
	Button submit,showlist,importbtn,exportbtn,dp;
	Context context;
	Intent intent;
	private String TableID;
	
	Calendar today;
	
	private SQLiteDatabase newDB;
	private SQLiteEntry helper;
	
	
	
	public static final int requestcode = 1;
	
	
    @Override
    protected void onCreate(Bundle savedInstanceState) 
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_input_field);
        
        intent = getIntent();
        
        if(intent.getExtras() != null)
        {
        	TableID = intent.getExtras().get("_table").toString();
        }
        context = getApplicationContext();
        init();
    }

    public void init()
    {
    	myname = (EditText) findViewById(R.id.myName);
    	fname = (EditText) findViewById(R.id.fatherName);
    	mname = (EditText) findViewById(R.id.motherName);
    	occupation = (EditText) findViewById(R.id.occupation);
    	idno = (EditText) findViewById(R.id.idNo);
    	address = (EditText) findViewById(R.id.address);
    	dp = (Button) findViewById(R.id.datePicker);
    	submit = (Button) findViewById(R.id.submitBtn);
    	showlist = (Button) findViewById(R.id.nationalIDList);
    	importbtn = (Button) findViewById(R.id.importb);
    	exportbtn = (Button) findViewById(R.id.exportb);
    	
    	
    	
	    today = Calendar.getInstance();
	    year = today.get(Calendar.YEAR);
	    month = today.get(Calendar.MONTH);
	    day = today.get(Calendar.DAY_OF_MONTH);
	    
	    dp.setOnClickListener(this);
	    submit.setOnClickListener(this);
	    showlist.setOnClickListener(this);
	    importbtn.setOnClickListener(this);
	    exportbtn.setOnClickListener(this);
    	
    }
    


	@Override
	public void onClick(final View v) 
	{
		switch(v.getId())
		{
		case R.id.datePicker:
			DatePickerDailog dp = new DatePickerDailog(InputFieldActivity.this,today, new DatePickerDailog.DatePickerListner() 
			{

						@Override
						public void OnDoneButton(Dialog datedialog, Calendar c) 
						{
							datedialog.dismiss();
							
							today.set(Calendar.YEAR, c.get(Calendar.YEAR));
							today.set(Calendar.MONTH,c.get(Calendar.MONTH));
							today.set(Calendar.DAY_OF_MONTH,c.get(Calendar.DAY_OF_MONTH));
							
							
							((Button) v).setText(new SimpleDateFormat("MMMM dd yyyy").format(c.getTime()));
							
							day = c.get(Calendar.DAY_OF_MONTH);
							month = c.get(Calendar.MONTH)+1;
							year = c.get(Calendar.YEAR);
							
						}

						@Override
						public void OnCancelButton(Dialog datedialog) 
						{
							// TODO Auto-generated method stub
							datedialog.dismiss();
						}
					});
			dp.show();
			break;
		case R.id.submitBtn:
		{
			boolean didItWork = true;
			try
			{
				String myName = myname.getText().toString();
				String fName = fname.getText().toString();
				String mName = mname.getText().toString();
				String Occu = occupation.getText().toString();
				String idNo = idno.getText().toString();
				String Address = address.getText().toString();


			    
			    if(myName.isEmpty() || fName.isEmpty() || mName.isEmpty() || Occu.isEmpty() || idNo.isEmpty() || Address.isEmpty() || this.dp.getText().equals("Select Date of Birth"))
			    {
			    	didItWork = false;
			    	Toast.makeText(this, "All field required!", Toast.LENGTH_SHORT).show();
			    }
			    else
			    {
					SQLiteEntry entry = new SQLiteEntry(InputFieldActivity.this,TableID);
					entry.createEntry(myName, fName, mName,Occu, this.day, this.month, this.year, idNo, Address);
					entry.close();
					this.myname.setText("");
					this.fname.setText("");
					this.mname.setText("");
					this.occupation.setText("");
					this.idno.setText("");
					this.address.setText("");
					this.dp.setText("Select Date of Birth");
					this.myname.requestFocus();
			    }
				
			}
			catch (Exception e)
			{
				didItWork = false;
				String error = e.toString();
				Dialog d = new Dialog(context);
				d.setTitle("Dang it!");
				TextView tv = new TextView(context);
				tv.setText(error);
				d.setContentView(tv);
				d.show();
			}
			finally
			{
				if(didItWork)
				{
					Toast.makeText(context, "Saved", Toast.LENGTH_SHORT).show();
				}
			}
		}
			break;
		case R.id.nationalIDList:
			Intent i = new Intent(context,NIDList.class);
			i.putExtra("_table", TableID);
			startActivity(i);
			break;
		case R.id.importb:
		{
            Intent fileintent = new Intent(Intent.ACTION_GET_CONTENT);
            fileintent.setType("gagt/sdf");
            try 
            {
                startActivityForResult(fileintent, requestcode);
            }
            catch (ActivityNotFoundException e) 
            {
                Toast.makeText(this,"No app found for importing the file.",Toast.LENGTH_SHORT).show();
            }
		}

			break;
		case R.id.exportb:
		{
			
			String currentDBPath = "/data/"+getPackageName()+"/databases/"+dbName;
			File dbFile = getDatabasePath(currentDBPath);
			helper = new SQLiteEntry(this.getApplicationContext(),TableID);
			File exportDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS);
			
			if(!exportDir.exists())
			{
				exportDir.mkdirs();
			}
			
			File file = new File(exportDir,TableID+".csv");
			try 
			{
				file.createNewFile();
				CSVWriter csvwriter = new CSVWriter(new FileWriter(file),',',CSVWriter.NO_QUOTE_CHARACTER,CSVWriter.NO_ESCAPE_CHARACTER);
				newDB = helper.getReadableDatabase();
				Cursor c = newDB.rawQuery("SELECT * FROM "+TableID, null);
				csvwriter.writeNext(c.getColumnNames());
				while(c.moveToNext())
				{
					String arrStr[] = {c.getString(1), c.getString(2),c.getString(3),c.getString(4),c.getString(5),c.getString(6),c.getString(7),c.getString(8),c.getString(9)};
					csvwriter.writeNext(arrStr);
				}
				csvwriter.close();
				c.close();
				Toast.makeText(getApplicationContext(), "Exported in Download Folder", Toast.LENGTH_SHORT).show();
			}
			catch (Exception e) 
			{
				// TODO: handle exception
				Toast.makeText(getApplicationContext(), "No information found for exporting.", Toast.LENGTH_SHORT).show();
			}

		}
			break;
		}
	}


	
	
	
	 protected void onActivityResult(int requestCode, int resultCode, Intent data)
	 {
		 if(data == null)return;
		 
	        switch (requestCode) 
	        {
	            case requestcode:
	                String filepath = data.getData().getPath();
	                SQLiteEntry controller = new SQLiteEntry(getApplicationContext(),TableID);
	                SQLiteDatabase db = controller.getWritableDatabase();
	                try 
	                {
	                    if (resultCode == RESULT_OK) 
	                    {
	                        try 
	                        {
	                            FileReader file = new FileReader(filepath);
	                            BufferedReader buffer = new BufferedReader(file);
	                            ContentValues contentValues = new ContentValues();
	                            String line = "";
	                            db.beginTransaction();
	                            line = buffer.readLine();                 //skipping header line from excel sheet
	                            while ((line = buffer.readLine()) != null) 
	                            {
	                                String[] str = line.split(",", 9);  // defining 3 columns with null or blank field //values acceptance
	                                //Id, Company,Name,Price
	                                String myname = str[0].toString();
	                                String fname = str[1].toString();
	                                String mname = str[2].toString();
	                                String occupation = str[3].toString();
	                                String day = str[4].toString();
	                                String month = str[5].toString();
	                                String year = str[6].toString();
	                                String idno = str[7].toString();
	                                String address = str[8].toString();
	                                
	                                
	                                contentValues.put(Myname, myname);
	                                contentValues.put(Fname, fname);
	                                contentValues.put(Mname, mname);
	                                contentValues.put(Occupation, occupation);
	                                contentValues.put(daycol, day);
	                                contentValues.put(monthcol, month);
	                                contentValues.put(yearcol, year);
	                                contentValues.put(Idno, idno);
	                                contentValues.put(Address, address);
	                                db.insert(TableID, null, contentValues);
	                            }
	                            Toast.makeText(getApplicationContext(), "Imported", Toast.LENGTH_SHORT).show();
	                            db.setTransactionSuccessful();
	                            db.endTransaction();
	                        } 
	                        catch (IOException e) 
	                        {
	                            if (db.inTransaction())
	                                db.endTransaction();
	                            Dialog d = new Dialog(this);
	                            d.setTitle(e.getMessage().toString() + "first");
	                            d.show();
	                            // db.endTransaction();
	                        }
	                    } 
	                    else 
	                    {
	                        if (db.inTransaction())
	                            db.endTransaction();
	                        Dialog d = new Dialog(this);
	                        d.setTitle("Only CSV files allowed");
	                        d.show();
	                    }
	                } 
	                catch (Exception ex) 
	                {
	                    if (db.inTransaction())
	                        db.endTransaction();
	                    Dialog d = new Dialog(this);
	                    d.setTitle(ex.getMessage().toString() + "second");
	                    d.show();
	                    // db.endTransaction();
	                }
	        }
	 }
	
	



	@Override
	public boolean onCreateOptionsMenu(Menu menu) 
	{
		getMenuInflater().inflate(R.menu.main_act_menu, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) 
	{
		int id = item.getItemId();
		if(id == R.id.deleteDB)
		{
			Builder bd = new Builder(InputFieldActivity.this);
			bd.setIcon(android.R.drawable.ic_delete);
			bd.setTitle("Delete Database");
			bd.setMessage("This will completely delete male and female database. This cannot be undone. Are you sure?");
			
			bd.setPositiveButton("OK", new DialogInterface.OnClickListener() 
			{
				@Override
				public void onClick(DialogInterface dialog, int which) 
				{
					// TODO Auto-generated method stub
					boolean result = InputFieldActivity.this.deleteDatabase(dbName);
					if (result==true) 
					{
						 Toast.makeText(InputFieldActivity.this, "Database Deleted.", Toast.LENGTH_SHORT).show();
					}
				}
			});
			
			bd.setNegativeButton("Cancel", new DialogInterface.OnClickListener() 
			{
				
				@Override
				public void onClick(DialogInterface dialog, int which) 
				{
					// TODO Auto-generated method stub
				}
			});
			bd.create().show(); 
			return true;
		}
		
		return super.onOptionsItemSelected(item);
	}

	
	
}
