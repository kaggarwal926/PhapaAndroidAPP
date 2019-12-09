package com.icls.offlinekyc.function.Adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import com.icls.offlinekyc.R;
import com.icls.offlinekyc.function.Models.kycReqListItemModel;

import java.util.ArrayList;

import static com.android.volley.VolleyLog.TAG;

public class KycReqListAdapter extends BaseAdapter {

    Context ctx;
    LayoutInflater lInflater;
    ArrayList<kycReqListItemModel> objects;
    CompoundButton.OnCheckedChangeListener myCheckChangList = new CompoundButton.OnCheckedChangeListener() {
        public void onCheckedChanged(CompoundButton buttonView,
                                     boolean isChecked) {
            getkycReqListItemModel((Integer) buttonView.getTag()).box = isChecked;
        }
    };


    public KycReqListAdapter(Context context, ArrayList<kycReqListItemModel> doclist) {
        ctx = context;
        objects = doclist;
        lInflater = (LayoutInflater) ctx
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return objects.size();
    }

    @Override
    public Object getItem(int position) {
        return objects.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View view = convertView;
        if (view == null) {
            view = lInflater.inflate(R.layout.kyclistitem, parent, false);
        }

        kycReqListItemModel p = getkycReqListItemModel(position);
        if (p!= null ) {
            ((TextView) view.findViewById(R.id.kycReqListItem)).setText(p.getdOCName());
            CheckBox cbBuy = view.findViewById(R.id.cbBox);
            cbBuy.setOnCheckedChangeListener(myCheckChangList);
            cbBuy.setTag(position);
            cbBuy.setChecked(p.box);
        }
        return view;
    }

    private kycReqListItemModel getkycReqListItemModel(int position) {
        return ((kycReqListItemModel) getItem(position));
    }

    public ArrayList<kycReqListItemModel> getBox() {
        ArrayList<kycReqListItemModel> box = new ArrayList<kycReqListItemModel>();
        for (kycReqListItemModel p : objects) {
            Log.w(TAG, "getBox: " + p);
            if (p.isBox())
                box.add(p);
        }
        return box;
    }
}
