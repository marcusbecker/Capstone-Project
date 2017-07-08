package br.com.mvbos.fillit.fragment;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.widget.DatePicker;

import java.util.Calendar;

/**
 * Created by MarcusS on 14/06/2017.
 */

public class DatePickerFragment extends DialogFragment
        implements DatePickerDialog.OnDateSetListener {

    public interface FinishDatePicker {
        void onFinish(Calendar c);
    }

    private FinishDatePicker finishDatePicker;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // Use the current date as the default date in the picker
        final Calendar c = Calendar.getInstance();
        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH);
        int day = c.get(Calendar.DAY_OF_MONTH);

        // Create a new instance of DatePickerDialog and return it
        return new DatePickerDialog(getActivity(), this, year, month, day);
    }

    public void onDateSet(DatePicker view, int year, int month, int day) {
        if (finishDatePicker != null) {
            final Calendar c = Calendar.getInstance();
            c.set(year, month, day);
            finishDatePicker.onFinish(c);
        }
    }

    public FinishDatePicker getFinishDatePicker() {
        return finishDatePicker;
    }

    public void setFinishDatePicker(FinishDatePicker finishDatePicker) {
        this.finishDatePicker = finishDatePicker;
    }
}
