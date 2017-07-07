package br.com.mvbos.fillit.item;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Environment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.File;

import br.com.mvbos.fillit.R;
import br.com.mvbos.fillit.model.VehicleModel;
import br.com.mvbos.fillit.util.FileUtil;

/**
 * Created by MarcusS on 27/06/2017.
 */

public class VehicleSpinnerAdapter extends ArrayAdapter<VehicleModel> {

    private final File mPath;
    private final VehicleModel[] list;

    public VehicleSpinnerAdapter(Context context, VehicleModel[] list) {
        super(context, R.layout.item_flag_spinner, R.id.spinnerTextView, list);
        this.list = list;
        this.mPath = context.getExternalFilesDir(Environment.DIRECTORY_PICTURES);
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

        LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View row = inflater.inflate(R.layout.item_flag_spinner, parent, false);

        VehicleModel v = list[position];

        TextView textView = (TextView) row.findViewById(R.id.spinnerTextView);
        textView.setText(v.getName());

        ImageView imageView = (ImageView) row.findViewById(R.id.spinnerImageView);

        String photo = v.getPhoto();
        Bitmap imageBitmap = null;
        if (photo != null && !photo.isEmpty()) {
            //final Bitmap imageBitmap = BitmapFactory.decodeFile(new File(mPath, photo).getAbsolutePath());
            //imageView.setImageBitmap(FileUtil.roundBitmap(imageBitmap));
            imageBitmap = FileUtil.scale(new File(mPath, photo).getAbsolutePath(), 40, 40);
        }

        if (imageBitmap != null) {
            imageView.setImageBitmap(FileUtil.roundBitmap(imageBitmap));
        } else {
            //TODO set default image
        }

        return row;
    }

    @Override
    public int getCount() {
        return list.length;
    }
}
