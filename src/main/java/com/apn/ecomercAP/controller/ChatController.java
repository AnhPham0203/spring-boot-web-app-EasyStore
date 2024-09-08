package com.apn.ecomercAP.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.apn.ecomercAP.DTO.ChatMessage;

@RestController
@RequestMapping("/api/chat")
public class ChatController {
	private static Map<String, String> responses = new HashMap<>();
	private static List<ChatMessage> chatHistory = new ArrayList<>();
	static {
		responses.put("hello", "Hi there! How can I help you today?");
		responses.put("shipping", "Our standard shipping takes 3-5 business days.");
		responses.put("return policy", "You can return items within 30 days of receipt.");
		responses.put("goodbye", "Goodbye! Have a great day ahead!");
		responses.put("thank you", "You're welcome! Let us know if you need any more help.");
		// Thêm nhiều câu trả lời khác
	}

	public static String getResponse(String question) {
		for (Map.Entry<String, String> entry : responses.entrySet()) {
			if (question.toLowerCase().contains(entry.getKey())) {
				return entry.getValue();
			}
			if (question.contains("bye") || question.contains("goodbye") || question.contains("see you")) {
				return responses.get("goodbye");
			} else if (question.contains("hello") || question.contains("hi") || question.contains("hey")) {
				return responses.get("hello");
			} else if (question.contains("price") && question.contains("smartphone")) {
				return responses.get("price of smartphone");
			} else if (question.contains("thank you") || question.contains("thanks")) {
				return responses.get("thank you");
			}
		}
		return "I'm sorry, I don't understand the question. Please contact support.";
	}

	@PostMapping("/send")
	public String sendMessage(@RequestParam(name = "value") String key) {

		String chatbotReply = getResponse(key);

		return chatbotReply;
	}


}
