package com.example.rittik97.tutorial;


import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
//import android.app.Fragment;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.squareup.picasso.Picasso;


import java.util.ArrayList;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 */
public class NewFragment extends Fragment {

    private List<String> ls;
    private List<Boxes> item;
    private ListView lw;
    public NewFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View fragview= inflater.inflate(R.layout.fragment_new, container, false);
        lw=(ListView)fragview.findViewById(R.id.Mylistview);
        ls= new ArrayList<String>();
        item= new ArrayList<Boxes>();
        ls.add(0,"First");
        ls.add(1,"Secondi");
        ls.add(2,"Dolce");
        ParseQuery<ParseObject> query = ParseQuery.getQuery("content");
        //query.whereEqualTo("playerName", "Dan Stemkoski");
        query.findInBackground(new FindCallback<ParseObject>() {
            public void done(List<ParseObject> items, ParseException e) {
                if (e == null) {
                    Log.d("score", "Retrieved " + items.size() + " scores");
                    for (ParseObject counter:items){
                        String name= (String) counter.get("Name");
                        String link= (String) counter.get("Link");
                        String piclink= (String) counter.get("PictureLink");

                        Boxes box=new Boxes(name,link,piclink);
                        item.add(box);


                    }
                    lw.setAdapter(new myadapter());

                } else {
                    Log.d("score", "Error: " + e.getMessage());
                }
            }
        });


        lw.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                openbrowser(item.get(position).getLink());
            }
        });

        return fragview;
    }

    public void openbrowser(String Link){
        Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(Link));
        startActivity(browserIntent);
    }

    private class  myadapter extends BaseAdapter{

        @Override
        public int getCount() {
            return item.size();
        }

        @Override
        public Object getItem(int position) {
            return item.get(position);
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View rowview=getActivity().getLayoutInflater().inflate(R.layout.row,null);
            TextView tvrow= (TextView) rowview.findViewById(R.id.textView);
            Boxes bx=item.get(position);
            tvrow.setText(bx.getTitle());
            ImageView iv=(ImageView)rowview.findViewById(R.id.imageView);
            Picasso.with(getActivity()).load(bx.getImagelink()).into(iv);


            return rowview;
        }
    }


}
