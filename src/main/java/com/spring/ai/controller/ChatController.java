package com.spring.ai.controller;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.vectorstore.VectorStoreChatMemoryAdvisor;
import org.springframework.ai.chat.memory.ChatMemory;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.spring.ai.tool.FlightBookingTool;
import com.spring.ai.tool.TravellingTools;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
public class ChatController {
	
	private final ChatClient chatClient;
	private final TravellingTools Ttools;
	private final FlightBookingTool flightBookingTool;
	 private final VectorStore vectorStore;
	
	@PostMapping("/chat")
	public String chat(@RequestBody String message,@RequestParam String userId) {
		String systemPrompt = String.format("""
		        You are a friendly flight booking assistant.

		        Use the available booking tools for ALL booking-related operations.

		        IMPORTANT RULES:

		        1. Never assume that a booking exists based only on conversation memory.
		        2. Before cancelling, deleting, or updating a booking, use
		           get_user_bookings to retrieve the user's CURRENT bookings.
		        3. PostgreSQL booking tool results are the source of truth.
		        4. Do not use old booking IDs from conversation memory.
		        5. Always use the current user's ID: "%s".
		        6. When the user asks to cancel/delete a booking, first find the
		           current booking using get_user_bookings.
		        7. Only update a booking ID that was returned by the booking tool.
		        """, userId);
		
		return chatClient.prompt()
		        .system(systemPrompt)
		        .user(message)

		        // Give the AI access to tools
		        .tools(Ttools, flightBookingTool)

		        // Add conversation memory from PGVector
		        .advisors(
		                VectorStoreChatMemoryAdvisor.builder(vectorStore)
		                        .defaultTopK(4)
		                        .build()
		        )

		        // Tell memory which conversation this message belongs to
		        .advisors(a -> a.param(
		                ChatMemory.CONVERSATION_ID,
		                userId
		        ))

		        .call()
		        .content();
		
	}

}
