import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketException;
import org.omg.CORBA.ORB;
import org.omg.CosNaming.NameComponent;
import org.omg.CosNaming.NamingContextExt;
import org.omg.CosNaming.NamingContextExtHelper;
import org.omg.PortableServer.POA;
import org.omg.PortableServer.POAHelper;
import DEMSApp.DEMS;
import DEMSApp.DEMSHelper;

public class MTLServer {
	
	public static void main(String args[]) 
	{
		try {
			
				ORB orb = ORB.init(args, null);
						
					
				POA rootpoa = POAHelper.narrow(orb.resolve_initial_references("RootPOA"));
				rootpoa.the_POAManager().activate();

						
				DEMSImplement obj1 = new DEMSImplement();
				obj1.servername="MTLServer";
				obj1.setORB(orb);

				org.omg.CORBA.Object ref = rootpoa.servant_to_reference(obj1);
						
						
						
				//DEMS href = DEMSHelper.narrow(ref);

					
				org.omg.CORBA.Object objRef = orb.resolve_initial_references("NameService");
						
						
				NamingContextExt ncRef = NamingContextExtHelper.narrow(objRef);

						
				NameComponent path[] = ncRef.to_name("MTLServer");
				ncRef.rebind(path, ref);

				System.out.println("MTL Server is up and running");

						
				//for (;;) 
				//{
					//orb.run();
					//System.out.println("run after");
				//}
			
			
		}catch (Exception e) {
			System.err.println("ERROR: " + e);
			e.printStackTrace(System.out);}
		
		
		
		DatagramSocket aSocket = null;
		try{
			//System.out.println("inside try");
	    	aSocket = new DatagramSocket(6789);
			byte[] buffer = new byte[1000];
			
 			while(true){
 				DatagramPacket request = new DatagramPacket(buffer, buffer.length);
  				aSocket.receive(request);
  				giveMTLResponse resp=new giveMTLResponse(aSocket,request,args);
  				Thread t=new Thread(resp);
				t.start();
 
    			
    		}
		}catch (SocketException e){System.out.println("Socket: " + e.getMessage());
		}catch (IOException e) {System.out.println("IO: " + e.getMessage());
		}
		
	
	
	
	
	}
	

}


class giveMTLResponse implements Runnable
{

	DatagramSocket aSocket;
	DatagramPacket request;
	String[] receivedData;
	String[] args;
	String getData;
	byte[] m;
	public giveMTLResponse(DatagramSocket aSocket,DatagramPacket request,String[] args)
	{
		this.aSocket=aSocket;
		this.request=request;
		this.args=args;
	}
 	
	
	@Override
	public void run() {
		receivedData=new String(request.getData()).split("\\,");
		
		DEMS obj;
		try
		{
			ORB orb=ORB.init(args,null);
			//-ORBInitialPort 1050 -ORBInitialHost localhost
			org.omg.CORBA.Object objRef = orb.resolve_initial_references("NameService");
			NamingContextExt ncRef = NamingContextExtHelper.narrow(objRef);
			obj = (DEMS) DEMSHelper.narrow(ncRef.resolve_str(receivedData[0]));
			switch(receivedData[1])
			{
			case "listEvent":
				//System.out.println("HI LIST EVENT");
				getData=obj.listEvent(receivedData[2].concat("temp"));
				break;
			case "bookEvent":
				System.out.println("received eventid Data is: "+receivedData[3]);
				System.out.println("received CustID Data is: "+receivedData[4]);
				System.out.println("received eventtype Data is: "+receivedData[2]);
				getData=obj.bookEvent(receivedData[4],receivedData[3],receivedData[2]);
				System.out.println("Data is: "+getData );
				break;
			/*case "getSchedule":
				getData=obj.getSchedule("t".concat(receivedData[4]));
				System.out.println("Data is: "+getData );
				break;*/
			case "cancelEvent":
				getData=obj.cancelEvent(receivedData[4],receivedData[3],receivedData[2]);
				System.out.println("Data is: "+getData );
				break;
			case "removeEvent":
				getData=obj.removeEvent("temp".concat(receivedData[3]),receivedData[2]);
				System.out.println("Data is: "+getData );
				break;
			}
		}catch (Exception e) {System.out.println("Thread: " + e.getMessage());}
		
			
		try
		{
			m=getData.getBytes();
			DatagramPacket reply =new DatagramPacket(m,  getData.length(), request.getAddress(), request.getPort());
			aSocket.send(reply);
		}catch (SocketException e){System.out.println("Socket: " + e.getMessage());
		}catch (IOException e){System.out.println("IO: " + e.getMessage());
		}
		
	}
	
}

