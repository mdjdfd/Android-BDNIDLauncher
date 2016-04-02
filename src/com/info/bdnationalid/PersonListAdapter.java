package com.info.bdnationalid;

import java.util.ArrayList;

import android.content.Context;
import android.graphics.Color;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView.FindListener;
import android.widget.BaseAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

public class PersonListAdapter extends BaseAdapter implements Filterable
{

	
    private NIDList activity;
    private PersonFilter personFilter;
    private ArrayList<Person> personList;
    private ArrayList<Person> filteredPersonList;
    private SparseBooleanArray mSelectedItemsIds;
	
	
    
    public PersonListAdapter(NIDList activity, ArrayList<Person> personList) 
    {
    	mSelectedItemsIds = new SparseBooleanArray();
        this.activity = activity;
        this.personList = personList;
        this.filteredPersonList = personList;
        getFilter();
    }
    
    
    
	@Override
	public int getCount() 
	{
		// TODO Auto-generated method stub
		return filteredPersonList.size();
	}

	@Override
	public Object getItem(int position) 
	{
		// TODO Auto-generated method stub
		return filteredPersonList.get(position);
	}

	@Override
	public long getItemId(int position) 
	{
		// TODO Auto-generated method stub
		return position;
	}

	
    static class ViewHolder 
    {
    	TextView primaryKey;
        TextView name;
        TextView id;
    }
	

	@Override
	public View getView(int position, View view, ViewGroup parent) 
	{
        final ViewHolder holder;
        final Person person = (Person) getItem(position);
        
        if (view == null) 
        {
            LayoutInflater layoutInflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = layoutInflater.inflate(R.layout.list_item, parent, false);
            holder = new ViewHolder();
           
           // holder.primaryKey = (TextView) view.findViewById(R.id.uniqueKey);
            holder.name = (TextView) view.findViewById(R.id.nameList);
            holder.id = (TextView) view.findViewById(R.id.idnoList);
            
            view.setTag(holder);
        }
        
        else 
        {
            // get view holder back
            holder = (ViewHolder) view.getTag();
        }
        
        
        
        //holder.primaryKey.setText(person.getPrimaryKey()+".");
        holder.name.setText(person.getName());
        holder.id.setText(person.getIDNO());
        
        /*if(mSelectedItemsIds.get(position))
        {
        	view.setBackgroundColor(Color.CYAN);
        }
        else
        {
        	view.setBackgroundColor(Color.WHITE);
        }*/
        return view;
	}
	
	
	
	public void remove(Person object) 
	{
		personList.remove(object);
		notifyDataSetChanged();
	}
	
	
	public ArrayList<Person> getWorldPopulation() 
	{
		return personList;
	}
	
	public void toggleSelection(int position) 
	{
		selectView(position, !mSelectedItemsIds.get(position));
	}

	public void removeSelection() 
	{
		mSelectedItemsIds = new SparseBooleanArray();
		notifyDataSetChanged();
	}

	public void selectView(int position, boolean value) 
	{
		if (value)
			mSelectedItemsIds.put(position, value);
		else
			mSelectedItemsIds.delete(position);
		notifyDataSetChanged();
	}

	public int getSelectedCount() 
	{
		return mSelectedItemsIds.size();
	}

	public SparseBooleanArray getSelectedIds() 
	{
		return mSelectedItemsIds;
	}
	


	@Override
	public Filter getFilter() 
	{
        if (personFilter == null) 
        {
            personFilter = new PersonFilter();
        }

        return personFilter;
	}

	
    private class PersonFilter extends Filter 
    {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) 
        {
            FilterResults filterResults = new FilterResults();
            if (constraint!=null && constraint.length()>0) {
                ArrayList<Person> tempList = new ArrayList<Person>();

                // search content in friend list
                for (Person user : personList) 
                {
                    //if (user.getName().toLowerCase().contains(constraint.toString().toLowerCase())) 
                	if (user.getName().toLowerCase().contains(constraint.toString().toLowerCase()) || user.getIDNO().contains(constraint.toString()))
                    {
                        tempList.add(user);
                    }
                }

                filterResults.count = tempList.size();
                filterResults.values = tempList;
            }
            else 
            {
                filterResults.count = personList.size();
                filterResults.values = personList;
            }

            return filterResults;
        }
	
	
        @SuppressWarnings("unchecked")
        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) 
        {
            filteredPersonList = (ArrayList<Person>) results.values;
            notifyDataSetChanged();
        }

    }

}
