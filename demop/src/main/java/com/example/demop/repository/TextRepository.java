package com.example.demop.repository;

import com.example.demop.model.Text;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TextRepository extends JpaRepository<Text, Long> {
    Text findOneById(long id);

}
