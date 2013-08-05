package de.yannick;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;


@Path("/fp")
public class FerienplanerResource {

	private static final File file = new File("abwesenheiten");
	private static final Map<Abwesenheit,Abwesenheit> abwesenheitsMap = new HashMap<>();
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
					abwesenheitsMap.put(aw,aw);
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
		abwesenheitsMap.put(aw,aw);
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
		String result = "[";
		for (Abwesenheit aw:abwesenheitsMap.values()){
			//Abwesenheit aw = abwesenheitsliste.get(i);
			if (aw.month==month){
				if (result.length()>2){
					result = result + ",";
				}
				result = result + "{\"year\":"+aw.year+",\"month\":"+aw.month+",\"day\":"+aw.day+",\"abwesenheit\":"+aw.abwesenheit+",\"personenID\":"+aw.personenID+"}";
				//return "[{\"year\":aw.year,\"month\":aw.month,\"day\":aw.day,\"abwesenheit\":aw.abwesenheit,\"personenID\":aw.personenID}]";
			}
		}
		result = result + "]";
		return result;
		//return "[{\"year\":aw.year,\"month\":aw.month,\"day\":aw.day,\"abwesenheit\":aw.abwesenheit,\"personenID\":aw.personenID}]";
		//return "[{\"year\":2013,\"month\":2,\"day\":22,\"abwesenheit\":2,\"personenID\":1}]";
	}
	@GET
	@Path("urlaubstage")
	@Consumes({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	@Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	public String getUrlaubstage(){
		JSONArray alleUrlaubsTage = new JSONArray();

		Map<Integer, Integer> urlaubstage = new HashMap<>();
		for (Abwesenheit aw:abwesenheitsMap.values()){
			if (7 == aw.abwesenheit)  {
				Integer urlaubstaganzahl = urlaubstage.get(aw.personenID);
				urlaubstaganzahl = (urlaubstaganzahl == null) ? 0 : urlaubstaganzahl;
				urlaubstage.put(aw.personenID, urlaubstaganzahl+1);
			}
		}

		for (Integer personenID : urlaubstage.keySet()) {
			JSONObject personUrlaubsTage = new JSONObject();
			personUrlaubsTage.put("id",personenID);
			personUrlaubsTage.put("anzahl", urlaubstage.get(personenID));
			alleUrlaubsTage.add(personUrlaubsTage);
		}




		return alleUrlaubsTage.toString();
	}

}
