package com.fastcode.emaildemo.emailbuilder.domain.irepository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.fastcode.emaildemo.domain.model.EmailHistory;

public interface EmailHistoryRepository extends JpaRepository<EmailHistory, Long> {
}
