package org.netcomputing.webservices.server;

import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.PUT;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Request;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;
import javax.xml.bind.JAXBElement;

import org.netcomputing.webservices.datamodel.Event;
import org.netcomputing.webservices.datamodel.EventDAO;


public class EventResource {
	@Context
	UriInfo uriInfo;
	@Context
	Request request;
	String id;
	public EventResource(UriInfo uriInfo, Request request, String id) {
		this.uriInfo = uriInfo;
		this.request = request;
		this.id = id;
	}
	
	//Application integration 		
	@GET
	@Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
	public Event getLocation() {
		Event p = EventDAO.instance.getModel().get(id);
		if(p==null)
			throw new RuntimeException("Get: location with " + id +  " not found");
		return p;
	}
	
	// For the browser
	@GET
	@Produces(MediaType.TEXT_XML)
	public Event getLocationHTML() {
		Event p = EventDAO.instance.getModel().get(id);
		if(p==null)
			throw new RuntimeException("Get: Location with " + id +  " not found");
		return p;
	}
	
	@PUT
	@Consumes(MediaType.APPLICATION_XML)
	public Response putLocation(JAXBElement<Event> todo) {
		Event c = todo.getValue();
		return putAndGetResponse(c);
	}
	
	@DELETE
	public void deleteLocation() {
		Event c = EventDAO.instance.getModel().remove(id);
		if(c==null)
			throw new RuntimeException("Delete: Location with " + id +  " not found");
	}
	
	private Response putAndGetResponse(Event l) {
		Response res;
		if(EventDAO.instance.getModel().containsKey(l.getId())) {
			res = Response.noContent().build();
		} else {
			res = Response.created(uriInfo.getAbsolutePath()).build();
		}
		EventDAO.instance.getModel().put(l.getId(), l);
		return res;
	}
	
	

} 