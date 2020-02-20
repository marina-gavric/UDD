package com.example.demop.services;

import java.util.List;
import org.springframework.stereotype.Service;
import com.example.demop.model.ScientificArea;

@Service
public interface ScientificAreaService {
	public List<ScientificArea> getAll();
	public ScientificArea save(ScientificArea area);
	public ScientificArea findOneById(Long id);

}
