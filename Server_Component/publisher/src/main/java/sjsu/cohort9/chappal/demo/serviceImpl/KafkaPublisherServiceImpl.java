package sjsu.cohort9.chappal.demo.serviceImpl;

import sjsu.cohort9.chappal.demo.entiry.StepsEO;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import sjsu.cohort9.chappal.demo.service.KafkaPublisherService;

@Service
public class KafkaPublisherServiceImpl implements KafkaPublisherService{

	@Autowired
    private KafkaTemplate<String, StepsEO> kafkaTemplate;
	
	@Override
	public void publishToKafka(StepsEO steps) {
		kafkaTemplate.send("testing", steps);
		System.out.println("Published !!!");
		
	}

}
