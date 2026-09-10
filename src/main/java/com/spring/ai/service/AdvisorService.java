package com.spring.ai.service;

import java.util.List;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.SafeGuardAdvisor;
import org.springframework.ai.chat.client.advisor.vectorstore.QuestionAnswerAdvisor;
import org.springframework.ai.chat.client.advisor.vectorstore.VectorStoreChatMemoryAdvisor;
import org.springframework.ai.chat.memory.ChatMemory;
import org.springframework.ai.vectorstore.SearchRequest;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.stereotype.Service;

import com.spring.ai.advisor.TokenUsageAdvisor;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AdvisorService {
	private final ChatClient chatClient;
    private final org.springframework.ai.embedding.EmbeddingModel em;
    private final VectorStore vectorStore;
    
    
    

    
    public String askAiwithadvisor(String prompt, String userId) {

        return chatClient.prompt()
                .system("you are an AI assistant called Cody. You have access to the user's chat history and can provide personalized responses based on that history.")	
                .user(prompt)
                .advisors(
                		new SafeGuardAdvisor(
                			    List.of("politics", "gaming", "password")
                			),
                		 new TokenUsageAdvisor(),
                		
                        VectorStoreChatMemoryAdvisor.builder(vectorStore)
                        .defaultTopK(4)
                                .build(),
                                
                         QuestionAnswerAdvisor.builder(vectorStore)
                         .searchRequest(
                        		 SearchRequest.builder()
								.filterExpression("file_name == 'faq.pdf'")
                                .build()
                                )
                         .build()
                               
                )
                .advisors(a -> a.param(
                        ChatMemory.CONVERSATION_ID,
                        userId
                ))
                
                .call()
                .content();
    }
    
  
}
