public class Deadlock
{
    private Thread t1;
    private Thread t2;

    private final String res1 = "Domine";
    private final String res2 = "Ameno";


    public Deadlock()
    {
        t1 = new Thread(() -> {
            synchronized(res1)
            {
                System.out.println("T1 Locked " + res1);
                try
                {
                    Thread.sleep(5000);
                }
                catch (Exception e)
                {
                    e.printStackTrace();
                }
                System.out.println("T1 Waiting for " + res2);
            }

            synchronized(res2)
            {
                System.out.println("T1 Locked " + res2);
            }
        });

        t2 = new Thread( () ->
        {
            synchronized(res2)
            {
                System.out.println("T2 Locked " + res2);
                try
                {
                    Thread.sleep(5000);
                }
                catch (Exception e)
                {
                    e.printStackTrace();
                }
                System.out.println("T2 Waiting for " + res1);
            }

            synchronized(res2)
            {
                System.out.println("T2 Locked " + res1);
            }
        });

        t1.run();
        t2.run();

    }
}
