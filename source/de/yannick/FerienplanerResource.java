package de.yannick;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;


@Path("/fp")
public class FerienplanerResource {
	
	private static final File file = new File("abwesenheiten");
	private static final List<Abwesenheit> abwesenheitsliste = new ArrayList<>();
	static {
		try{
			if (file.exists()){
				List<String> lines = FileUtils.readLines(file);
				for (String line : lines){
					String[] parts = StringUtils.split(line, ";");
					Abwesenheit aw = new Abwesenheit();
					aw.year = Integer.parseInt(parts[0]);
					aw.month = Integer.parseInt(parts[1]);
					aw.day = Integer.parseInt(parts[2]);
					aw.abwesenheit = Integer.parseInt(parts[3]);
					aw.personenID = Integer.parseInt(parts[4]);
					abwesenheitsliste.add(aw);
				}
			}
		} catch (Exception e){
			throw new RuntimeException(e);
		}
	}
	
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
		try {
			FileUtils.write(file, year+";"+month+";"+day+";"+abwesenheit+";"+personenID+"\n", true);
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
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
