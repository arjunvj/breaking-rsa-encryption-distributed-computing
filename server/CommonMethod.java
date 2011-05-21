import java.rmi.Remote;
import java.rmi.RemoteException;
import java.math.BigInteger;

public interface CommonMethod extends Remote
{
	public BigInteger findPHI(BigInteger n, BigInteger startPrime, BigInteger endNumber, BigInteger e) throws RemoteException;
}