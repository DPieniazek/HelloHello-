import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Arrays;


public class Message_zad3 {
	
	String nickname;
	String message;
	String date;
	byte[] checksum;
	boolean correctChecksum = false;
	
	public Message_zad3(String nickname, String message, String date){
		this.nickname = nickname;
		this.message = message;
		this.date = date;
		this.checksum = countChecksum();  // Mehod that counts the checkSum
		this.correctChecksum = true;		// This constructor is used before sending message
	}
	
	public Message_zad3(String nickname, String message, String date, byte[] checksum) {
		this.nickname = nickname;
		this.message = message;
		this.date = date;
		this.checksum = countChecksum();
		this.correctChecksum = Arrays.equals(this.checksum, checksum);	// Checking if counted checksum equals the one we recived from the message
	}
	

	private byte[] countChecksum(){
		String datagram = nickname + ":" + date + ":" + message;
		return datagram.getBytes();		
	}
	
	public byte[] messageToBytes() {
		ByteArrayOutputStream out = new ByteArrayOutputStream();	
		try {
			out.write(nickname.getBytes());	//The message has a format Nickname/Message/Data/Checksum.
			out.write(127);	// THe marking that divides the two values. The highest value allowed in ByteArrayOutputStream
			out.write(message.getBytes());
			out.write(127);
			out.write(date.getBytes());
			out.write(127);
			out.write(checksum);
			return out.toByteArray();		// Byte table ready to send.
		} catch (IOException e) {
			System.out.println("Error 5: Function out.write didn't work");
			e.printStackTrace();
		}
		return null;	
	}
	
	public static Message_zad3 recivedMessageFromBytes(byte[] recivedMessage){
		int search127 = 0;	// The value that helps to find the end of each send part of Datagram
		while(search127 < recivedMessage.length && recivedMessage[search127] != 127){
			search127++;	// Loop looks for the value 127
		}
		if(search127 == recivedMessage.length){
			System.out.println("Error 6: Incorrect message");
			return null;
		}	
		String nickname = new String(Arrays.copyOfRange(recivedMessage, 0, search127));
		int tmp127 = ++search127;
		while(search127 < recivedMessage.length && recivedMessage[search127] != 127){
			search127++;
		}
		if(search127 == recivedMessage.length){
			System.out.println("Error 6: Incorrect message");
			return null;
		}	
		String message = new String(Arrays.copyOfRange(recivedMessage, tmp127, search127));
		tmp127 = ++search127;
		while(search127 < recivedMessage.length && recivedMessage[search127] != 127){
			search127++;
		}
		if(search127 == recivedMessage.length){
			System.out.println("Error 6: Incorrect message");
			return null;
		}	
		String date = new String(Arrays.copyOfRange(recivedMessage, tmp127, search127));
		tmp127 = ++search127;
		byte[] checksum = Arrays.copyOfRange(recivedMessage, tmp127, recivedMessage.length);
		
		Message_zad3 returnValue = new Message_zad3(nickname, message, date, checksum);
		return returnValue;
	}
	
	public boolean hasCorrectChecksum(){
		return correctChecksum;
	}
}
