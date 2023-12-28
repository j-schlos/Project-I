import java.io.Console;

// Press Shift twice to open the Search Everywhere dialog and type `show whitespaces`,
// then press Enter. You can now see whitespace characters in your code.
public class Main{


    public static void main(String[] args) {
        ConsoleHelper console = ConsoleHelper.getInstance();
        console.printLine("\r\n\r\n########## Current Weather ##########");
        String query = console.readString("Enter city name");
        Weather weather = Weather.getInstance();
        String response = weather.getCurrentWeather(query);
        console.printLine(response);
    }
}