package de.yannick;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.json.simple.JSONValue;

@Path("/fp")
public class FerienplanerResource {
	@GET
	@Path("{year}/{month}/{day}")
	@Consumes({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	@Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	// [{"name":"Dirk Schaube","month":"0","absenseType":"7","day":"3"},{"name":"Dirk Schaube","month":"0","absenseType":"7","day":"4"}]
	public String speichereAbwesenheit(@PathParam("year") int year,@PathParam("month") int month,@PathParam("day") int day,@PathParam("abwesenheit") int abwesenheit,@PathParam("personenID") int personenID) {
		//System.err.println("Size --> " + absenceHolder.values().size() + "\n");
		List<Map<String, String>> list = new LinkedList<Map<String, String>>();
//		for (Day day : absenceHolder.values()) {
//			if (day.getMonth().equals(month)) {
//				Map<String, String> map = new HashMap<String, String>();
//				map.put("name", day.getName());
//				map.put("day", String.valueOf(day.getDay()));
//				map.put("month", day.getMonth());
//				map.put("absenseType", String.valueOf(day.getAbsenseType()));
//				list.add(map);
//			}
//		}
		System.out.println(JSONValue.toJSONString(list));
		return JSONValue.toJSONString(list);
	}

}
