package cl.loicalab.tbt;

import android.location.Location;
import android.location.LocationListener;
import android.os.Bundle;

public class PositionLocation implements LocationListener {

	private String position = "";   
	private double latitude = 0, longitude= 0;;

	public void onLocationChanged(Location loc) {
		latitude = loc.getLatitude();
		longitude = loc.getLongitude();
		position = loc.getLongitude() + "," + loc.getLatitude();  
	}  

	public void onProviderDisabled(String provider) {  
		// TODO Auto-generated method stub  
	}  

	public void onProviderEnabled(String provider) {  
		// TODO Auto-generated method stub  
	}  

	public void onStatusChanged(String provider, int status, Bundle extras) {
		// 	TODO Auto-generated method stub
	}  

	public String getStringPosition(){
		return position;
	}
	
	public double getLatitude(){
		return latitude;
	}
	
	public double getLongitude() {
		return longitude;
	}
} 
