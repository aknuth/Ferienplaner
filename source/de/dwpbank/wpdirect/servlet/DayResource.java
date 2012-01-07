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
	
	private static List<String> absenceHolder = new ArrayList<String>();
	
	@POST @Path("day")
	@Consumes({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
	@Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	public void a(String absence) {
		absenceHolder.add(absence);
	}
	@GET @Path("days/{month}")
	@Consumes({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
	@Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	public String e(@PathParam("month") String month) {
		String result = "[";
		for (String absence : absenceHolder){
			result = result + absence + ",";
		}
		result=absenceHolder.size()==0?result+"]":result.subSequence(0, result.length()-1)+"]";
		return result;
		//return "[{\"name\":\"Andreas Knuth\",\"day\":3,\"month\":0,\"absenseType\":4}]";
	}
//	@GET @Path("persons")
//	@Consumes({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
//	@Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
//	public List<Person> f() {
//		//return "{name:'Andreas Knuth',name:'Stefan Grager',name:'Dirk Schaube',name:'Alexander Krumeich'}";
//		List<Person> result = new ArrayList<Person>();
//		result.add(new Person("Andreas Knuth"));
//		result.add(new Person("Stefan Grager"));
//		result.add(new Person("Dirk Schaube"));
//		result.add(new Person("Alexander Krumeich"));
//		return result;
//	}
	@GET @Path("persons")
	@Consumes({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
	@Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	public String f() {
		//return "{\'person\':[{\'name\':\'Andreas Knuth\'},{\'name\':\'Stefan Grager\'},{\'name\':\'Dirk Schaube\'},{\'name\':\'Alexander Krumeich\'}]}";
		return "[{\"name\":\"Andreas Knuth\"},{\"name\":\"Stefan Grager\"},{\"name\":\"Dirk Schaube\"}]";
	}
//	@POST @Path("{/months/month/days/day}")
//	@Consumes({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
//	@Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
//	public void e(String content) {
//		System.out.println(content);
//	}

	//@GET @Path("{month}")
//	@GET @Path("/days/{month}")
//	@Consumes({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
//	@Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
//	public List<Day> find(@PathParam("month") String month) {
//		List<Day> result = new ArrayList<Day>();
//		Day day = new Day();
//		day.setIndex(0);
//		day.setAbsenseType(2);
//		day.setMonth(month);
//		result.add(day);
//		return result;
//	}
//	@POST @Path("{months}")
//	@Consumes({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
//	@Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
//	public void b(String content) {
//		System.out.println(content);
//	}
//	@POST @Path("{month}")
//	@Consumes({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
//	@Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
//	public void c(String content) {
//		System.out.println(content);
//	}
//	@POST @Path("{day}")
//	@Consumes({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
//	@Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
//	public void d(String content) {
//		System.out.println(System.currentTimeMillis()+" : "+content);
//	}
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

	/**@POST
	@Consumes({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
	@Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	public Day create(Day day) {
		return day;
	}*/
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
