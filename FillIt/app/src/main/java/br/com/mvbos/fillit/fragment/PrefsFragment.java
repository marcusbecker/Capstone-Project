package br.com.mvbos.fillit.fragment;

import android.os.Bundle;
import android.preference.PreferenceFragment;

import br.com.mvbos.fillit.R;

/**
 * Created by MarcusS on 05/07/2017.
 */

public class PrefsFragment extends PreferenceFragment {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        addPreferencesFromResource(R.xml.preferences);
    }
}