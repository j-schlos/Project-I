import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.util.List;
import java.util.Objects;
import java.util.Scanner;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.*;

public class Weather extends Singleton{
    private static Weather instance = null;
    private static final String BASE_URL = "http://api.weatherapi.com/v1";
    private static final String CURRENT_URL_APPEND = "/current.json";
    private static final String API_KEY = "d2dd28c8913e46a2b1c162759232812";

    private URL url = null;

    private Weather(){

    }

    private String urlBuilder(String baseUrl, String urlAppend, String apikey, String query){
        return baseUrl + urlAppend + "?key=" + apikey + "&q=" + query + "&aqi=no";
    }

    public static synchronized Weather getInstance(){
        if (instance == null)
            instance = new Weather();
        return instance;
    }

    private HttpURLConnection createConnection(String urlAppend, String query) throws IOException{
        try {
            this.url = new URL(urlBuilder(BASE_URL, urlAppend, API_KEY, query));
            return (HttpURLConnection) this.url.openConnection();
        } catch (MalformedURLException e) {
            throw new RuntimeException(e);
        }
    }
    private void sendGetRequest(HttpURLConnection connection) throws IOException, MyRequestException{
        try {
            connection.setRequestMethod("GET");
            connection.connect();
            int responseCode = connection.getResponseCode();
            if (responseCode != 200){
                throw new MyRequestException("Request for API failed, response code: " + responseCode + "\r\nThere could be a problem with an api key or given city name was not found.");
            }
        } catch (ProtocolException e) {
            throw new RuntimeException(e);
        }
    }

    private String getJSONValue(JSONObject object, String key){
        JSONObject currentObject = object;
        if(key.contains(":")){
            String[] keySplit = key.split(":", 2);
            currentObject = (JSONObject) currentObject.get(keySplit[0]);
            return getJSONValue(currentObject, keySplit[1]);
        }
        Object o = currentObject.get(key);
        return o.toString();
    }

    private String stringAssembler(String fieldName, JSONObject jsonObject, String jsonKey, String units){
        return "\t" + fieldName + ": " + getJSONValue(jsonObject, jsonKey) + " " + units + "\r\n";
    }

    private String stringAssembler(String fieldName, JSONObject jsonObject, String jsonKey){
        return stringAssembler(fieldName, jsonObject, jsonKey, "");
    }

    private String parseJSONResponseData(String responseData){
        String parsed = "\r\n";
        try{
            JSONObject jsonObject = (JSONObject) new JSONParser().parse(responseData);
            parsed += "\t\t\t[LOCATION INFO]\r\n";
            parsed += stringAssembler("Country", jsonObject, "location:country");
            parsed += stringAssembler("Region", jsonObject, "location:region");
            parsed += stringAssembler("City", jsonObject, "location:name");
            parsed += "\r\n\t\t\t[WEATHER INFO]\r\n";
            parsed += "\t--------Temperature----------\r\n";
            parsed += stringAssembler("Current Temperature", jsonObject, "current:temp_c", "°C");
            parsed += stringAssembler("Perceived Temperature", jsonObject, "current:feelslike_c", "°C");
            parsed += "\t--------Other Info-----------\r\n";
            parsed += stringAssembler("Weather Condition", jsonObject, "current:condition:text");
            parsed += stringAssembler("UV Index", jsonObject, "current:uv");
            parsed += stringAssembler("Pressure", jsonObject, "current:pressure_mb", "mbar");
            parsed += stringAssembler("Humidity", jsonObject, "current:humidity", "%");
            parsed += stringAssembler("Precipitation amount", jsonObject, "current:precip_mm", "mm");
            parsed += "\t---------Wind Info-----------\r\n";
            parsed += stringAssembler("Wind Speed", jsonObject, "current:wind_kph", "km/h");
            parsed += stringAssembler("Wind Direction", jsonObject, "current:wind_dir");
            parsed += stringAssembler("Wind Degrees", jsonObject, "current:wind_degree", "°");
            parsed += stringAssembler("Wind Gust", jsonObject, "current:gust_kph", "km/h");
            parsed += "\r\n\t\t\t[TIME INFO]\r\n";
            parsed += stringAssembler("Timezone name", jsonObject, "location:tz_id");
            parsed += stringAssembler("Local Time", jsonObject, "location:localtime");
            parsed += stringAssembler("Weather Info Last Update Time:", jsonObject, "current:last_updated");
        } catch(ParseException e){
            throw new RuntimeException(e);
        }
        return parsed;
    }
    private String getResponseData() throws IOException{
        StringBuilder sb = new StringBuilder();
        InputStream inputStream = url.openStream();
        Scanner scanner = new Scanner(inputStream);
        while(scanner.hasNext()){
            sb.append(scanner.nextLine());
        }
        scanner.close();
        inputStream.close();
        return parseJSONResponseData(sb.toString());
    }

    public String getCurrentWeather(String query){
        try {
            HttpURLConnection connection = createConnection(CURRENT_URL_APPEND, query);
            sendGetRequest(connection);
            return getResponseData();
        } catch (IOException e){
            throw new RuntimeException(e);
        } catch (MyRequestException e){
            return e.getMessage();
        }
    }
}
