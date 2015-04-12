import java.io.IOException;
import java.net.InetAddress;
import java.net.MulticastSocket;


public class Chat_zad3 {

	/**
	 * @param args <Multicast address> <port> <Nickname>
	 * @throws InterruptedException 
	 */
	public static void main(String[] args) throws InterruptedException {
		if (args.length != 3) {
			System.out.println("Error 0: No agruments");		
			System.out.println("Input parameters: <Multicast address> <port> <nickname>"); 
			System.exit(-1);
		}
		// multicast address are in range 224.0.0.1 and 239.255.255.254. Tested 238.0.0.1
		int port = 0;
		try{
			port = Integer.parseInt(args[1]);
		} catch(NumberFormatException e){
			System.out.println("Error 1: Incorrect <port> argument, enter correct port");
			System.exit(-1);
		}
		if(args[2].length() > 6){
			System.out.println("Error 2: Incorrect <nickname> argument, Nickname's length max 6 characters");
			System.exit(-1);
		}
		
		try {
			MulticastSocket socket = new MulticastSocket(port);
			InetAddress Chat_group = InetAddress.getByName(args[0]);
			socket.joinGroup(Chat_group);
			
			// Thread for listening
			ChatClassThread_zad3 chat = new ChatClassThread_zad3(socket, args[2]);
			Thread chatThread = new Thread(chat);
			
			// Thread for sending messages
			UserClassThread_zad3 user = new UserClassThread_zad3(socket, args[2], Chat_group, port);
			Thread userThread = new Thread(user);
			
			userThread.start();
			chatThread.start();
			
			try{
				userThread.join();
				chatThread.join();
			} finally {
				socket.leaveGroup(Chat_group);
				socket.close();
			}						
		} catch (IOException e) {
			e.printStackTrace();
		}		
	}
}
