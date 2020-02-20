package com.example.demop.repository;
import com.example.demop.model.Keyword;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface KeyWordRepository extends JpaRepository<Keyword, Long>{
    Keyword findOneById(long id);
    Keyword findOneByName(String name);

}
