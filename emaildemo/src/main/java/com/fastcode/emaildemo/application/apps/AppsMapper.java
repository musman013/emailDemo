package com.fastcode.emaildemo.application.apps;

import org.mapstruct.Mapper;
import com.fastcode.emaildemo.application.apps.dto.*;
import com.fastcode.emaildemo.domain.model.AppsEntity;

@Mapper(componentModel = "spring")
public interface AppsMapper {

   AppsEntity createAppsInputToAppsEntity(CreateAppsInput appsDto);
   
   CreateAppsOutput appsEntityToCreateAppsOutput(AppsEntity entity);

   AppsEntity updateAppsInputToAppsEntity(UpdateAppsInput appsDto);

   UpdateAppsOutput appsEntityToUpdateAppsOutput(AppsEntity entity);

   FindAppsByIdOutput appsEntityToFindAppsByIdOutput(AppsEntity entity);


}
