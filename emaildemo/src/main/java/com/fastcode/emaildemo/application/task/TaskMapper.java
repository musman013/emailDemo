package com.fastcode.emaildemo.application.task;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import com.fastcode.emaildemo.domain.model.AppsEntity;
import com.fastcode.emaildemo.application.task.dto.*;
import com.fastcode.emaildemo.domain.model.TaskEntity;

@Mapper(componentModel = "spring")
public interface TaskMapper {

   TaskEntity createTaskInputToTaskEntity(CreateTaskInput taskDto);
   
   @Mappings({ 
   @Mapping(source = "apps.id", target = "appId"),                   
   @Mapping(source = "apps.name", target = "appsDescriptiveField"),                    
   }) 
   CreateTaskOutput taskEntityToCreateTaskOutput(TaskEntity entity);

   TaskEntity updateTaskInputToTaskEntity(UpdateTaskInput taskDto);

   @Mappings({ 
   @Mapping(source = "apps.id", target = "appId"),                   
   @Mapping(source = "apps.name", target = "appsDescriptiveField"),                    
   }) 
   UpdateTaskOutput taskEntityToUpdateTaskOutput(TaskEntity entity);

   @Mappings({ 
   @Mapping(source = "apps.id", target = "appId"),                   
   @Mapping(source = "apps.name", target = "appsDescriptiveField"),                    
   }) 
   FindTaskByIdOutput taskEntityToFindTaskByIdOutput(TaskEntity entity);


   @Mappings({
   @Mapping(source = "apps.description", target = "description"),                  
   @Mapping(source = "apps.id", target = "id"),                  
   @Mapping(source = "task.id", target = "taskId"),
   })
   GetAppsOutput appsEntityToGetAppsOutput(AppsEntity apps, TaskEntity task);

}
