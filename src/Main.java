import java.io.Console;

// Press Shift twice to open the Search Everywhere dialog and type `show whitespaces`,
// then press Enter. You can now see whitespace characters in your code.
public class Main{


    public static void main(String[] args) {
        ConsoleHelper console = ConsoleHelper.getInstance();
        console.printLine("\r\n\r\n########## Weather Forecast ##########");
        String query = console.readString("Enter city name");
        int days = console.readInt("Enter number of days for forecast (1-10)");
        String alerts = console.readString("Do you also want to see alerts? (yes/no)");
        Weather weather = Weather.getInstance();
        String response = weather.getWeatherForecast(query, days, alerts);
        console.printLine(response);
    }
}