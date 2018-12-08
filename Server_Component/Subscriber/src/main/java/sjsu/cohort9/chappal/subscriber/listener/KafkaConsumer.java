package sjsu.cohort9.chappal.subscriber.listener;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import sjsu.cohort9.chappal.subscriber.entity.StepsEO;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import sjsu.cohort9.chappal.subscriber.db.DBService;

import sjsu.cohort9.chappal.subscriber.entity.UserProfile;
import sjsu.cohort9.chappal.subscriber.ml.CaloriePredictor;

@Service
public class KafkaConsumer {
	@Autowired
	DBService dbservice;
	
	@Autowired
	CaloriePredictor cp;
	
    @KafkaListener(topics = "testing")
    public void consume(String message) {
        try {
			System.out.println("Consumed message: " + message);
			ObjectMapper objectMapper = new ObjectMapper();
			StepsEO steps = objectMapper.readValue(message, StepsEO.class);
			calAndUpdate(steps);
		} catch (JsonParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (JsonMappingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }

    @KafkaListener(topics = "testing",
            containerFactory = "userKafkaListenerFactory")
    public void consumeJson(StepsEO steps) {
        System.out.println("Consumed JSON Message: " + steps + " : " + steps.getStep());
        calAndUpdate(steps);
    }
    
    private void calAndUpdate(StepsEO steps) {
    	try {
	    	  UserProfile up = dbservice.getUser(steps.getUserId());
	          System.out.println("User details fetched " + up.toString());
	          double totalSteps = steps.getStep() + dbservice.getSteps(steps.getUserId());
	          
          	double minutes = Math.round(totalSteps/100);
	        System.out.println("Minutes " + minutes);
	        
	        double calories = 0;
	        calories = cp.predictCalorie(Double.parseDouble(up.getGender()),
	        		Double.parseDouble(up.getAge()), 
	        		Double.parseDouble(up.getHeight()), 
	        		Double.parseDouble(up.getWeight()), 
	        		minutes, 
	        		Double.parseDouble(up.getHeartRate()), 
	        		Double.parseDouble(up.getBodyTemp()));
	       System.out.println("Calories predicted "+ calories);
	       
	       dbservice.updateSteps(steps.getUserId(), totalSteps, minutes, calories);
    	}catch(Exception ex) {
    		ex.printStackTrace();
    	}
	       
    }
}
