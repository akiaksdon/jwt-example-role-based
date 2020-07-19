package com.devglan.controller;

import java.io.IOException;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.devglan.model.Events;
import com.devglan.model.Market;
import com.devglan.model.Markets;
import com.fasterxml.jackson.databind.ObjectMapper;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.ResponseBody;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/events")
public class EventController {
	
	
	private static final Logger log = LoggerFactory.getLogger(EventController.class);

	
	@RequestMapping(value = "", method = RequestMethod.GET, produces = "application/json")
	public ResponseEntity<Events> getListOfEvents() {

		// abstract this logic to service layer later on 
		
		 OkHttpClient client = new OkHttpClient();
		 ObjectMapper objectMapper = new ObjectMapper();
		Request request = new Request.Builder()
			.url("https://sport-data.p.rapidapi.com/api/listBetEventTypes")
			.get()
			.addHeader("x-rapidapi-host", "sport-data.p.rapidapi.com")
			.addHeader("x-rapidapi-key", "927875fad7mshc0dd3c20a97f03ap1854f7jsnc0b1a557da8e")
			.build();

		//implement global error controller advice
		try {
			ResponseBody responseBody = client.newCall(request).execute().body();
			Events entity = objectMapper.readValue(responseBody.string(), Events.class);

			return new ResponseEntity<Events>(entity,HttpStatus.OK);
		} catch (IOException e) {
			log.info("failed fetching events" + e.getMessage());
		}
		return null;
		
	}
	
	@RequestMapping(value = "/{eventId}/inplay/{isInplay}", method = RequestMethod.GET, produces = "application/json")
	public ResponseEntity<Markets> getBetMarketsByEvent(@PathVariable("eventId") int eventId,@PathVariable("isInplay") boolean isInplay) {
		
		OkHttpClient client = new OkHttpClient();
		 ObjectMapper objectMapper = new ObjectMapper();
		 Request request = new Request.Builder()
					.url("https://sport-data.p.rapidapi.com/api/listBetMarkets/"+eventId+"/"+isInplay)
					.get()
					.addHeader("x-rapidapi-host", "sport-data.p.rapidapi.com")
					.addHeader("x-rapidapi-key", "927875fad7mshc0dd3c20a97f03ap1854f7jsnc0b1a557da8e")
					.build();

		//implement global error controller advice
		try {
			ResponseBody responseBody = client.newCall(request).execute().body();
			Markets entity = objectMapper.readValue(responseBody.string(), Markets.class);

			List<Market> result = entity.getResult();

			return new ResponseEntity<Markets>(entity,HttpStatus.OK);
		} catch (IOException e) {
			log.info("failed fetching events" + e.getMessage());
		}
		return null;
	}

}
