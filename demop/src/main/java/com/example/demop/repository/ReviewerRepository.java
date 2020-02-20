package com.example.demop.repository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.demop.model.Reviewer;

@Repository
public interface ReviewerRepository  extends JpaRepository<Reviewer, Long>{

}
