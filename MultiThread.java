import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.math.BigInteger;

public class MultiThread extends Thread
{
	BigInteger startPrime, n, endNumber, e, response=null;
	String host;
	public MultiThread(BigInteger n, BigInteger startPrime, BigInteger endNumber, BigInteger e, String host)
	{
		this.startPrime=startPrime;
		this.n=n;
		this.endNumber=endNumber;
		this.e=e;
		this.host=host;
		start();
	}
	
	public void run()
	{
		try {
			Registry registry = LocateRegistry.getRegistry(host);
			CommonMethod stub = (CommonMethod) registry.lookup("CommonMethod");
			System.out.println("Calculating from " + startPrime + " to " + endNumber);
			Thread.sleep(100);
			BigInteger response = stub.findPHI(n, startPrime, endNumber, e);
			System.out.println(startPrime + " response: " + response);
			this.response=response;
		} catch (Exception e) {
			System.err.println("Client exception: " + e.toString());
			e.printStackTrace();
		}
	}
}
