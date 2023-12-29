import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class ConsoleHelper extends Singleton{
    private static ConsoleHelper instance = null;

    private ConsoleHelper(){

    }

    public static synchronized ConsoleHelper getInstance(){
        if(instance == null)
            instance = new ConsoleHelper();
        return instance;
    }

    public void print(String message){
        System.out.print(message);
    }

    public void printLine(String message){
        System.out.println(message);
    }

    public String readString(){
        try{
            BufferedReader reader = new BufferedReader(
                new InputStreamReader(System.in)
            );
            String text = reader.readLine();
            return text;
        }
        catch(IOException e) {
            printLine("Error occured: " + e.getMessage());
        }
        return "";
    }

    public String readString(String message) {
        print(message + ": ");
        return readString();
    }

    public int readInt(String message) {
        String input = readString(message);
        return Integer.parseInt(input);
    }

}
