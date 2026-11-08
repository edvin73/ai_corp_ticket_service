package com.edvin.ticket_service.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.edvin.ticket_service.model.Ticket;

public interface TicketRepository extends JpaRepository<Ticket, Long> {
}
