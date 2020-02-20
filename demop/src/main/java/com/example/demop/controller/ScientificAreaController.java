package com.example.demop.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.example.demop.model.ScientificArea;
import com.example.demop.model.ScientificAreaDTO;
import com.example.demop.services.ScientificAreaService;

@RestController
@RequestMapping(value="/areas")
@CrossOrigin(origins = "http://localhost:4200")
public class ScientificAreaController {

	@Autowired
	private ScientificAreaService scientificService;
	
	@RequestMapping(value="/getAll", 
			method = RequestMethod.GET,
			produces = MediaType.APPLICATION_JSON_VALUE)	
	public ResponseEntity<List<ScientificAreaDTO>> getAllAreas(){	
		List<ScientificAreaDTO> areasList = new ArrayList<ScientificAreaDTO>();
		for(ScientificArea area : scientificService.getAll()){
				areasList.add(new ScientificAreaDTO(area.getId(),area.getName()));
		}
		return new ResponseEntity<List<ScientificAreaDTO>>(areasList, HttpStatus.OK);
	}
}