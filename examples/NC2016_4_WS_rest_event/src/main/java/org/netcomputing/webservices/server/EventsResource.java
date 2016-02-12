package org.netcomputing.webservices.server;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.Consumes;
import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Request;
import javax.ws.rs.core.UriInfo;

import org.netcomputing.webservices.datamodel.Event;
import org.netcomputing.webservices.datamodel.EventDAO;


//Will map the resource to the URL positions
@Path("/events")
public class EventsResource {
	// Allows to insert contextual objects into the class,
	// e.g. ServletContext, Request, Response, UriInfo 
	
	@Context UriInfo uriInfo;
	@Context
	Request request;

	// Return the list of events to the user in the browser 
	@GET
	@Produces(MediaType.TEXT_XML)
	public List<Event> getEventsBrowser() {
		List<Event> events = new ArrayList<Event>();
		events.addAll(EventDAO.instance.getModel().values());
		return events;
	}

	// Return the list of events for applications 
	@GET
	@Produces({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	public List<Event> getEvents() {
		List<Event> events = new ArrayList<Event>();
		events.addAll(EventDAO.instance.getModel().values());
		return events;
	}

	// retuns the number of Events
	// Use
	// http://localhost:8080/.../rest/events/count
	// to get the total number of records @GET @Path("count")
	@Produces(MediaType.TEXT_PLAIN)
	public String getCount() {
		int count = EventDAO.instance.getModel().size();
		return String.valueOf(count);
	}
	
	@POST
	@Produces(MediaType.TEXT_HTML)
	@Consumes(MediaType.APPLICATION_FORM_URLENCODED)
	public void newLocation(@FormParam("id") String id,
			@FormParam("message") String message,
			@FormParam("value") Double value,
			@Context HttpServletResponse servletResponse) throws IOException {
		Event event= new Event();
		event.setId(id);
		event.setMessage(message);
		event.setValue(value);
		EventDAO.instance.getModel().put(id, event);
		servletResponse.sendRedirect("../addevent.html");
	}

	// Defines that the next path parameter after Events is
	// treated as a parameter and passed to the EventResources
	// Allows to type
	// http://localhost:8080/.../rest/locations/1
	// 1 will be treaded as parameter and passed to EventResource
	@Path("{event}")
	public EventResource getLocation(@PathParam("event") String id) {
		return new EventResource(uriInfo, request, id);
	}
}
