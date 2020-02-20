package com.example.demop.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.demop.model.ScientificArea;
import com.example.demop.repository.ScientificAreaRepository;

@Service
public class ScientificAreaServiceImpl implements ScientificAreaService {
	
	@Autowired
	private ScientificAreaRepository areaRepository;
	
	@Override
	public List<ScientificArea> getAll() {
		// TODO Auto-generated method stub
		return areaRepository.findAll();
	}

	@Override
	public ScientificArea save(ScientificArea area) {
		// TODO Auto-generated method stub
		return areaRepository.save(area);
	}

	@Override
	public ScientificArea findOneById(Long id) {
		return areaRepository.findOneById(id);
	}

}
