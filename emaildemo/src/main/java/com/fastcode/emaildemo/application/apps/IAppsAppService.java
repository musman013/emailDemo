package com.fastcode.emaildemo.application.apps;

import java.util.List;
import javax.validation.constraints.Positive;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import com.fastcode.emaildemo.commons.search.SearchCriteria;
import com.fastcode.emaildemo.application.apps.dto.*;

@Service
public interface IAppsAppService {

	CreateAppsOutput create(CreateAppsInput apps);

    void delete(Long id);

    UpdateAppsOutput update(Long id, UpdateAppsInput input);

    FindAppsByIdOutput findById(Long id);

    List<FindAppsByIdOutput> find(SearchCriteria search, Pageable pageable) throws Exception;

}
