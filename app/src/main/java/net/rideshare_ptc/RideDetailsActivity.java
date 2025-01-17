package net.rideshare_ptc;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ScrollView;
import android.widget.TextView;

import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import android.os.Bundle;

public class RideDetailsActivity extends AppCompatActivity {

    static ArrayList<Ride> Rides = new ArrayList<Ride>();

    TextView rideDetails;
    Ride ride = new Ride();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ride_details);
        // Get A RIDE specific
        rideDetails = (TextView) findViewById(R.id.txtViewRideDetails);
        String rideData = ride.toString();
        try {
            getRidesFromDB();
            rideDetails.setText(rideData);

            //dest.setText(ride.getDest());
        } catch (IOException e) {
            e.printStackTrace();
        }


    }

    private void getRidesFromDB() throws IOException {
        int resCode = 0;
        String strResponse = "";
        URL url =new URL("http://10.0.2.2:8080/viewRides");

        HttpURLConnection con = (HttpURLConnection) url.openConnection();
        con.setUseCaches(false);
        con.setRequestMethod("GET");

        con.connect();
        try {
            BufferedReader buffread = new BufferedReader(
                    new InputStreamReader(con.getInputStream()));
            StringBuilder stringBuilder = new StringBuilder();
            String line;
            while ((line = buffread.readLine()) != null) {
                stringBuilder.append(line);
            }
            buffread.close();
            strResponse = stringBuilder.toString();
            resCode = con.getResponseCode();
            ObjectMapper mapper = new ObjectMapper();
            try {
                Rides = mapper.readValue(strResponse, new TypeReference<ArrayList<Ride>>(){});
            } catch (JsonGenerationException ge) {
                System.out.println(ge);
            } catch (JsonMappingException me) {
                System.out.println(me);
            }
        } catch (IOException e){
            startActivity(new Intent(RideDetailsActivity.this, DriverOnlySplash.class).putExtra("Success Ride Posted", "Connection Error: \n ERROR \n"+ e + resCode + strResponse));
        }

        con.disconnect();
    }
}