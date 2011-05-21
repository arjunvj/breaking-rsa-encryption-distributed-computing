import java.math.BigInteger;

public class Main
{
	private Main(){}
	
	BigInteger n=new BigInteger("721498723487");
	BigInteger e=new BigInteger("71");
	BigInteger encryptedNumber=new BigInteger("2790");
	
	public static void main(String[] args) 
	{		
		new Main().init(args);
	}
	
	public void init(String args[])
	{
		BigInteger d,decryptedNumber;
		BigInteger phi=findPHI(n, args);
		if(phi==null)
		{
			System.out.println("Something is Wrong!");
			return;
		}
		
		d=findD(phi, e);
		decryptedNumber=decrypt(encryptedNumber, d, n);
		
		System.out.println("Yes, It can Decrypted");
		System.out.println("Private Key - {" + d + ", " + n + "}");
		System.out.println("Decrypted Result = " + decryptedNumber);
	}
	
	/**
	 * Calculates decryptedNumber=(encryptedNumber^d)%n using fast exponentiation 
	 * @param encryptedNumber
	 * @param d
	 * @param n
	 * @return decryptedNumber
	 */
	private BigInteger decrypt(BigInteger x, BigInteger y, BigInteger n) //x=encryptedNumber, y=d
	{
		  BigInteger z=BigInteger.ONE;
		  x=x.mod(n);
	      while (y.compareTo(BigInteger.ZERO) > 0) 
	      {
	    	 while(!y.testBit(0))
	         {
	            x = x.multiply(x);
	            y=y.shiftRight(1);
	            x=x.mod(n);
	         }
	         z = z.multiply(x);
	         y=y.subtract(BigInteger.ONE);
	         z=z.mod(n);
	      }
	      return z;
	}
	
	/**
	 * For a given e and phi, find a d such that gcd of d*e and phi is 1. Uses extended euclidean algorithm
	 * @param e
	 * @param phi
	 * @return d
	 */
	//TODO optimize the extended euclidean algorithm Pref=3
	private BigInteger findD(BigInteger a, BigInteger b) //a=phi, b=e
	{
		BigInteger phi=a;
		BigInteger x = BigInteger.ZERO;
		BigInteger lastx = BigInteger.ONE;
		BigInteger y = BigInteger.ONE;
		BigInteger lasty = BigInteger.ZERO;
		while(!b.equals(BigInteger.ZERO))
		{
			BigInteger quotient[] = a.divideAndRemainder(b);
			a=b;
			b=quotient[1];
			
			BigInteger temp=x;
			x = lastx.subtract(quotient[0].multiply(x));
			lastx=temp;
			
			temp=y;
			y= lasty.subtract(quotient[0].multiply(y));
			lasty=temp;
		}
		if(lasty.compareTo(BigInteger.ZERO)<0)
			lasty=lasty.add(phi);
		return lasty;
	}
	
	/**
	 * 1) Finds a prime number 'p' less than sqrt(n). 
	 * 2) Divides n by p to obtain q and check if q is prime
	 * 3) check if gcd of e and (p-1)*(q-1) is one
	 * 4) If 2 or 3 fails go to step to find next prime
	 * 5) else (p-1)*(q-1)
	 * 6) If no such p and q exist, return null
	 * @param n
	 * @return (p-1)*(q-1) if there exists one else return null
	 */
	//TODO Increase probable prime argument to 999, and check if my prime generating function gives better optimized result Pref:2
	private BigInteger findPHI(BigInteger n, String args[])
	{
		BigInteger sqrt=floorSqrt(n);
		int length=args.length;
		//System.out.println("Enter " + (length+1) + " numbers till " + sqrt);
		//Scanner cin=new Scanner(System.in);
		
		MultiThread mt[]=new MultiThread[length+1];
		BigInteger temp2=sqrt.divide(new BigInteger((length+1) + ""));
		mt[0]=new MultiThread(n, new BigInteger("2"), temp2, e, null);
		for(int i=1; i<=length; i++)
		{
			mt[i]=new MultiThread(n, temp2.nextProbablePrime(), temp2.multiply(new BigInteger((i+1) + "")), e, args[i-1]);
		}

		try {
			for(int i=0; i<=length; i++)
			{
				mt[i].join();
				if(mt[i].response!=null)
					return mt[i].response;
			}
		} catch (InterruptedException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
		return null;
	}
	
	/**
	 * @param x
	 * @return Integer square root of BigInteger x
	 */
	private BigInteger floorSqrt(BigInteger x) 
	{  	      
	    int bit = Math.max(0, (x.bitLength() - 63) & 0xfffffffe); // last even numbered bit in first 64 bits  
	    BigInteger result = BigInteger.valueOf(floorSqrt(x.shiftRight(bit).longValue()) & 0xffffffffL);  
	    bit >>>= 1;  
	    result = result.shiftLeft(bit);  
	    while (bit != 0) {  
	      bit--;  
	      final BigInteger resultHigh = result.setBit(bit);  
	      if (resultHigh.multiply(resultHigh).compareTo(x) <= 0) result = resultHigh;  
	    }	      
	    return result;  
	}
	
	private int floorSqrt(final int x) 
	{  
	    return (int) StrictMath.sqrt(x & 0xffffffffL);  
	}  
	    
	  // floorSqrt :: unsigned long -> unsigned int  
	  // Gives the exact floor of the square root of x, treated as unsigned.  
	private int floorSqrt(final long x) 
	{  
	    if ((x & 0xfff0000000000000L) == 0L) return (int) StrictMath.sqrt(x);  
	    final long result = (long) StrictMath.sqrt(2.0d*(x >>> 1));  
	    return result*result - x > 0L ? (int) result - 1 : (int) result;  
	}
}