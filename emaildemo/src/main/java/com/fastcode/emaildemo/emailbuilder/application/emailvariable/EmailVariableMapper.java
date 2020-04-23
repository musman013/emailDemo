package com.fastcode.emaildemo.emailbuilder.application.emailvariable;

import org.mapstruct.Mapper;

import com.fastcode.emaildemo.emailbuilder.application.emailvariable.dto.*;
import com.fastcode.emaildemo.emailbuilder.domain.model.EmailVariableEntity;


@Mapper(componentModel = "spring")
public interface EmailVariableMapper {

    EmailVariableEntity createEmailVariableInputToEmailVariableEntity(CreateEmailVariableInput emailDto);

    CreateEmailVariableOutput emailVariableEntityToCreateEmailVariableOutput(EmailVariableEntity entity);

    EmailVariableEntity updateEmailVariableInputToEmailVariableEntity(UpdateEmailVariableInput emailDto);

    UpdateEmailVariableOutput emailVariableEntityToUpdateEmailVariableOutput(EmailVariableEntity entity);

    FindEmailVariableByIdOutput emailVariableEntityToFindEmailVariableByIdOutput(EmailVariableEntity entity);

    FindEmailVariableByNameOutput emailVariableEntityToFindEmailVariableByNameOutput(EmailVariableEntity entity);
}