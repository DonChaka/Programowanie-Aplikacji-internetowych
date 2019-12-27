import java.io.IOException;
import java.util.List;

public class ClientHandler extends Thread
{
    boolean shoudRun = true;

    List<Client> clients;
    Client client;

    public ClientHandler(List<Client> clients, Client client)
    {
        this.client = client;
        this.clients = clients;
    }

    @Override
    public void run()
    {

        while(shoudRun)
        {
            try
            {
                String text = (String) client.inputStream.readObject();
                System.out.println(text);
                if(text != null)
                    for (Client iterator : clients)
                    {
                        System.out.println(iterator);
                        iterator.outputStream.writeObject(text);
                    }
            }
            catch (IOException e)
            {
                shoudRun = false;
                ChatServer.disconnectClient(this);
            }
            catch (Exception e)
            {
                e.printStackTrace();
                System.out.println("2");
                shoudRun = false;
            }
        }
    }
}
