package com.spring.ai.tool;

import org.springframework.ai.tool.annotation.Tool;
import org.springframework.ai.tool.annotation.ToolParam;
import org.springframework.stereotype.Service;

@Service
public class TravellingTools {
	
	@Tool(description = "Get the current weather for a given city.")
	public String getWeather(@ToolParam(description ="city name for which to get the weather information")  String city) {
		switch(city.toLowerCase()) {
			case "new york":
				return "The weather in New York is sunny with a high of 75°F.";
			case "los angeles":
				return "The weather in Los Angeles is cloudy with a high of 68°F.";
			case "chicago":
				return "The weather in Chicago is rainy with a high of 60°F.";
			default:
				return "Weather information for " + city + " is not available.";
		}
		
	}

}
