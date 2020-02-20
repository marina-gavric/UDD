package com.example.demop.services;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;

import com.example.demop.model.Magazine;
import com.example.demop.model.Role;
import com.example.demop.model.ScientificArea;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.demop.model.User;
import com.example.demop.repository.UserRepository;

@Service
public class UserServiceImpl implements UserService{

	@Autowired
	private UserRepository userRepository;
	
	@Override
	public List<User> getAll() {
		// TODO Auto-generated method stub
		return userRepository.findAll();
	}

	@Override
	public User save(User user) {
		// TODO Auto-generated method stub
		System.out.println("Usao u saveUser");
		return userRepository.save(user);
	}

	@Override
	public User findUserByMail(String mail) {
		// TODO Auto-generated method stub
		System.out.println("Usao je u findUserBymail");
		System.out.println("Mail je dobio "+mail);
		return userRepository.findOneByMail(mail);
	}

	@Override
	public User findUserByUsername(String username) {
		// TODO Auto-generated method stub
		return userRepository.findOneByUsername(username);
	}

	@Override
	public void deleteUser(User user) {
		// TODO Auto-generated method stub
		 userRepository.delete(user);
	}

	public String encrypt(String sifra) throws NoSuchAlgorithmException {
		System.out.println("Usao da enkriptuje sifru " + sifra );
		MessageDigest md = MessageDigest.getInstance("SHA-256"); 
		  
		 //pretvori sifru  u bajte
         byte[] messageDigest = md.digest(sifra.getBytes()); 
         
         StringBuilder sb = new StringBuilder();
         for (byte b : messageDigest) {
             sb.append(String.format("%02x", b));
         }
        String povratna=sb.toString();
        System.out.println("Rezultat enkripcije je "+povratna);
    	
        return povratna;
	}
	List<User> getUsersRole(String role){
		List<User> rightUsers = new ArrayList<User>();
		List<User> allusers = userRepository.findAll();
		System.out.println("Rola je "+role);
		for(User u : allusers){
			for(Role r: u.getRoles()){
				if(r.getName().equals(role)){
					System.out.println("Dodat sa imenom "+u.getUsername());
						rightUsers.add(u);
						break;
				}
			}
		}
		return rightUsers;
	}
	List<User> getEditorByScience(String idScience, String idMagazin){
		List<User> allEditors = new ArrayList<User>();
		allEditors = getUsersRole("EDITOR");
		List<User> ret = new ArrayList<User>();
		List<User> pom = new ArrayList<User>();

		for(User u: allEditors){
			for(ScientificArea a:u.getInterestedAreas()){
				String idArea = a.getId().toString();
				if(idArea.equals(idScience)){
					System.out.println("Dodaje se editor "+u.getUsername());
					pom.add(u);
					break;
				}
			}
		}
		System.out.println("Id magazina je "+idMagazin);
		for(User u:pom){
			for(Magazine m : u.getEditedMagazines()){
				String idM = m.getId().toString();
				System.out.println("poredi sa " +idM);
				if(idMagazin.equals(idM)){
					System.out.println("Dodat useer");
					ret.add(u);
				}
			}
		}

		return ret;
	}
	public List<User> getReviewerByScience(String idScience, String idMagazin){
		List<User> allReviewers = new ArrayList<User>();
		allReviewers = getUsersRole("REVIEWER");
		List<User> ret = new ArrayList<User>();
		List<User> pom = new ArrayList<User>();

		System.out.println("Area" + idScience);
		for(User u: allReviewers){
			System.out.println("Provera area");
			for(ScientificArea a:u.getInterestedAreas()){
				String idArea = a.getId().toString();
				System.out.println("Area poredjenje "+idArea);
				if(idArea.equals(idScience)){
					System.out.println("Dodaje se reviewer "+u.getUsername());
					pom.add(u);
					break;
				}
			}
		}
		System.out.println("Id magazina je "+idMagazin);
		for(User u:pom){
			for(Magazine m : u.getReviewedMagazines()){
				String idM = m.getId().toString();
				System.out.println("poredi sa " +idM);
				if(idMagazin.equals(idM)){
					System.out.println("Dodat useer");
					ret.add(u);
				}
			}
		}

		return ret;
	}
}
