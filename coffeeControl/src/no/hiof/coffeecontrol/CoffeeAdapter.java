package no.hiof.coffeecontrol;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

public class CoffeeAdapter extends ArrayAdapter<CoffeeData> {
	
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

	        if (date != null) {
	        	String yyyy = test.getDate().substring(0, 4);
	        	String mm = test.getDate().substring(4, 6);
	        	String dd = test.getDate().substring(6, 8);
	        	date.setText(dd + " . " + mm + " . " + yyyy);
	        	//date.setText(test.getDate());
	        }
	        if (amount != null) {
	        	amount.setText(Integer.toString(test.getAmount()) + " Cups of Coffee ");
	        	//amount.setText(Integer.toString(test.getAmount()) + "");
	        }
	    }

	    return view;
	}
}
