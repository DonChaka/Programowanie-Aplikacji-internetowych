import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


public class Main
{
    public static void main(String[] args)
    {
        ExecutorService thread = Executors.newFixedThreadPool(5);

        if(args[0].equalsIgnoreCase("deadlock"))
        {
            Deadlock deadlock = new Deadlock();
        }

    }

}
