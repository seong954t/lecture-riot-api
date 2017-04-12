package univ.lecture.riotapi.controller;

import lombok.extern.log4j.Log4j;
import univ.lecture.riotapi.model.EquationData;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.json.JacksonJsonParser;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;
import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by tchi on 2017. 4. 1..
 */
@RestController
@RequestMapping("/api/v1")
@Log4j
public class RiotApiController {
	@Autowired
	private RestTemplate restTemplate;

	@Value("${endpoint}")
	private String Endpoint;

	@RequestMapping(value = "/calc", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	@ResponseBody
	public EquationData calEquation(@RequestBody String equation) throws UnsupportedEncodingException {
		final String url = Endpoint;
		final int teamId = 4;

		Calculator cal = new Calculator();
		double result = cal.calculate(equation);
		long now = System.currentTimeMillis();
		Map<String, Object> jsonObject = new HashMap<String, Object>();
		jsonObject.put("teamId", teamId);
		jsonObject.put("now", now);
		jsonObject.put("result", result);

		Map<String, Object> msg = restTemplate.postForObject(url, jsonObject, HashMap.class);

		EquationData equationdata = new EquationData(teamId, now, result, (String) msg.get("msg"));
		return equationdata;
	}
}
