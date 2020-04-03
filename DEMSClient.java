import org.omg.CORBA.ORB;
import org.omg.CosNaming.NamingContextExt;
import org.omg.CosNaming.NamingContextExtHelper;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.logging.FileHandler;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;
import DEMSApp.DEMS;
import DEMSApp.DEMSHelper;

public class DEMSClient {
	
	static final Logger log=Logger.getLogger(DEMSClient.class.getName());
	static FileHandler file1=null;	
	static String path="/home/vijay/java/DEMSCorba/src/client";
	static String filename;
	static SimpleFormatter formatter = new SimpleFormatter();
	
	public static void main(String[] args) 
	{
		try 
		{
			Scanner sc=new Scanner(System.in);
			System.out.println("Enter the input folder:");
			
			String fold=sc.next();
			
			File folder = new File("/home/vijay/java/DEMSCorba/src/"+fold+"/");
			File[] listOfFiles = folder.listFiles();
			ArrayList<Thread> threads=new ArrayList<Thread>();
			ArrayList<Multithreading> messages=new ArrayList<Multithreading>();

			 
			for(File file:listOfFiles)
			{
					//System.out.println("file name:"+file);
					ArrayList<String> lines = new ArrayList<String>();;
					
					 BufferedReader br = new BufferedReader(new FileReader(file));
					 String st; 
					 while ((st = br.readLine()) != null) 
					  {
					    lines.add(st); 
					  }
					String[] datas = lines.toArray(new String[lines.size()]);
					/*for(String i:datas)
					{
						System.out.println(i);
					}*/
					
					ORB orb = ORB.init(args, null);
					//-ORBInitialPort 1050 -ORBInitialHost localhost
					org.omg.CORBA.Object objRef = orb.resolve_initial_references("NameService");
					NamingContextExt ncRef = NamingContextExtHelper.narrow(objRef);
					DEMS obj = null;
					
					
				
					if(datas[0].contains("MTL"))
					{
						
						obj = (DEMS) DEMSHelper.narrow(ncRef.resolve_str("MTLServer"));
					}
					else if(datas[0].contains("QUE"))
					{
						obj = (DEMS) DEMSHelper.narrow(ncRef.resolve_str("QUEServer"));
					}
					else if(datas[0].contains("SHE"))
					{
						obj = (DEMS) DEMSHelper.narrow(ncRef.resolve_str("SHEServer"));
					}
					
					
					Multithreading threadobj=new Multithreading(obj,datas);
					messages.add(threadobj);
					
					Thread t=new Thread(threadobj);
					threads.add(t);
					t.start();
					br.close();
			}
			for(int j=0;j<threads.size();j++)
			{
				while(threads.get(j).isAlive())
				{
					if(messages.get(j).msg!=null)
					{
						threads.get(j).interrupt();
					}
				}
			}
			
			for(int j=0;j<threads.size();j++)
			{
				filename=path+messages.get(j).datas[0]+".txt";
				file1=new FileHandler(filename,true);
				file1.setFormatter(formatter);
				log.addHandler(file1);
				log.setUseParentHandlers(false);
				log.info(messages.get(j).loginfo);
				file1.close();
				
			}
			
			
		}catch (Exception e) {
			System.out.println("Hello Client exception: " + e);
			e.printStackTrace();}
	}
}
			
			
			
			
			
		

