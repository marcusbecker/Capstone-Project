package br.com.mvbos.fillit.item;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import br.com.mvbos.fillit.R;
import br.com.mvbos.fillit.model.FlagModel;

/**
 * Created by MarcusS on 27/06/2017.
 */

public class FlagSpinnerAdapter extends ArrayAdapter<FlagModel> {

    private Context ctx;
    private FlagModel[] list;

    private final String urlPath;

    public FlagSpinnerAdapter(Context context, FlagModel[] list) {
        super(context, R.layout.item_flag_spinner, R.id.spinnerTextView, list);
        this.ctx = context;
        this.list = list;
        urlPath = context.getString(R.string.url_images);
    }

    @Override
    public View getDropDownView(int position, View convertView, ViewGroup parent) {
        return getCustomView(position, parent);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        return getCustomView(position, parent);
    }

    public View getCustomView(int position, ViewGroup parent) {

        LayoutInflater inflater = (LayoutInflater) ctx.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View row = inflater.inflate(R.layout.item_flag_spinner, parent, false);

        TextView textView = (TextView) row.findViewById(R.id.spinnerTextView);
        textView.setText(list[position].getName());

        ImageView imageView = (ImageView) row.findViewById(R.id.spinnerImageView);
        Picasso.with(ctx)
                .load(urlPath + list[position].getIcon())
                .placeholder(R.drawable.ic_create_white_24dp)
                .into(imageView);


        return row;
    }

    @Override
    public int getCount() {
        return list.length;
    }
}
