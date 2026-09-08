package com.spring.ai.service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.SimpleLoggerAdvisor;
import org.springframework.ai.chat.prompt.PromptTemplate;
import org.springframework.ai.document.Document;
import org.springframework.stereotype.Service;
import org.springframework.ai.vectorstore.SearchRequest;
import org.springframework.ai.vectorstore.VectorStore;
import com.spring.ai.dto.Joke;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class RagAi {

	private final ChatClient chatClient;
	private final org.springframework.ai.embedding.EmbeddingModel em;
	private final VectorStore vectorStore;

	public String askAI(String prompt) {

	    String template = """
	            You are an AI assistant called Cody.

	            Rules:
	            - Use ONLY the information provided in the context.
	            - Do NOT use your own knowledge.
	            - Do NOT make assumptions.
	            - If the answer is not present in the context,
	              respond exactly with "I don't know".

	            Context:
	            {context}

	            Answer in a friendly, conversational tone.
	            """;

	    List<Document> doc = vectorStore.similaritySearch(
	            SearchRequest.builder()
	                    .query(prompt)
	                    .topK(2)
	                    .similarityThreshold(0.7)
	                    .filterExpression(
	                        "topic == 'Spring AI' || topic == 'Vector Store'"
	                    )
	                    .build()
	    );

	    // VERY IMPORTANT
	    if (doc.isEmpty()) {
	        return "I don't know";
	    }

	    String context = doc.stream()
	            .map(Document::getText)
	            .collect(Collectors.joining("\n\n"));

	    PromptTemplate pt = new PromptTemplate(template);

	    String systemPrompt = pt.render(
	            Map.of("context", context)
	    );

	    return chatClient.prompt()
	            .system(systemPrompt)
	            .user(prompt)
	            .call()
	            .content();
	}

	public void ingestData() {

		Document doc1 = new Document("""
				Spring AI is a Java framework for building AI-powered applications
				using the Spring ecosystem. It provides abstractions for working with
				chat models, embedding models, vector stores, prompts, and AI tools.
				Developers can use Spring AI to integrate large language models into
				Spring Boot applications.
				""", Map.of("topic", "Spring AI", "category", "Introduction", "difficulty", "Beginner", "source",
				"spring-ai-basics"));

		Document doc2 = new Document("""
				Embeddings in Spring AI convert text into numerical vectors.
				These vectors represent the semantic meaning of the text.
				Embeddings are commonly used for similarity search, document retrieval,
				and Retrieval-Augmented Generation. Spring AI provides an EmbeddingModel
				abstraction that allows applications to generate embeddings from text.
				""", Map.of("topic", "Embeddings", "category", "Core Concept", "difficulty", "Intermediate", "source",
				"spring-ai-embeddings"));

		Document doc3 = new Document("""
				Vector stores in Spring AI are used to store and search document
				embeddings. A document is converted into an embedding and stored
				in a vector database. When a user asks a question, the question is
				converted into an embedding and similarity search is performed to
				retrieve the most relevant documents. PostgreSQL with pgvector can
				be used as a vector store with Spring AI.
				""", Map.of("topic", "Vector Store", "category", "RAG", "difficulty", "Intermediate", "source",
				"spring-ai-vectorstore"));
		vectorStore.add(List.of(doc1, doc2, doc3));
	}

	public List<Document> similaritySearch(String text) {
		return vectorStore.similaritySearch(SearchRequest.builder().query(text).topK(2).build());
	}

}