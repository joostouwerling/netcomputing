package org.netcomputing.webservices.client;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;

public class Test {
	public static void main(String[] args) {
		Client client = ClientBuilder.newClient(); 
		
		WebTarget target = client.target("http://localhost:8080/NC2016_4_WS_rest/rest").path("hello"); 
		System.out.println(target.request(MediaType.TEXT_PLAIN).get(String.class));
		System.out.println(target.request(MediaType.TEXT_XML).get(String.class));
		// Fluent interfaces
//		System.out.println(service.path("rest").path("hello").accept(MediaType.TEXT_PLAIN).get(ClientResponse.class).toString());
		// Get plain text
//		System.out.println(service.path("rest").path("hello").accept(MediaType.TEXT_PLAIN).get(String.class));
		// Get XML
//		System.out.println(service.path("rest").path("hello").accept(MediaType.TEXT_XML).get(String.class));
		// The HTML
//		System.out.println(service.path("rest").path("hello").accept(MediaType.TEXT_HTML).get(String.class));

	}

} 