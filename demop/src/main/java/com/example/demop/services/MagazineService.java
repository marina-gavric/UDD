package com.example.demop.services;

import java.util.List;
import org.springframework.stereotype.Service;
import com.example.demop.model.Magazine;

@Service
public interface MagazineService {
	public List<Magazine> getAll();
	public Magazine save(Magazine magazine);
	public Magazine findMagazineById(long id);
	public void deleteMagazine(Magazine magazine);
	public Magazine findMagazineByIssn(String issn);


}
