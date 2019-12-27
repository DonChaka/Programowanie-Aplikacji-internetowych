import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class ChatServer
{
    public static ServerSocket server;
    public static List<Client> clients;

    public static List<ClientHandler> threads;

    public ChatServer() throws IOException
    {
        clients = new ArrayList<>();
        threads = new ArrayList<>();
        server = new ServerSocket(2137);
        while(true)
        {
            try
            {
                Socket socket = server.accept();
                Client client = new Client(socket);

                clients.add(client);
                ClientHandler thread = new ClientHandler(clients, client);

                threads.add(thread);
                threads.get(threads.size() - 1).start();
                System.out.println("Connected");
                System.out.println("Clients size: " + clients.size() + " threads size: " + threads.size());
            }
            catch (Exception e)
            {
                e.printStackTrace();
                server.close();
                break;
            }
        }
    }

    public static void disconnectClient(ClientHandler handler)
    {
        clients.remove(handler.client);

        threads.remove(handler);

        System.out.println("Disconnected");
        System.out.println("Clients size: " + clients.size() + " threads size: " + threads.size());
    }

    public  static void main(String[] args) throws IOException
    {
        ChatServer server = new ChatServer();
    }
}
