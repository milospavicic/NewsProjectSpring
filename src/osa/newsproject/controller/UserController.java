package osa.newsproject.controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import osa.newsproject.dto.PostDTO;
import osa.newsproject.dto.UserDTO;
import osa.newsproject.entity.Post;
import osa.newsproject.entity.User;
import osa.newsproject.service.UserServiceInterface;

@RestController
@RequestMapping(value = "api/users")
public class UserController {

	@Autowired
	private UserServiceInterface userServiceIterface;
	
	private final Logger logger = LoggerFactory.getLogger(this.getClass());

	@GetMapping
	public ResponseEntity<List<UserDTO>> getUsers() {
		logger.info("GET Method, request for all users.");
		List<User> users = userServiceIterface.findAll();
		List<UserDTO> userDTOS = new ArrayList<UserDTO>();
		for (User u : users) {
			userDTOS.add(new UserDTO(u));
		}
		return new ResponseEntity<List<UserDTO>>(userDTOS, HttpStatus.OK);
	}

	@GetMapping(value = "/{id}")
	public ResponseEntity<UserDTO> getUser(@PathVariable("id") Integer id) {
		logger.info("GET Method, request for user by id: "+id+".");
		User user = userServiceIterface.findOne(id);
		if (user == null) {
			logger.error("User with id: "+id+" not found.");
			return new ResponseEntity<UserDTO>(HttpStatus.NOT_FOUND);
		}
		return new ResponseEntity<UserDTO>(new UserDTO(user), HttpStatus.OK);
	}

	@GetMapping(value = "/get/{username}")
	public ResponseEntity<UserDTO> getUserByUsername(@PathVariable("username") String username) {
		logger.info("GET Method, request for user by username: "+username+".");
		User user = userServiceIterface.findByUsername(username);
		if (user == null) {
			logger.error("User with username: "+username+" not found.");
			return new ResponseEntity<UserDTO>(HttpStatus.NOT_FOUND);
		}
		return new ResponseEntity<UserDTO>(new UserDTO(user), HttpStatus.OK);
	}
	@GetMapping(value="/{username}/{password}")
	public ResponseEntity<UserDTO> getUserByUsernameAndPassword(@PathVariable("username") String username,@PathVariable("password") String password){
		logger.info("GET Method, request for user by username: "+username+" and password: "+password+".[LOGIN]");
		User user = userServiceIterface.findByUsernameAndPassword(username, password);
		if(user == null) {
			logger.error("User with username: "+username+" and password: "+password+" not found.");
			return new ResponseEntity<UserDTO>(HttpStatus.NOT_FOUND);
		}
		System.out.println("Username: "+user.getUsername());
		return new ResponseEntity<UserDTO>(new UserDTO(user), HttpStatus.OK);
	}

	@PostMapping(consumes = "application/json")
	public ResponseEntity<UserDTO> saveUser(@RequestBody UserDTO userDTO) {
		logger.info("POST Method, create new user with username: "+userDTO.getUsername()+".");
		User usernameTaken = userServiceIterface.findByUsername(userDTO.getUsername());
		if (usernameTaken != null) {
			logger.error("Username : "+userDTO.getUsername()+" is taken.");
			return new ResponseEntity<UserDTO>(HttpStatus.FORBIDDEN);
		}
		
		User user = new User();
		user.setName(userDTO.getName());
		user.setUsername(userDTO.getUsername());
		user.setPassword(userDTO.getPassword());
		user.setUserType(userDTO.getUserType());
		user = userServiceIterface.save(user);
		return new ResponseEntity<UserDTO>(new UserDTO(user), HttpStatus.CREATED);

	}
	@PostMapping(value = "/upload_photo")
    public ResponseEntity<Void> uploadUserPic(@RequestParam("id") Integer id,@RequestParam("photo") MultipartFile photo){
		logger.info("POST Method, upload photo for user with id: "+id+".");
        User user=userServiceIterface.findOne(id);
        if(user == null) {
        	logger.error("User with id: "+id+" not found.");
            return  new ResponseEntity<Void>(HttpStatus.NOT_FOUND);
        }

        try {
        	user.setPhoto(photo.getBytes());
		} catch (IOException e) {
			e.printStackTrace();
			logger.error("Upload photo for user with id: "+id+" failed.");
			return  new ResponseEntity<Void>(HttpStatus.BAD_REQUEST);
		}

        user=userServiceIterface.save(user);
        return new ResponseEntity<Void>(HttpStatus.OK);
    }
	
	@PutMapping(value = "/{id}", consumes = "application/json")
	public ResponseEntity<UserDTO> editUser(@RequestBody UserDTO userDTO, @PathVariable("id") Integer id) {
		logger.info("PUT Method, update user with id: "+id+".");
		User user = userServiceIterface.findOne(id);
		if (user == null) {
			logger.error("User with id: "+id+" not found.");
			return new ResponseEntity<UserDTO>(HttpStatus.NOT_FOUND);
		}
		user.setName(userDTO.getName());
		user.setPassword(userDTO.getPassword());
		user.setUserType(userDTO.getUserType());
		user = userServiceIterface.save(user);

		return new ResponseEntity<UserDTO>(new UserDTO(user), HttpStatus.OK);
	}

	@DeleteMapping(value = "/{id}")
	public ResponseEntity<Void> deleteUser(@PathVariable("id") Integer id) {
		logger.info("DELETE Method, delete user with id: "+id+".");
		User user = userServiceIterface.findOne(id);
		if (user == null) {
			logger.error("User with id: "+id+" not found.");
			return new ResponseEntity<Void>(HttpStatus.NOT_FOUND);
		}

		userServiceIterface.remove(id);
		return new ResponseEntity<Void>(HttpStatus.OK);
	}

}
