package com.example.demop.repository;

import com.example.demop.model.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RoleRepository extends JpaRepository<Role, Long> {
    Role findOneById(long id);
    Role findOneByName(String name);

}
