package ch.usi.ict.dev.AclOverSoapHttpMP.components;

import java.net.MalformedURLException;
import java.net.URL;

public class WShttpAddress {
	
	private static final String WSPath = "/ACLSoapOverHttpMP/services/AclOverSoapHttpMPPort";
	
	protected final URL url;

	public WShttpAddress(URL addr) throws MalformedURLException {
		url = addr;
	}  

	public WShttpAddress(String addr) throws MalformedURLException {
		url = new URL(addr);
	}

	public WShttpAddress(String addr, int port, boolean https) throws MalformedURLException {
		if(https){
			url = new URL("https",addr,port, WSPath);
		}else{
			url = new URL("http",addr,port, WSPath);
		}
	}
	
	public String getStringAddress() {
		
		return url.toString();
	}

}
