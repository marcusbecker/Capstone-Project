package br.com.mvbos.fillit;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import br.com.mvbos.fillit.fragment.PrefsFragment;

public class SettingsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        getFragmentManager().beginTransaction()
                .replace(android.R.id.content, new PrefsFragment())
                .commit();

    }
}
