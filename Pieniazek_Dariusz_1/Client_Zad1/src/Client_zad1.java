import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Scanner;

public class Client_zad1 {

	static Socket socket = null;
	static String numberToSendS;
	static int numberToSend;
	static String nOfBytesS;
	static int nOfBytes;
	/**
	 * @param args
	 * @throws IOException 
	 * @throws UnknownHostException 
	 * @throws NumberFormatException 
	 */
	public static void main(String[] args) throws NumberFormatException, UnknownHostException, IOException {

		if (args.length != 2) {
			System.out.println("Error 0: No agruments");		  // IP 0.0.0.0 for obvious reasons
			System.out.println("Input parameters: <IP_address> <port>");  // Any port (2000)
			System.exit(-1);
		}		
		//System.out.println("Args: IP address: " + args[0] + " Port: " + args[1]);

		boolean def = false; // The default value serves to save time. 
		if(!def){
			Scanner read = new Scanner(System.in);
	        System.out.print("Choose one:\n1:\tSend one-byte number\n2:\tSend two-byte number\n4:\tSend four-byte number\n8:\tSend eight-byte number\nYour Number: ");
	        nOfBytesS = read.nextLine();      
			try {
				nOfBytes = Integer.parseInt(nOfBytesS);
			} catch (NumberFormatException e) {
				System.out.println("Error 1: The chosen value was not integer. Enabling default. 4 four-byte ");
				nOfBytes = 4;
			}
			
			System.out.print("Input "+ nOfBytes +"-byte number to send");
			numberToSendS = read.nextLine();
	        read.close();
			try {
				numberToSend = Integer.parseInt(numberToSendS);
			} catch (NumberFormatException e) {
				System.out.println("Error 2: The chosen value was not a number. Enabling default number ");
				def = true;
			}
			
			switch(nOfBytes){	//Checks whatever the n-byte number was indeed n-byte number
				case 1:			//if someone wishes to send 10 as 8 byte number they have to input it as "00000010"
					if(numberToSendS.length() != 1){
						System.out.println("Wrong number, enabling default");
						def = true;
					}
					break;
				case 2:
					if(numberToSendS.length() != 2){
						System.out.println("Wrong number, enabling default");
						def = true;
					}
					break;
				case 4:
					if(numberToSendS.length() != 4){
						System.out.println("Wrong number, enabling default");
						def = true;
					}
					break;
				case 8:
					if(numberToSendS.length() != 8){
						System.out.println("Wrong number, enabling default");
						def = true;
					}
					break;
				default:
			}	
		}
		if(def){		// If there were problems in configuration the client asks for normal number
			nOfBytesS = "4";      
	        nOfBytes = 4;			
			numberToSendS = "0010";
	        numberToSend = 10;
		}
	
		try {
			// There are some comments that are leftover from the Lab example because I was using that code as a base
			// They are still useful so there is noreason to remove them.
			socket = new Socket(args[0], Integer.parseInt(args[1]));
			
			InputStream in = socket.getInputStream(); // - get input stream from socket
			OutputStream out = socket.getOutputStream(); // - get output stream from socket
					
			byte sendline[] = numberToSendS.getBytes();
			byte recvline[] = new byte[1]; // - just create byte buffer with some arbitrary length
			
			out.write(sendline); // send data by invoking write method on output stream passing sendline byte buffer						
			System.out.println("sent bytes: " + sendline.length); // print the length of sent data (length of sendline buffer)
			System.out.println("sent: " + new String(sendline)); // print sent data by passing sendline buffer to String constructor
			
			in.read(recvline); // receive data by invoking read method on output stream
			System.out.println("received bytes: " + recvline.length); // print the length of received data (returned from read method)
			System.out.println("received: " + new String(recvline, 0, 1)); // print received data by passing recvline buffer to String contructor with index and offset

			socket.close();			
		} catch (NumberFormatException e) {
			e.printStackTrace();
		} catch (UnknownHostException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (socket != null)
				try { socket.close(); }
				catch (IOException e1) {}
		}		
	}
}

