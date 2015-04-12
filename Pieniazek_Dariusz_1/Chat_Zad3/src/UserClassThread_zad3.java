import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.util.Calendar;
import java.util.Locale;
import java.util.Scanner;
import java.text.SimpleDateFormat;


public class UserClassThread_zad3 implements Runnable{
	
	DatagramSocket socket;
	String nickname; 
	InetAddress group;
	int port;
	SimpleDateFormat dataFormat = new SimpleDateFormat("HH:mm:ss dd-MM-YY", Locale.UK);
		
	UserClassThread_zad3(DatagramSocket socket, String nickname, InetAddress group, int port){
		this.socket = socket;
		this.nickname = nickname;
		this.group = group;
		this.port = port;
	}
		
	@Override
	public void run() {

		DatagramPacket packet;
		Scanner read = new Scanner(System.in);   // Scanner reads the inputed message from Users' consoles
		
		while(!Thread.interrupted()){
			String message = read.nextLine();
			
			while(message.length() > 20){
				System.out.println("Message max 20 characters, input message shorter than 20");
				message = read.nextLine();
			}	
			
			String date = dataFormat.format(Calendar.getInstance().getTime());
			
			Message_zad3 datagramToSend = new Message_zad3(nickname, message, date);
			// Message_zad3 - class that contains all sended attributes
			// It also has the methods to prepare messages to sending and reciving
			// Namely, messageToBytes() - changes message into byte table. (That table is afterwards send as DatagramPacket
			// The other function recivedMessageFromBytes() - changes the byte table back into the message
						
			byte[] buffer = datagramToSend.messageToBytes();
			packet = new DatagramPacket(buffer, buffer.length, group, port);
			try {
				socket.send(packet);
				// System.out.println("User: \nNickname: " + nickname + "\nMessage: " + message + "\nDate: " + date);
				// Uncomment to see send message, useful in tests
			} catch (IOException e) {
				System.out.println("Error 3: Function socket.send() didn't work");
				e.printStackTrace();
			}			
		}
		read.close();
	}

}
