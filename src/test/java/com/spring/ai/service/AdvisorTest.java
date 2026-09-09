package com.spring.ai.service;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class AdvisorTest {
	@Autowired
    private AdvisorService chatai;

    @Test
    public void askAI() {
    	var joke= chatai.askAiwithadvisor("i am not able to login discord how can i","rohit123");
    	System.out.println(joke);

        
    }
   


}
