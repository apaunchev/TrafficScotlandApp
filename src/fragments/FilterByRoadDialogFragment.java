package fragments;

/**
 * @author Angel Paunchev (S1105400)
 */

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;

import com.me.trafficscotlandapp.R;

public class FilterByRoadDialogFragment extends DialogFragment {
	
	public FilterByRoadDialogFragment() {
	}
	
	public interface FilterByRoadDialogListener {
		public void onRoadDialogPositiveClick(DialogFragment dialog, String roadName);
    }
	
	FilterByRoadDialogListener listener;
	
	@Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        
        try {
            listener = (FilterByRoadDialogListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString() + " must implement FilterByRoadDialogListener");
        }
    }
	
	@Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
		LayoutInflater inflater = getActivity().getLayoutInflater();
		View view = inflater.inflate(R.layout.fragment_filter_by_road, null);
		final EditText roadNameEv = (EditText) view.findViewById(R.id.road_name);
		
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        
        builder.setView(view)
        	.setTitle(R.string.filter_by_road)
        	.setPositiveButton(R.string.filter, new DialogInterface.OnClickListener() {
               @Override
               public void onClick(DialogInterface dialog, int id) {
            	   String input = roadNameEv.getText().toString();
            	   if (input != "") {
            		   listener.onRoadDialogPositiveClick(FilterByRoadDialogFragment.this, input);
            	   }
               }
           })
           .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
        	   @Override
               public void onClick(DialogInterface dialog, int id) {
            	   FilterByRoadDialogFragment.this.getDialog().cancel();
               }
           });
        
        return builder.create();
    }

}
