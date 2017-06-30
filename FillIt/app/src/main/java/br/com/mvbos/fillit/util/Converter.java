package br.com.mvbos.fillit.util;

import android.widget.EditText;

import java.text.SimpleDateFormat;
import java.util.Locale;

/**
 * Created by Marcus Becker on 26/06/2017.
 */

public class Converter {

    public static float toFloat(EditText editText, float defVal) {
        if (editText != null && editText.getText() != null && editText.getText().length() > 0) {
            return Float.parseFloat(editText.getText().toString());
        }

        return defVal;
    }
}
