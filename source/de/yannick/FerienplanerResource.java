package de.yannick;

import java.util.ArrayList;
import java.util.List;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

@Path("/fp")
public class FerienplanerResource {
	private static final List<Abwesenheit> abwesenheitsliste = new ArrayList<>();
	
	@GET
	@Path("{year}/{month}/{day}/{abwesenheit}/{personenID}")
	@Consumes({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	@Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	// [{"name":"Dirk Schaube","month":"0","absenseType":"7","day":"3"},{"name":"Dirk Schaube","month":"0","absenseType":"7","day":"4"}]
	public String speichereAbwesenheit(@PathParam("year") int year,@PathParam("month") int month,@PathParam("day") int day,@PathParam("abwesenheit") int abwesenheit,@PathParam("personenID") int personenID) {
		Abwesenheit aw = new Abwesenheit();
		aw.year = year;
		aw.month = month;
		aw.day = day;
		aw.abwesenheit = abwesenheit;
		aw.personenID = personenID;
		abwesenheitsliste.add(aw);
		return "";
	}
	
	@GET
	@Path("{month}")
	@Consumes({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	@Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	public String getAbwesenheitenProMonat(@PathParam("month") int month){
		//[{"year":2013,"month":2,"day":22,"abwesenheit":2,"personenID":1},{}]
		return "[{\"year\":2013,\"month\":2,\"day\":22,\"abwesenheit\":2,\"personenID\":1}]";
	}
}
