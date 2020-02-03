package com.fyp.testmj;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.Marker;

public class InfoWndowAdapter implements GoogleMap.InfoWindowAdapter {
    private Context context;
    public InfoWndowAdapter(Context context) {
        this.context = context.getApplicationContext();
    }
    @Override
    public View getInfoWindow(Marker marker) {
        return null;
    }
    @Override
    public View getInfoContents(Marker marker) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view =  inflater.inflate(R.layout.custome_infowindow, null);
        TextView tvDetails = (TextView) view.findViewById(R.id.tvd);
        return view;
    }


}
