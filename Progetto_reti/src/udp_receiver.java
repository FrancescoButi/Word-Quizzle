import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.net.DatagramPacket;
//import java.net.DatagramSocket;
//
//import javax.swing.JOptionPane;
//
//public class udp_receiver implements Runnable {
//
//	private int clientport;
//	private DatagramSocket dgs;;
//	
//	public udp_receiver(DatagramSocket dgs) {
//		this.dgs = dgs;
//	}
//
//	@Override
//	public void run() {
//		try {
//			//dgs.setSoTimeout(2000);
//			while (true) {
//				//Creo byte buffer per la rcv
//				byte [] rcvbuff = new byte [20];
//				//Creo dp per la ricezione
//				DatagramPacket rcvmsg = new DatagramPacket(rcvbuff, 20);
//				dgs.receive(rcvmsg);
//				String msg = new String(rcvbuff).toString();
//				String result = Client.show_match_notification (msg);
//				System.out.println(result);
//				if (msg.contains("iniziata")) {
//					Word_Quizzle_Client.showchallengeframe();
//				}
////				Sfida sfida = new Sfida (username, msg);
////				sfide.add(sfida);
//			}
//		} catch (IOException e1) {
//			System.err.println("Failed to create or operate on socket:");
//			e1.printStackTrace();
//		}
//		
//	}
//
//}
