package com.fastcode.emaildemo.domain.irepository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.stereotype.Repository;

import com.fastcode.emaildemo.domain.model.File;

@Repository
@RepositoryRestResource(path = "files", collectionResourceRel = "files")
public interface FileRepository extends JpaRepository<File, Long> {

	@Modifying
	@Query("update File u set u.emailTemplateId = :emailTemplateId where u.id = :id")
	void updateFileEmailTemplate(@Param("id") Long id, @Param("emailTemplateId") Long emailTemplateId);

	List<File> getFileByEmailTemplateId(Long emailTemplateId);

}
