/*
 * Copyright © 2013-2018 camunda services GmbH and various authors (info@camunda.com)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.camunda.bpm.engine.test.bpmn.event.timer;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import org.camunda.bpm.engine.impl.test.PluggableProcessEngineTestCase;
import org.camunda.bpm.engine.runtime.Job;
import org.camunda.bpm.engine.runtime.ProcessInstance;
import org.camunda.bpm.engine.test.Deployment;


/**
 * Test timer expression according to act-865
 * 
 * @author Saeid Mirzaei
 */

public class TimeExpressionTest extends PluggableProcessEngineTestCase {
	
	  
	  private Date testExpression(String timeExpression) {
		    // Set the clock fixed
		    HashMap<String, Object> variables1 = new HashMap<String, Object>();
		    variables1.put("dueDate", timeExpression);
		  
		    // After process start, there should be timer created    
		    ProcessInstance pi1 = runtimeService.startProcessInstanceByKey("intermediateTimerEventExample", variables1);
		    assertEquals(1, managementService.createJobQuery().processInstanceId(pi1.getId()).count());


		    List<Job> jobs = managementService.createJobQuery().executable().list();
		    assertEquals(1, jobs.size());
		    return jobs.get(0).getDuedate();
	  }
	  
	  @Deployment(resources = { "org/camunda/bpm/engine/test/bpmn/event/timer/IntermediateTimerEventTest.testExpression.bpmn20.xml"})	  
	  public void testTimeExpressionComplete() throws Exception {
		    Date dt = new Date();
		    
		    Date dueDate = testExpression(new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss").format(dt));
		    assertEquals(new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss").format(dt),new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss").format(dueDate));		    	  
	  }
	  
	  @Deployment(resources = { "org/camunda/bpm/engine/test/bpmn/event/timer/IntermediateTimerEventTest.testExpression.bpmn20.xml"})	  
	  public void testTimeExpressionWithoutSeconds() throws Exception {
		    Date dt = new Date();
		    
		    Date dueDate = testExpression(new SimpleDateFormat("yyyy-MM-dd'T'HH:mm").format(dt));
		    assertEquals(new SimpleDateFormat("yyyy-MM-dd'T'HH:mm").format(dt),new SimpleDateFormat("yyyy-MM-dd'T'HH:mm").format(dueDate));
	  }
	  
	  @Deployment(resources = { "org/camunda/bpm/engine/test/bpmn/event/timer/IntermediateTimerEventTest.testExpression.bpmn20.xml"})	 
	  public void testTimeExpressionWithoutMinutes() throws Exception {
		    Date dt = new Date();

		    Date dueDate = testExpression(new SimpleDateFormat("yyyy-MM-dd'T'HH").format(new Date()));
		    assertEquals(new SimpleDateFormat("yyyy-MM-dd'T'HH").format(dt),new SimpleDateFormat("yyyy-MM-dd'T'HH").format(dueDate));
	  }
	  
	  @Deployment(resources = { "org/camunda/bpm/engine/test/bpmn/event/timer/IntermediateTimerEventTest.testExpression.bpmn20.xml"})	  
	  public void testTimeExpressionWithoutTime() throws Exception {
		    Date dt = new Date();

		    Date dueDate = testExpression(new SimpleDateFormat("yyyy-MM-dd").format(new Date()));
		    assertEquals(new SimpleDateFormat("yyyy-MM-dd").format(dt),new SimpleDateFormat("yyyy-MM-dd").format(dueDate));
	  }
	
	  @Deployment(resources = { "org/camunda/bpm/engine/test/bpmn/event/timer/IntermediateTimerEventTest.testExpression.bpmn20.xml"})	  
	  public void testTimeExpressionWithoutDay() throws Exception {
		    Date dt = new Date();

		    Date dueDate = testExpression(new SimpleDateFormat("yyyy-MM").format(new Date()));
		    assertEquals(new SimpleDateFormat("yyyy-MM").format(dt),new SimpleDateFormat("yyyy-MM").format(dueDate));
	  }
	  
	  @Deployment(resources = { "org/camunda/bpm/engine/test/bpmn/event/timer/IntermediateTimerEventTest.testExpression.bpmn20.xml"})	  
	  public void testTimeExpressionWithoutMonth() throws Exception {
		    Date dt = new Date();
		    
		    Date dueDate = testExpression(new SimpleDateFormat("yyyy").format(new Date()));
		    assertEquals(new SimpleDateFormat("yyyy").format(dt),new SimpleDateFormat("yyyy").format(dueDate));
	  }
}
