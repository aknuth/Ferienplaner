package de.dwpbank.wpdirect.servlet;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.nio.ByteBuffer;
import java.nio.channels.AsynchronousFileChannel;
import java.nio.channels.CompletionHandler;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;
import java.util.concurrent.Future;

import javax.net.ssl.SSLSocketFactory;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.apache.commons.lang3.StringUtils;
import org.json.simple.JSONValue;

import com.unboundid.ldap.sdk.Filter;
import com.unboundid.ldap.sdk.LDAPConnection;
import com.unboundid.ldap.sdk.SearchRequest;
import com.unboundid.ldap.sdk.SearchResult;
import com.unboundid.ldap.sdk.SearchResultEntry;
import com.unboundid.ldap.sdk.SearchScope;
import com.unboundid.util.ssl.SSLUtil;
import com.unboundid.util.ssl.TrustAllTrustManager;

@Path("/fp")
public class DayResource {

	private static final String CN = "cn";
	private static final String UID = "uid";
	private static Map<String, Day> absenceHolder = new HashMap<String, Day>();
	private static Map<String, List<String>> orgaUnits = new HashMap<String, List<String>>();
	private static java.nio.file.Path days = Paths.get("days");
	private static AsynchronousFileChannel ch = null;
	static {
		try {
			ch = AsynchronousFileChannel.open(Paths.get("days"), StandardOpenOption.WRITE, StandardOpenOption.CREATE);
			String line;
			BufferedReader br = new BufferedReader(new InputStreamReader(days.newInputStream()));
			while ((line = br.readLine()) != null) {
				StringTokenizer tokenizer = new StringTokenizer(line,"|");
				String[] parts = new String[4];
				int i=0;
				while (tokenizer.hasMoreTokens()){
					parts[i]=tokenizer.nextToken();
					i++;
				}
				Day day = new Day(Integer.parseInt(parts[2]), Integer.parseInt(parts[3]), parts[1], parts[0]);
				String key = day.getName() + "_" + day.getDay() + "_" + day.getMonth();
				absenceHolder.put(key, day);
			}
			br.close();
			
			List<String> personNames = new ArrayList<>();
			SSLSocketFactory socketFactory = new SSLUtil(new TrustAllTrustManager()).createSSLSocketFactory();
			LDAPConnection connection = new LDAPConnection(socketFactory, "97.0.242.247", 6360, "cn=wpdirect-user,ou=users,ou=technical,dc=dwp-bank,dc=net", "wpdirectpassword");
			Filter searchFilter = Filter.create("(&(objectclass=dwpPerson)(dwpOU=ITTAS)(employeeType=internal))");
			SearchRequest searchRequest = new SearchRequest("dc=dwp-bank,dc=net", SearchScope.SUB, searchFilter, UID, CN);
			SearchResult searchResult = connection.search(searchRequest);
			List<SearchResultEntry> persons = searchResult.getSearchEntries();
			for (SearchResultEntry thePerson: persons){
				String cn = thePerson.getAttributeValue(CN);
				String name = StringUtils.substringAfter(cn, ",").trim()+" "+StringUtils.substringBefore(cn, ",").trim();
				personNames.add(name);
			}
			
			orgaUnits.put("ITTAS", personNames);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	private String getKey(Day day){
		return day.getName() + "_" + day.getDay() + "_" + day.getMonth();
	}
	
	@POST
	@Path("day")
	@Consumes({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	@Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	public void a(Day day) {
		System.out.println("add --> " + day + "\n");
		
		if (day.getAbsenseType()==0 && absenceHolder.containsKey(getKey(day))){
			absenceHolder.remove(getKey(day));
		} else if (day.getAbsenseType()==1){
			return;
		} else {
			absenceHolder.put(getKey(day), day);
		}

		StringBuilder serial = new StringBuilder();
		for (Day d : absenceHolder.values()) {
			serial.append(d.toString());
			serial.append("\n");
		}
		Future<Integer> result = ch.write(ByteBuffer.wrap(serial.toString().getBytes()), 0, null, new CompletionHandler<Integer, Void>() {

			@Override
			public void completed(Integer result, Void attachment) {
				System.out.println("ok");
			}

			@Override
			public void failed(Throwable exc, Void attachment) {
				throw new RuntimeException(exc);
			}

			@Override
			public void cancelled(Void attachment) {
				System.out.println("cancelled");
			}
		});

	}

	@GET
	@Path("days/{month}")
	@Consumes({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	@Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	// [{"name":"Dirk Schaube","month":"0","absenseType":"7","day":"3"},{"name":"Dirk Schaube","month":"0","absenseType":"7","day":"4"}]
	public String e(@PathParam("month") String month) {
		System.err.println("Size --> " + absenceHolder.values().size() + "\n");
		List<Map<String, String>> list = new LinkedList<Map<String, String>>();
		for (Day day : absenceHolder.values()) {
			if (day.getMonth().equals(month)) {
				Map<String, String> map = new HashMap<String, String>();
				map.put("name", day.getName());
				map.put("day", String.valueOf(day.getDay()));
				map.put("month", day.getMonth());
				map.put("absenseType", String.valueOf(day.getAbsenseType()));
				list.add(map);
			}
		}
		System.out.println(JSONValue.toJSONString(list));
		return JSONValue.toJSONString(list);
	}

	@GET
	@Path("persons")
	@Consumes({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	@Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	public String f() {
		List<Map<String, String>> list = new LinkedList<Map<String, String>>();
		for (String name : orgaUnits.get("ITTAS")){
			Map<String, String> map = new HashMap<String, String>();
			int holidays = 0;
			for (Day day : absenceHolder.values()) {
				if (day.getName().equals(name) && day.getAbsenseType()==2){
					holidays++;
				}
			}
			map.put("name", name);
			map.put("holidays", new Integer(holidays).toString());
			list.add(map);
		}	
		System.out.println(JSONValue.toJSONString(list));
		return JSONValue.toJSONString(list);
	}
}
