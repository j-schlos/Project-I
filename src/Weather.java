import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.util.Scanner;

public class Weather extends Singleton{
    private static Weather instance = null;
    private static final String BASE_URL = "http://api.weatherapi.com/v1";
    private static final String CURRENT_URL_APPEND = "/current.json";
    private static final String API_KEY = "d2dd28c8913e46a2b1c162759232812";

    private URL url = null;

    private Weather(){

    }

    private String urlBuilder(String baseUrl, String urlAppend, String apikey, String query){
        StringBuilder sb = new StringBuilder();
        sb.append(baseUrl);
        sb.append(urlAppend);
        sb.append("?key=");
        sb.append(apikey);
        sb.append("&q=");
        sb.append(query);
        sb.append("&aqi=no");

        return sb.toString();
    }

    public static synchronized Weather getInstance(){
        if (instance == null)
            instance = new Weather();
        return instance;
    }

    private HttpURLConnection createConnection(String query) throws IOException{
        try {
            this.url = new URL(urlBuilder(BASE_URL, CURRENT_URL_APPEND, API_KEY, query));
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            return conn;
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

    private String getResponseData() throws IOException{
        StringBuilder sb = new StringBuilder();
        InputStream inputStream = url.openStream();
        Scanner scanner = new Scanner(inputStream);
        while(scanner.hasNext()){
            sb.append(scanner.nextLine());
        }
        scanner.close();
        inputStream.close();
        return sb.toString();
    }

    private String parseResponseData(){
        //TODO zpracovat json, který přijde v response
        return null;
    }

    public String getCurrentWeather(String query){
        try {
            HttpURLConnection connection = createConnection(query);
            sendGetRequest(connection);
            return getResponseData();
        } catch (IOException e){
            throw new RuntimeException(e);
        } catch (MyRequestException e){
            return e.getMessage();
        }
    }
}
