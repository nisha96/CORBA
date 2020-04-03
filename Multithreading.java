import java.text.ParseException;
import java.util.Arrays;
import DEMSApp.DEMS;

public class Multithreading  implements Runnable
{
	
	String managerID,custID,eventID,eventtype,oldeventID,oldeventtype,neweventID,neweventtype,tempID,method,loginfo,msg;
	int capacity;
	String file;
	String[] datas;
	
	DEMS obj;

	
	
	public Multithreading(DEMS obj,String[] datas)
	{
		this.obj=obj;
		this.datas=datas;
	}
	@Override
	public void run() 
	{
		Thread.currentThread().setName(datas[0]);
		//System.out.println("current thread: "+Thread.currentThread().getName());
		if(datas[0].charAt(3)=='M')
		{
			managerID=datas[0];
			switch(datas[1])
			{
				case "addEvent":
					//System.out.println("Enter the Event Type: ");
					eventtype=datas[2];
					//System.out.println("Enter the Event ID: ");
					eventID=datas[3];
					//System.out.println("Enter the Capacity: ");
					capacity=Integer.parseInt(datas[4]);
					if(managerID.substring(0,3).equals(eventID.substring(0,3)))
					{
						msg=managerID+": "+eventID+" "+obj.addEvent(eventID, eventtype, capacity);
						loginfo="\nEvent Type:"+eventtype+"\nEvent ID:"+eventID+"\nCapacity:"+capacity+"\n"+msg;
						System.out.println(msg);
					}
					else
					{
						msg=managerID+":\n"+"you can not add events for other cities";
						loginfo=msg;
						System.out.println(msg);
					}
					
					break;
					
				case "removeEvent":
					//System.out.print("Enter the Event Type: ");
					eventtype=datas[2];
					//System.out.print("Enter the Event ID: ");
					eventID=datas[3];
					if(managerID.substring(0,3).equals(eventID.substring(0,3)))
					{
						msg=managerID+":\n"+obj.removeEvent(eventID, eventtype);
						loginfo="\nEvent Type:"+eventtype+"\nEvent ID:"+eventID+"\n"+msg;
						System.out.println(msg);
					}
					else
					{
						msg=managerID+":\n"+"you can not remove events from other cities";
						loginfo=msg;
						System.out.println(msg);
					}
					
					break;
					
				case "listEvent":
					//System.out.print("Enter the Event Type: ");
					eventtype=datas[2];
					//String callee="manager";
					msg=managerID+":\n"+obj.listEvent(eventtype);
					loginfo="\nEvent Type:"+eventtype+"\nlist of events available displayed";
					System.out.println(msg);
					break;
					
				case "customerOperation":
					//System.out.print("Enter the Customer ID: ");
					custID=datas[2];
					if(!(custID.substring(0,3).equals(managerID.substring(0,3))))
					{
						msg=managerID+":\n"+"You cant perform service for other city customers";
						loginfo=msg;
						System.out.println(msg);
						break;
					}
					else
					{
						String[] tempdatas=Arrays.copyOfRange(datas,2,datas.length+1);
							String temp = null;
							try {
								temp=customerfun(tempdatas,obj);
							} catch (ParseException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
						msg=managerID+":"+temp;
						System.out.println(msg);
						loginfo="\nCustomer ID:"+custID+"\nServices performed";
					}
			
				
			}
		}
		else if(datas[0].charAt(3)=='C')
		{
			//String msg = null;
			try {
				loginfo = customerfun(datas,obj);
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			//System.out.println(msg);
		}
			  
	}
	public static String customerfun(String[] datas,DEMS obj) throws ParseException
	{
		
		String custID,eventID,eventtype,oldeventID,oldeventtype,neweventID,neweventtype,msg,loginfo;
		
		
		custID=datas[0];
		
		switch(datas[1])
		{
		case "bookEvent":
			//System.out.println("Enter the Event Type: ");
			eventtype=datas[2];
			//System.out.println("Enter the Event ID: ");
			eventID=datas[3];
			msg=obj.bookEvent(custID,eventID, eventtype);
			loginfo="\nCustomer ID:"+custID+"\nEvent Type:"+eventtype+"\nEvent ID:"+eventID+"\n"+msg;
			System.out.println(custID+": "+msg);
			//break;
			return loginfo;
			
		case "getSchedule":
			msg=obj.getSchedule(custID);
			loginfo="\nCustomer ID:"+custID+"\nschedule displayed";
			System.out.println(custID+": "+msg);
			return loginfo;
			//break;
			
		case "cancelEvent":
			//System.out.println("Enter the Event Type: ");
			eventtype=datas[2];
			//System.out.println("Enter the Event ID: ");
			eventID=datas[3];
			msg=obj.cancelEvent(custID,eventID, eventtype);
			loginfo="\nCustomer ID:"+custID+"\nEvent Type:"+eventtype+"\nEvent ID:"+eventID+"\n"+msg;
			System.out.println(custID+": "+msg);
			return loginfo;
			//break;
		case "swapEvent":
			//System.out.println("Enter the old Event Type: ");
			oldeventtype=datas[2];
			//System.out.println("Enter the old Event ID: ");
			oldeventID=datas[3];
			//System.out.println("Enter the new Event Type: ");
			neweventtype=datas[4];
			//System.out.println("Enter the new Event ID: ");
			neweventID=datas[5];
			msg=obj.swapEvent (custID,neweventID,neweventtype,oldeventID,oldeventtype);
			loginfo="\nCustomer ID:"+custID+"\nold Event Type:"+oldeventtype+"\nold Event ID:"+oldeventID+"\nnew Event Type:"+neweventtype+"\nnew Event ID:"+neweventID+"\n"+msg;
			System.out.println(custID+": "+msg);
			return loginfo;
			//break;
		}
		return "not done anything";
	}
}
	



