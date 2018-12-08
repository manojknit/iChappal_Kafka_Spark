package sjsu.cohort9.chappal.demo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import sjsu.cohort9.chappal.demo.entiry.StepsEO;
import sjsu.cohort9.chappal.demo.service.KafkaPublisherService;

@RestController
@RequestMapping("/api/publish")
@CrossOrigin(origins = {"*"})
public class KafkaPublisherController {
	@Autowired
	KafkaPublisherService service;
	
	@RequestMapping(method=RequestMethod.GET)
	public String getOrders() {
		return "hello NEW world ";
	}
	
	@RequestMapping(method=RequestMethod.POST)
	public HttpStatus publishToKafka(@RequestBody StepsEO steps) {
		try {
			System.out.println("Received !!! " + steps.toString());
			service.publishToKafka(steps);
		}catch(Exception ex ) {
			ex.printStackTrace();
			return HttpStatus.INTERNAL_SERVER_ERROR;
		}
		 return HttpStatus.OK;
	}
}
