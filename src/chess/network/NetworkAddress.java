package chess.network;

import java.net.IDN;
import java.util.Objects;

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

	@Override
	public String toString()
	{
		return ip + ":" + port;
	}

	@Override
	public int hashCode() {
		return Objects.hash(ip, port);
	}

	public static NetworkAddress create(String ip, int port)
	{
		return new NetworkAddress(ip, port);
	}
}
