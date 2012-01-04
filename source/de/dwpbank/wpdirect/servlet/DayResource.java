package de.dwpbank.wpdirect.servlet;

import java.util.ArrayList;
import java.util.List;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

@Path("/fp")
public class DayResource {

/**	@GET
	@Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	public List<Wine> findAll() {
		return dao.findAll();
	}

	@GET @Path("search/{query}")
	@Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	public List<Wine> findByName(@PathParam("query") String query) {
		return dao.findByName(query);
	}

	@GET @Path("{id}")
	@Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	public Wine findById(@PathParam("id") String id) {
		return dao.findById(Integer.parseInt(id));
	}*/

	@POST
	@Consumes({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
	@Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	public Day create(Day day) {
		return day;
	}

	//@GET @Path("{month}")
	@GET @Path("/days/{month}")
	@Consumes({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
	@Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	public List<Day> find(@PathParam("month") String month) {
		List<Day> result = new ArrayList<Day>();
		Day day = new Day();
		day.setIndex(0);
		day.setAbsenseType(2);
		day.setMonth(month);
		result.add(day);
		return result;
	}
	/**@PUT @Path("{id}")
	@Consumes({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
	@Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	public Wine update(Wine wine) {
		return dao.update(wine);
	}

	@DELETE @Path("{id}")
	@Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	public void remove(@PathParam("id") int id) {
		dao.remove(id);
	}*/
}
