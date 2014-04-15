package com.povodev.hemme.android.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.povodev.hemme.android.R;
import com.povodev.hemme.android.bean.Document;
import com.povodev.hemme.android.bean.Result;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by gbonadiman.stage on 15/04/2014.
 */
public class Document_Adapter extends ArrayAdapter<Document> {

    private Context context;
    private List<Document> documenti;

    public Document_Adapter(Context context, int resource, ArrayList<Document> documenti) {
        super(context, resource, documenti);
        this.context = context;
        this.documenti = documenti;
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        View view = convertView;
        if (view == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.diary_row_layout,null);
        }

        Document item = getItem(position);
        if (item!= null) {
            TextView noteText = (TextView) view.findViewById(R.id.textViewList);
            ImageView imageView = (ImageView) view.findViewById(R.id.image_view);
            if (noteText!= null) {
                noteText.setText(item.getFile());
            }
            if (imageView!= null) {
                imageView.setImageBitmap(item.getFile_immagine());
            }
        }
        return view;
    }


}
