package com.example.demop.services;

import java.util.List;
import org.springframework.stereotype.Service;
import com.example.demop.model.User;

@Service
public interface UserService {
	public List<User> getAll();
	public User save(User user);
	public User findUserByMail(String mail);
	public User findUserByUsername(String username);
	public void deleteUser(User user);
}
