package chess.network.impl.server;

public class NetworkAddress
{
	public final String ip;
	public final int port;
	
	public NetworkAddress(String ip, int port)
	{
		this.ip = ip;
		this.port = port;
	}
	
	@Override
	public boolean equals(Object obj)
	{
		return obj instanceof NetworkAddress && ((NetworkAddress)obj).ip.equals(ip) && ((NetworkAddress)obj).port == port;
	}
	
	public static NetworkAddress create(String ip, int port)
	{
		return new NetworkAddress(ip, port);
	}
}
