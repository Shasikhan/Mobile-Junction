package com.fyp.testmj;

import android.app.Activity;
import android.app.Notification;
import android.app.NotificationManager;
import android.content.Context;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.DrawableRes;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.Marker;

import org.w3c.dom.Text;

import static com.fyp.testmj.LabEngineerHome.CHANNEL_1_ID;

public class Map_Custom_Info_Window implements GoogleMap.InfoWindowAdapter, GoogleMap.OnInfoWindowClickListener {

    private Context context;


    public Map_Custom_Info_Window(Context ctx) {

        context = ctx;

    }
    @Override
    public View getInfoWindow(Marker marker) {
        return null;
    }

    @Override
    public View getInfoContents(Marker marker) {

        View view = ((Activity)context).getLayoutInflater().inflate(R.layout.map_custom_info_window, null);
        //ImageView DP = view.findViewById(R.id.profile_imageview);
        TextView LabEngineerName = view.findViewById(R.id.LabEngineer_name_textview);
        TextView Skills = view.findViewById(R.id.skill_textview);
        TextView Email = view.findViewById(R.id.email_textview);
        TextView PhoneNumber = view.findViewById(R.id.phone_no_textview);

        RatingBar ratingBar = view.findViewById(R.id.star_rating);

        //LabEngineerName.setText(marker.getTitle());

        Info_Window_Data infoWindowData = (Info_Window_Data) marker.getTag();

        LabEngineerName.setText(infoWindowData.getLabEngineerName());
        Skills.setText(infoWindowData.getLabEngineerSkills());
        Email.setText(infoWindowData.getLabEngineerEmail());
        PhoneNumber.setText(infoWindowData.getLabEngineerPhone());
        ratingBar.setRating(infoWindowData.getRatingBar());

        /*
        int imageId = context.getResources().getIdentifier(infoWindowData.getLabRnineerImage().toLowerCase(),
                "drawable", context.getPackageName());
        DP.setImageResource(imageId);

        /*
        * Rating Bar is remaining to be added
        * */

        return view;
    }

    @Override
    public void onInfoWindowClick(Marker marker) {



    }
}
