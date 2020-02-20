package com.example.demop.services;

import com.example.demop.model.Role;
import com.example.demop.model.ScientificArea;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface RoleService {
    public List<Role> getAll();
    public Role save(Role role);
    public Role findOneById(long id);
    public Role findOneByName(String name);
}
