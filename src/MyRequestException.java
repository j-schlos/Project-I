public class MyRequestException extends Exception{
    private String message;
    public MyRequestException(){

    }

    public MyRequestException(String message){
        this.message = message;
    }

    public String getMessage(){
        return message;
    }

}
