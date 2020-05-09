package com.fastcode.emaildemo.emailbuilder.domain.irepository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.fastcode.emaildemo.emailbuilder.domain.model.EmailVariableTypeEntity;

public interface IEmailVariableTypeRepository extends JpaRepository<EmailVariableTypeEntity, Long> {

}