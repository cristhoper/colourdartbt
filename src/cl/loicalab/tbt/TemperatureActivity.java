package cl.loicalab.tbt;


import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;

import com.google.android.maps.GeoPoint;

import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.util.Pair;
import android.view.View;
import android.webkit.WebView.FindListener;
import android.widget.ImageView;
import android.widget.TextView;
import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;

public class TemperatureActivity extends Activity {

	public static int MAX_TEMPERATURE = 30;
	public static int MIN_TEMPERATURE = -40;
	
	public String temperatureMax = "0";
	public String temperatureMin = "0";
	public String temperatureAct = "0";
	public Pair<String,String> temperatures = new Pair<String, String>("", "");
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.temperature);
		
		setColors(temperatureMin,temperatureMax,temperatureAct);
		getForecast();
	}
	
	public void onClick(View v){
		getTemperature();
	}
	
	public void setColors(String  minTemp, String maxTemp, String actualTemp){
		
		int min_temperature = Integer.valueOf(minTemp);
		int max_temperature = Integer.valueOf(maxTemp);
		int actual_temperature = Integer.valueOf(actualTemp);
		
		Resources res = this.getResources();
		TypedArray colors = res.obtainTypedArray(R.array.colors);

		
		float step = (float) ((float)(max_temperature-min_temperature)/6.0);
		int[] t_level = new int[6];
		int[] t_color = new int[6];
		Log.i("Step:",Float.toString(step));
		for(int i=0;i<6;i++){
			t_level[i] = Math.round(min_temperature + i*step);
			t_color[i] = colors.getColor(colorLevel(t_level[i]),0);
			Log.i("Color:",Integer.toString(t_level[i]) + " " +Integer.toString(t_color[i]));
		}
		
		
		View layout = findViewById(R.id.background_layout);

	    GradientDrawable gd = new GradientDrawable(GradientDrawable.Orientation.BOTTOM_TOP,t_color);
	    gd.setCornerRadius(0f);
	    layout.setBackgroundDrawable(gd);
	    
	
	}
	
	public int colorLevel(int temperature){
		return temperature+40;
	}

	public Location getLocation(){
		Criteria crtr = new Criteria();
		crtr.setPowerRequirement(Criteria.ACCURACY_FINE);
		LocationManager locationManager = (LocationManager)getSystemService(Context.LOCATION_SERVICE);
		PositionLocation locationListener = new PositionLocation();

		locationManager.requestLocationUpdates(locationManager.getBestProvider(crtr, false), 0, 0, locationListener);
		Location loc1 = locationManager.getLastKnownLocation(locationManager.getBestProvider(crtr, false));
		return loc1;
	}

	public void getTemperature(){
			if(getLocation()!=null){
				WheatherApiTask w = new WheatherApiTask();
				String t = null;
				w.execute(getLocation().getLatitude(),getLocation().getLongitude());
			}
	}
	
	public void getForecast(){
		if(getLocation()!=null){
			ForecastApiTask f = new ForecastApiTask();
			String t = null;
			f.execute(getLocation().getLatitude(),getLocation().getLongitude());
		}
}
	
	private class WheatherApiTask extends AsyncTask<Double, String, Integer> {

		protected Integer doInBackground(Double... latlon) {
	        	Weather weather = new Weather();
				temperatureAct = weather.readActualTemp(latlon[0], latlon[1]);
				return 0;
	    }

	    protected void onPostExecute(Integer result) {
				((TextView)findViewById(R.id.temperature_value)).setText(temperatureAct+"°C");

	    }
	}
	
	private class ForecastApiTask extends AsyncTask<Double, String, Integer> {

		protected Integer doInBackground(Double... latlon) {
	        	Weather weather = new Weather();
				temperatures = weather.readForecast(latlon[0], latlon[1]);
				return 0;
	    }

	    protected void onPostExecute(Integer result) {
				((TextView)findViewById(R.id.max_temperature)).setText(temperatures.second+"°C");
				((TextView)findViewById(R.id.min_temperature)).setText(temperatures.first+"°C");
				setColors(temperatures.first,temperatures.second,temperatureAct);
    	
	    }
	}
}


