import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.util.Arrays;


public class ChatClassThread_zad3 implements Runnable{

	public static int bufflen = 100;
	
	DatagramSocket socket;
	String nickname; 
	
	ChatClassThread_zad3(DatagramSocket socket, String nickname){
		this.socket = socket;
		this.nickname = nickname;
	}
	
	@Override
	public void run() {
		DatagramPacket packet;
		byte[] buffer = new byte[bufflen];
		
		while(!Thread.interrupted()){
			packet = new DatagramPacket(buffer, buffer.length);
			try{
				socket.receive(packet);
				Message_zad3 recived = Message_zad3.recivedMessageFromBytes(Arrays.copyOfRange(packet.getData(), 0, packet.getLength()));
								
				if(recived.hasCorrectChecksum() && !recived.nickname.equals(this.nickname)){
					System.out.println("Server: \nNickname: " + recived.nickname + "\nMessage: " + recived.message + "\nDate: " + recived.date);				
				} /*else if(recived.hasCorrectChecksum()){ 
					System.out.println("Server: \nNickname: " + recived.nickname + "\nMessage: " + recived.message + "\nDate: " + recived.date);
				}*/ else if(!recived.hasCorrectChecksum())
					System.out.println("Checksum error");
			} catch (IOException e){
				System.out.println("Error 4: Function socket.receive() didn't work");
				e.printStackTrace();
			}
		}
	}

}
