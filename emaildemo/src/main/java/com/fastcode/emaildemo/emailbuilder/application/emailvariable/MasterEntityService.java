package com.fastcode.emaildemo.emailbuilder.application.emailvariable;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fastcode.emaildemo.emailbuilder.domain.irepository.IMasterEntityRepository;

@Service
public class MasterEntityService implements IMasterEntityService {

	@Autowired
	private IMasterEntityRepository masterEntityRepository;
	
	@Override
	public List<String> getMastersByMasterName(String name) {
		return masterEntityRepository.findMasterValueByMasterName(name);
	}

}
