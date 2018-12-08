package sjsu.cohort9.chappal.demo.service;

import sjsu.cohort9.chappal.demo.entiry.StepsEO;

public interface KafkaPublisherService {
	public void publishToKafka(StepsEO steps);
}
