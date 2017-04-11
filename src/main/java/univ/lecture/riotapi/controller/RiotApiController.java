package univ.lecture.riotapi.controller;

import lombok.extern.log4j.Log4j;
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
import univ.lecture.riotapi.model.EquationData;

import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.Date;
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

    @RequestMapping(value = "/calc",  method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @ResponseBody
    public EquationData calEquation(@RequestBody String equation) throws UnsupportedEncodingException {
        final String url = Endpoint;
        Calculator cal = new Calculator();

        String StringTime   = new SimpleDateFormat("HHmmss").format(new Date());
        int IntegerTime = Integer.parseInt(StringTime);
        String request = "{\""+equation+"\":{\"teamId\":4,\"now\":"+IntegerTime+",\"result\":"+cal.calculate(equation)+"}}";
        String response = "{\"SendData\":"+restTemplate.postForObject(url, request, String.class)+"}";
        Map<String, Object> parsedMap = new JacksonJsonParser().parseMap(request);

        Map<String, Object> equationDetail = (Map<String, Object>) parsedMap.values().toArray()[0];
        int teamId = (Integer)equationDetail.get("teamId");
        int now = (Integer)equationDetail.get("now");
        double result = (Double)equationDetail.get("result");
        parsedMap = new JacksonJsonParser().parseMap(response);
        equationDetail = (Map<String, Object>) parsedMap.values().toArray()[0];
        String msg = (String)equationDetail.get("msg");
        EquationData equationdata = new EquationData(teamId, now, result, msg);
        return equationdata;
    }
}
