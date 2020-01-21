package com.jobposter.controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
//import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.jobposter.entity.Users;
import com.jobposter.exception.ErrorException;
import com.jobposter.service.UserService;
import com.jobposter.service.CityService;
import com.jobposter.service.DocumentService;
import com.jobposter.service.DocumentTypeService;
import com.jobposter.service.EmailService;
import com.jobposter.service.RoleService;
import com.jobposter.entity.Document;
import com.jobposter.entity.DocumentType;
import com.jobposter.entity.Mail;
import com.jobposter.entity.Register;
import com.jobposter.config.JwtTokenUtil;

@RestController
//@RequestMapping("/apl")
@CrossOrigin("*")
public class UserController {
	
	@Autowired
	private AuthenticationManager authenticationManager;

	@Autowired
	private JwtTokenUtil jwtTokenUtil;
	
	@Autowired
	private UserService userService;
	
	@Autowired
	private EmailService emailService;
	
	@Autowired
	private CityService cityService;
	
	@Autowired
	private RoleService roleService;
	
	@Autowired
	private DocumentTypeService documentTypeService;
	
	@Autowired
	private DocumentService documentService;
	
	@PostMapping("/user")
	public ResponseEntity<?> insert(@RequestBody Users appl) throws ErrorException{
		try {
			userService.insert(appl);
		}catch(Exception e) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
		}
		return ResponseEntity.status(HttpStatus.CREATED).body("Model Berhasil Ditambah");
	}
	
	@PostMapping("/user/login")
	public ResponseEntity<?> createAuthenticationToken(@RequestBody Users authenticationRequest) throws Exception {
		
		try {
		
			Object obj = new Object();
			List<Object> objs = new ArrayList<>(); 
			obj = userService.findByUsername(authenticationRequest.getUsername());
			objs.add(obj);
			
			authenticate(authenticationRequest.getUsername(), authenticationRequest.getPassword());

//			final UserDetails userDetails = userDetailsService
//					.loadUserByUsername(authenticationRequest.getUsername());
			
			final UserDetails userDetails = userService
					.loadUserByUsername(authenticationRequest.getUsername());

			final String token = jwtTokenUtil.generateToken(userDetails);
			objs.add(token);
			return ResponseEntity.ok(objs);
			
		}catch(Exception e) {
			
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Gagal Login");
		}

	}
	
	@PostMapping("/user/register")
	public ResponseEntity<?> saveUser(@RequestBody Register reg) throws Exception {
		try {
			Users appl = new Users();
			Mail mail = new Mail();
			int leftLimit = 97; // letter 'a'
		    int rightLimit = 122; // letter 'z'
		    int targetStringLength = 10;
		    Random random = new Random();
		 
		    String generatedString = random.ints(leftLimit, rightLimit + 1)
		      .limit(targetStringLength)
		      .collect(StringBuilder::new, StringBuilder::appendCodePoint, StringBuilder::append)
		      .toString();
		    
		    appl.setFirstName(reg.getFirstName());
		    appl.setLastName(reg.getLastName());
		    appl.setUsername(reg.getEmail());
		    appl.setPassword(generatedString);
		    appl.setAddress(reg.getAddress());
		    appl.setGender(reg.getGender());
		    appl.setDateOfBirthday(reg.getDateOfBirthday());
		    appl.setPhone(reg.getPhone());
		    appl.setCity(reg.getCity());
		    appl.setRole(reg.getRole());
		    mail.setName(reg.getFirstName()+" "+reg.getLastName());
		    mail.setSubject("Linov HR Job Poster Password Account");
		    mail.setContent(generatedString);
		    mail.setTo(reg.getEmail());
			valIdNull(appl);
			valBkNotNull(appl);
			valBkNotExist(appl);
			valNonBk(appl);
		    userService.insert(appl);
		    emailService.sendEmail(mail);
			return ResponseEntity.status(HttpStatus.OK).body("Berhasil register");
		}catch(Exception e) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
		}
		
	}
	
	@PutMapping("/user")
	public ResponseEntity<?> update(@RequestBody Users appl) throws ErrorException{
		try {
			valIdNotNull(appl);
			valIdExist(appl.getId());
			valBkNotNull(appl);
			valBkNotChange(appl);
			valNonBk(appl);
			userService.update(appl);
		}catch(Exception e) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
		}
		return ResponseEntity.status(HttpStatus.OK).body("Model Berhasil Diperbarui");
	}
	
	@DeleteMapping("/user/{id}")
	public ResponseEntity<?> delete(@PathVariable String id) throws ErrorException {
		try {
			valIdExist(id);
			userService.delete(id);
		}catch(Exception e) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
		}
		return ResponseEntity.status(HttpStatus.OK).body("Model Berhasil Dihapus");
	}
	
	@GetMapping("/user/id/{id}")
	public ResponseEntity<?> getById(@PathVariable String id) throws ErrorException {
		return ResponseEntity.ok(userService.findById(id));
	}
	
	@GetMapping("/user")
	public ResponseEntity<?> getAll()  throws ErrorException {
		return ResponseEntity.ok(userService.findAll());
	}
	
	@PutMapping("/user/upload/{id}")
	public ResponseEntity<?> saveImage(@PathVariable String id, @RequestPart MultipartFile[] upload ) throws IOException {
		try {
			Users user = userService.findById(id);
			user.setImage(upload[0].getBytes());
			user.setImageType(upload[0].getContentType());
			userService.insert(user);
			return ResponseEntity.status(HttpStatus.OK).body("Model Berhasil Diperbarui");
		}catch(Exception e) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
		}
	}
	
	@PostMapping("/user/upload/{id}/{path}")
	public ResponseEntity<?> uploadDocument(@RequestPart MultipartFile[] upload, @PathVariable String id, @PathVariable String path) {
		try {
			Users user = userService.findById(id);
			DocumentType dt = documentTypeService.findById(path);
			Document doc = new Document();
			doc.setFile(upload[0].getBytes());
			doc.setFileType(upload[0].getContentType());
			doc.setFileName(upload[0].getOriginalFilename());
			doc.setDocType(dt);
			doc.setUser(user);
			documentService.insert(doc);
			return ResponseEntity.status(HttpStatus.OK).body("Model Berhasil Diperbarui");
		}catch(Exception e) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
		}
	}
	
	private void authenticate(String username, String password) throws Exception {
		try {
			authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username, password));
		} catch (DisabledException e) {
			throw new Exception("USER_DISABLED", e);
		} catch (BadCredentialsException e) {
			throw new Exception("INVALID_CREDENTIALS", e);
		}
	}
	
	private Exception valIdNull(Users user) throws Exception {
		if(user.getId()!=null) {
			throw new Exception("Register Failed");
		}
		return null;
	}
	
	private Exception valIdNotNull(Users user) throws Exception{
		if(user.getId()==null) {
			throw new Exception("User doesn't exist");
		}
		return null;
	}
	
	private Exception valIdExist(String id) throws Exception{
		if(userService.findById(id)==null) {
			throw new Exception("User doesn't exists");
		}
		return null;
	}
	
	private Exception valBkNotNull(Users user) throws Exception{
		if(user.getUsername()==null) {
			throw new Exception("Username code must be filled");
		}else if(user.getPhone()==null) {
			throw new Exception("Phone must be filled");
		}
		return null;
	}
	
	private Exception valBkNotExist (Users user) throws Exception{
		if(userService.findByBk(user.getUsername(), user.getPhone())!=null) {
			throw new Exception("User already exists");
		}else if(cityService.findById(user.getCity().getId())== null || cityService.findById(user.getCity().getId()).isActiveState()==false) {
			throw new Exception("City doesn't exists");
		}else if(roleService.findById(user.getRole().getId())==null || roleService.findById(user.getRole().getId()).isActiveState()==false) {
			throw new Exception("Role doesn't exists");
		}
		return null;
	}
	
	private Exception valBkNotChange(Users user) throws Exception{
		if(!user.getUsername().equalsIgnoreCase(userService.findById(user.getId()).getUsername())) {
			throw new Exception("BK cannot change");
		}
		return null;
	}
	
	private Exception valNonBk(Users user) throws Exception {
		if(user.getFirstName() == null || user.getLastName() == null || user.getDateOfBirthday()==null || user.getGender() == null) {
			throw new Exception("Please fill in the blanks");
		}
		return null;
	}
}

