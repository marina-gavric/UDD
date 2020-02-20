package com.example.demop.repository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.demop.model.Editor;

@Repository
public interface EditorRepository extends JpaRepository<Editor, Long>{

}
