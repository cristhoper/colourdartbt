package cl.loicalab.tbt;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.text.DecimalFormat;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.util.Pair;

public class Weather {

	public Weather() {
	
	}

	public Pair<String,String> readForecast(double latitude, double longitude){
		String url = "http://api.openweathermap.org/data/2.5/forecast/daily?lat="+latitude+"&lon=" + longitude + "&cnt=2";
		String result = readAPI(url);
		String min = null, max = null;
		try{
			JSONObject content = new JSONObject(result);
			JSONArray temperatures = (JSONArray) content.get("list");
			JSONObject dayOne = (JSONObject) temperatures.get(0);
			JSONObject tempForecast  = (JSONObject) dayOne.get("temp");
			min = tempForecast.getString("min");
			max = tempForecast.getString("max");
			int max_temp = (int)(Double.parseDouble(max)-273.15);
			int min_temp = (int)(Double.parseDouble(min)-273.15);
			min = Integer.toString(min_temp);
			max = Integer.toString(max_temp);
		}
		catch(JSONException e){
			e.printStackTrace();
		}
		Pair<String, String> p = new Pair<String, String>(min, max);
		return p;
	}
		
	public String readActualTemp(double latitude, double longitude){
		String url = "http://api.openweathermap.org/data/2.5/weather?lat="+latitude+"&lon=" + longitude;
		String result =  readAPI(url);
		double temperature = 0.0;
		try{
			JSONObject content = new JSONObject(result);
			JSONObject temperatures = (JSONObject) content.get("main");
			temperature = (Double) temperatures.get("temp"); temperature -= 273.15;
		}
		catch(JSONException e){
			e.printStackTrace();
		}
		int f_t = (int) temperature;
		return Integer.toString(f_t);
	}
	
	private String readAPI(String url){
		String response = null;
		try {
            DefaultHttpClient httpClient = new DefaultHttpClient();
            HttpEntity httpEntity = null;
            HttpResponse httpResponse = null;

                HttpGet httpGet = new HttpGet(url);
 
                httpResponse = httpClient.execute(httpGet);
 
            httpEntity = httpResponse.getEntity();
            response = EntityUtils.toString(httpEntity);
 
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (ClientProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
         
        return response;

	}
}
