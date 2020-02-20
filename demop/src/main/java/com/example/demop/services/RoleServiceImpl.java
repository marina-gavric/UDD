package com.example.demop.services;

import com.example.demop.model.Role;
import com.example.demop.repository.RoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
public class RoleServiceImpl implements RoleService {
    @Autowired
    private RoleRepository roleRepository;

    @Override
    public List<Role> getAll() {
        return roleRepository.findAll();
    }

    @Override
    public Role save(Role role) {
        return roleRepository.save(role);
    }

    @Override
    public Role findOneById(long id) {
        return roleRepository.findOneById(id);
    }

    @Override
    public Role findOneByName(String name) {
        return roleRepository.findOneByName(name);
    }


}
