package com.fastcode.emaildemo.domain.irepository;

import org.springframework.content.commons.repository.ContentStore;
import org.springframework.stereotype.Repository;

import com.fastcode.emaildemo.domain.model.File;

@Repository
public interface FileContentStore extends ContentStore<File, String> {
}
