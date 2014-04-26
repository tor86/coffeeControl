package no.hiof.coffeecontrol;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

// This class makes it possible to display data from database in
// a ListActivity using arraylist

public class CoffeeAdapter extends ArrayAdapter<CoffeeData> {
	
	// Variabels for smiley images
	public int warning = 5;
	public int critical = 10;
	
	public CoffeeAdapter(Context context, int resource) {
		super(context, resource);
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
	    View view = convertView;

	    if (view == null) {
	        LayoutInflater viewInflater = LayoutInflater.from(getContext());
	        view = viewInflater.inflate(R.layout.one_element, null);
	    }

	    CoffeeData test = getItem(position);

	    if (test != null) {
	        TextView date = (TextView) view.findViewById(R.id.textView1);
	        TextView amount = (TextView) view.findViewById(R.id.textView2);
	        ImageView image = (ImageView) view.findViewById(R.id.imageView1);

	        if (date != null) {
	        	//Setting date per entry
	        	String yyyy = test.getDate().substring(0, 4);
	        	String mm = test.getDate().substring(4, 6);
	        	String dd = test.getDate().substring(6, 8);
	        	date.setText(dd + " . " + mm + " . " + yyyy);
	        	//date.setText(test.getDate());
	        }
	        if (amount != null) {
	        	//imageView1
	        	// Setting amount per entry and sets correct smiley according to amount
	        	if(test.getAmount()>critical)image.setImageResource(R.drawable.red_smiley);
	        	else if(test.getAmount()>warning)image.setImageResource(R.drawable.yellow_smiley);
	        	else image.setImageResource(R.drawable.green_smiley);
	        	amount.setText(Integer.toString(test.getAmount()) + " Cups of Coffee ");
	        	//amount.setText(Integer.toString(test.getAmount()) + "");
	        }
	    }

	    return view;
	}
	
	
	
	
	//// This is for backwards compatibility
	
	public void setData(List<CoffeeData> coffeedata) {
	    clear();
	    setNotifyOnChange(false);
	    if (coffeedata != null) {
	        for (CoffeeData item : coffeedata)
	            add(item);
	    }
	    notifyDataSetChanged();
	}
	
	////////////////////////////////////////
	
}
