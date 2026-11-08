package com.edvin.ticket_service.mcp;

import java.util.List;

import org.springframework.ai.mcp.annotation.McpPrompt;
import org.springframework.stereotype.Component;

import com.edvin.ticket_service.model.Ticket;
import com.edvin.ticket_service.service.TicketService;

import io.modelcontextprotocol.spec.McpSchema;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Component
@RequiredArgsConstructor
@Slf4j
public class TicketPromptProvider {
	
	
	private final TicketService ticketService;
	
	@McpPrompt(
			name = "analyze-tickets",
			description = "Provides a prompt for analyzing support tickets." 
	)
	public McpSchema.GetPromptResult analyzeTicketsPrompt() {
		
		
		List<Ticket> tickets = ticketService.getAllTickets();
		
		StringBuilder ticketSummary = new StringBuilder();
		
		for (Ticket ticket : tickets) {
			ticketSummary.append("Ticket ").append(ticket.getId())
				.append(" : ").append(ticket.getTitle())
				.append(", Priority: ").append(ticket.getPriority())
				.append(", Status: ").append(ticket.getStatus()) 

				.append("\n");
		}
		 
		String prompt = """
				You are an AI assistant that helps analyze support tickets.
				Here are the current tickets:
				%s
				Please provide a summary of the tickets, including any patterns or insights you can find.
				
				Ticket Summary:
				""" + ticketSummary.toString() + """
						
				Provide :
				1. Overall ticket situation summary.
				2. High priority issues that need immediate attention.
				3. Possible operational risks based on the current tickets.
				4. Recommend next actions
				
				""".formatted(ticketSummary.toString());
				
				List<McpSchema.PromptMessage> messages = List.of(
				          new McpSchema.PromptMessage(
				              McpSchema.Role.USER,
				              new McpSchema.TextContent(prompt)
				          )
				      );
		
		return McpSchema.GetPromptResult
					.builder(messages)
					.description("Prompt for analyzing support tickets")
					.build();
	}

}
