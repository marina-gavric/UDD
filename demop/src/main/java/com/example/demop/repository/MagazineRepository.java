package com.example.demop.repository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.demop.model.Magazine;

@Repository
public interface MagazineRepository extends JpaRepository<Magazine, Long>{

	Magazine findOneById(long id);
	Magazine findOneByIssn(String issn);

}
