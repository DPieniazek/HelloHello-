import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.Socket;
import java.net.UnknownHostException;

public class Client_zad2 {

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
	public static void main(String[] args) {

		if (args.length != 3) {
			System.out.println("Error 0: No agruments");		  // 0.0.0.0 - IP dowolny
			System.out.println("Input parameters: <IP> <port> <file_name>");  // 2000
			System.exit(-1);
		}
		
		//System.out.println("Args: IP address: " + args[0] + " Port: " + args[1] + " FileName: " +args[2]);
		
		String filename = "src/"+args[2];  // It somehow doesn't work in eclipse
		
		File file = new File(filename);
		//File file = new File("src/file_to_send.txt");
		byte sendline[] = new byte[50];
		byte sendName[] = args[2].getBytes();
		
		try {
			socket = new Socket(args[0], Integer.parseInt(args[1]));
			
			OutputStream out = socket.getOutputStream(); // - get output stream from socket
			out.write(sendName);
				
			int read;
			FileInputStream is = new FileInputStream(file);
			while((read = is.read(sendline)) > 0){
				out.write(sendline, 0, read);
				out.flush();
				//System.out.println("sent bytes: " + sendline.length); // print the length of sent data (length of sendline buffer)
				//System.out.println("sent: " + new String(sendline));  // print the message			
			}
			is.close();
			socket.close();	
				
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (socket != null)
				try { socket.close(); }
				catch (IOException e1) {}
		}	
			
	}
}

