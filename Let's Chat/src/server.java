
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.sql.*;

public class server { 

	ServerSocket server;	//Socket Programming
	Socket socket;
	BufferedReader br;	// for reading 
	
	PrintWriter out;	// for writing

	//constructor
	public server()	
	{
		try {
			server = new ServerSocket(8080);	//for networking 
			System.out.println("server is waiting");
			socket=server.accept();
			
			br = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			out = new PrintWriter(new OutputStreamWriter(socket.getOutputStream()));
		
			//method calling
		reading();	
		writing();
		}
		catch(Exception e) {
			e.printStackTrace();
		}
	}
	public void reading() {
		
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
					
					}catch(Exception e){ System.out.println(e);}
			}
		};
//		threading
		new Thread(r1).start();	// 
		
	}
	
	public void writing() {
		Runnable r2 = ()->{
			while(true) {
				try {
					BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
					String content = br.readLine();
					out.println("Server>>"+content);
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
			System.out.println("hello this is server");
			new server();
	}
	
}