package fragments;

/**
 * @author Angel Paunchev (S1105400)
 */

import java.util.Calendar;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.widget.DatePicker;

public class FilterByDatePickerFragment extends DialogFragment
	implements DatePickerDialog.OnDateSetListener {
	
	public FilterByDatePickerFragment() {
	}
	
	public interface FilterByDateDialogListener {
        public void onDateSetCompleted(DatePicker view, int year, int month, int day);
    }
	
	FilterByDateDialogListener listener;
	
	@Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        
        try {
            listener = (FilterByDateDialogListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString() + " must implement FilterByDateDialogListener");
        }
    }
	
	@Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // Use the current date as the default date in the picker.
        final Calendar c = Calendar.getInstance();
        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH);
        int day = c.get(Calendar.DAY_OF_MONTH);

        // Create a new instance of DatePickerDialog and return it.
        return new DatePickerDialog(getActivity(), this, year, month, day);
    }
	
	public void onDateSet(DatePicker view, int year, int month, int day) {
		listener.onDateSetCompleted(view, year, month, day);
    }

}
