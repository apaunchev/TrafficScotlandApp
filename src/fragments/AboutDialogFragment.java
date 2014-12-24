package fragments;

/**
 * @author Angel Paunchev (S1105400)
 */

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;

import com.me.trafficscotlandapp.R;

public class AboutDialogFragment extends DialogFragment {
	
	public AboutDialogFragment() {
	}

	@Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
		AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
		
        builder.setTitle(R.string.about)
        	.setMessage(R.string.about_content)
        	.setPositiveButton(R.string.close, new DialogInterface.OnClickListener() {
               public void onClick(DialogInterface dialog, int id) {
               }
           });

        return builder.create();
    }
	
}
