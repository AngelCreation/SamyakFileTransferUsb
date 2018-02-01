package samyak;

import java.io.BufferedInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.Socket;

public class MyClient {

	public static void main(String[] args) {
		OutputStream outputStream = null;
		Socket socket = null;
		final String FILE_TO_SEND = "D:\\Shreyansh\\message.txt";
		FileInputStream fis = null;
		BufferedInputStream bis = null;

		try {
			socket = new Socket("127.0.0.1", 6667);

			File source = new File(FILE_TO_SEND);
			byte[] mybytearray = new byte[(int) source.length()];
			bis = new BufferedInputStream(new FileInputStream(source));
			bis.read(mybytearray, 0, mybytearray.length);
			outputStream = socket.getOutputStream();
			System.out.println("Sending " + FILE_TO_SEND + "(" + mybytearray.length + " bytes)");
			outputStream.write(mybytearray, 0, mybytearray.length);

			// byte buf[] = mybytearray;
			// int len;
			// while ((len = bis.read(buf)) != -1) {
			// outputStream.write(buf, 0, len);
			// }

			// DataOutputStream dout = new
			// DataOutputStream(socket.getOutputStream());
			// dout.writeUTF("Hello Shreyansh");
			// dout.flush();
			// dout.close();

			outputStream.flush();
			System.out.println("Done.");
			outputStream.close();
			bis.close();
			socket.close();

		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
