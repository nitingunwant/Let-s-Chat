
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;
import java.sql.*;

public class client {
//	private static PrintStream msg;
	
		Socket socket;	//Socket Programming
		
		BufferedReader br;	// for reading
		PrintWriter out;	// for writing
		
		public client()	//constructor
		{
			try {
				System.out.println("sending request");
				socket=new Socket("Gunwant", 8080);
				System.out.println("Connection Done");
			
				br = new BufferedReader(new InputStreamReader(socket.getInputStream()));
				out = new PrintWriter(new OutputStreamWriter(socket.getOutputStream()));
				
				//method calling
			startReading();
			startWriting();
			}
			catch(Exception e) {
				e.printStackTrace();
			}
		}
		private void startReading() {
			Runnable r1 = ()-> {
				while(true) {
					try {
						String msg  = br.readLine();
						Class.forName("com.mysql.cj.jdbc.Driver");  
						Connection con=DriverManager.getConnection(  
						"jdbc:mysql://localhost:3306/test","root","nitinmysql");  
						
						Statement stmt=con.createStatement();
						
						stmt.executeUpdate("insert into tb(message) values('"+msg+"')");
						con.close();
						System.out.println(msg);
						if(msg.equals("exit")) {
							System.out.println("connection terminated");
							break;
						}
						
						}
					catch(Exception e) {
						e.printStackTrace();
					}
				}
			};
			//threading
			new Thread(r1).start();	
		}
		
		private void startWriting() {
			Runnable r2 = ()->{
				while(true) {
					try {
						BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
						String content = br.readLine();
						out.println("Client>>"+content);
						out.flush();
					}
					catch(Exception e){
						e.printStackTrace();
					}
				}
			};
			
			new Thread(r2).start();
		}

	public static void main(String[] args) {
		try{  
			Class.forName("com.mysql.cj.jdbc.Driver");  
			Connection con=DriverManager.getConnection(  
			"jdbc:mysql://localhost:3306/test","root","nitinmysql");  
			Statement stmt=con.createStatement();
			ResultSet rs= stmt.executeQuery("select*from tb");
			while(rs.next())
				System.out.println(rs.getString(1)+" ");
			con.close();
			}catch(Exception e) {
			e.printStackTrace();}
		
		System.out.println("this is client");
		new client();

	}

}
