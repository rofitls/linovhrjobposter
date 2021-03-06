package com.jobposter.controller;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.jobposter.entity.Users;
import com.jobposter.exception.ErrorException;
import com.jobposter.service.UserService;

import net.sf.jasperreports.engine.JRException;

import com.jobposter.service.ApplicationStateChangeService;
import com.jobposter.service.DocumentService;
import com.jobposter.service.DocumentTypeService;
import com.jobposter.service.EmailService;
import com.jobposter.service.RoleService;
import com.jobposter.service.UserPasswordService;
import com.jobposter.entity.Document;
import com.jobposter.entity.DocumentType;
import com.jobposter.entity.Mail;
import com.jobposter.entity.PasswordPojo;
import com.jobposter.entity.Register;
import com.jobposter.entity.ReportInput;
import com.jobposter.entity.ReportMasterPojo;
import com.jobposter.entity.Role;
import com.jobposter.entity.UserPassword;
import com.jobposter.entity.UserPojo;
import com.jobposter.config.JwtTokenUtil;

@RestController
//@RequestMapping("/apl")
@CrossOrigin("*")
public class UserController {
	
	@Autowired
	private PasswordEncoder bcryptEncoder;
	
	@Autowired
	private AuthenticationManager authenticationManager;

	@Autowired
	private JwtTokenUtil jwtTokenUtil;
	
	@Autowired
	private UserService userService;
	
	@Autowired
	private EmailService emailService;
	
	@Autowired
	private RoleService roleService;
	
	@Autowired
	private DocumentTypeService documentTypeService;
	
	@Autowired
	private DocumentService documentService;
	
	@Autowired
	private ApplicationStateChangeService stateService;
	
	@Autowired
	private UserPasswordService userPasswordService;
	
	@PostMapping("/user")
	public ResponseEntity<?> insert(@RequestBody Users appl) throws ErrorException{
		try {
			userService.insert(appl);
		}catch(Exception e) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
		}
		return ResponseEntity.status(HttpStatus.CREATED).body(appl);
	}
	
	@PostMapping("/user/login")
	public ResponseEntity<?> createAuthenticationToken(@RequestBody UserPojo authenticationRequest) throws Exception {
		
		try {
		
			Object obj = new Object();
			List<Object> objs = new ArrayList<>(); 
			obj = userService.findByUsername(authenticationRequest.getUsername());
			objs.add(obj);
			
			authenticate(authenticationRequest.getUsername(), authenticationRequest.getPassword());
			
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
			UserPassword up = new UserPassword();
			Mail mail = new Mail();
			
			 int leftLimit = 48; // numeral '0'
			 int rightLimit = 122; // letter 'z'
			 int targetStringLength = 10;
			 Random random = new Random();
			 
			    String generatedString = random.ints(leftLimit, rightLimit + 1)
			      .filter(i -> (i <= 57 || i >= 65) && (i <= 90 || i >= 97))
			      .limit(targetStringLength)
			      .collect(StringBuilder::new, StringBuilder::appendCodePoint, StringBuilder::append)
			      .toString();
		    
		    appl.setFirstName(reg.getFirstName());
		    appl.setLastName(reg.getLastName());
		    appl.setUsername(reg.getEmail());
		    	
		    if(reg.getRole().getId().equalsIgnoreCase(roleService.findByName("Applicant").getId())) {
		    	appl.setGender(reg.getGender());
			    appl.setDateOfBirthday(reg.getDateOfBirthday());
			    LocalDate now = LocalDate.now();
			    LocalDate dob = Instant.ofEpochMilli(reg.getDateOfBirthday().getTime())
	            .atZone(ZoneId.systemDefault())
	            .toLocalDate();
			    Double age = dob.until(now, ChronoUnit.DAYS) / 365.2425d;
			    appl.setAge(age.intValue());	
		    }
		    
		    appl.setPhone(reg.getPhone());
		    appl.setRole(reg.getRole());
		    mail.setName(reg.getFirstName()+" "+reg.getLastName());
		    mail.setSubject("Linov HR Job Poster Password Account");
		    mail.setContent(generatedString);
		    mail.setTo(reg.getEmail());
			valIdNull(appl);
			valBkNotNull(appl);
			valBkNotExist(appl);
			valNonBk(appl, reg.getRole());
		    userService.insert(appl);
		    emailService.sendEmail(mail);
		    appl = userService.findByUsername(reg.getEmail());
		    up.setUser(appl);
		    up.setPassword(generatedString);
		    userPasswordService.insert(up);
			return ResponseEntity.status(HttpStatus.OK).body(appl);
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
			valNonBk(appl, appl.getRole());
			userService.update(appl);
			return ResponseEntity.status(HttpStatus.OK).body(appl);
		}catch(Exception e) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
		}
	}
	
	@PutMapping("/user/change-password")
	public ResponseEntity<?> changePassword(@RequestBody PasswordPojo pas) throws ErrorException {
		try {
			UserPassword userPas = userPasswordService.findById(pas.getId());
			valIdExist(userPas.getUser().getId());
			valPasswordMatch(pas.getOldPas(), pas.getNewPas(), userPas);
			userPas.setPassword(pas.getNewPas());
			userPasswordService.update(userPas);
			Users user = userService.findById(pas.getId());
			user.setImage(null);
			return ResponseEntity.status(HttpStatus.OK).body(user);
		}catch(Exception e) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
		}
	}
	
	@DeleteMapping("/user/{id}")
	public ResponseEntity<?> delete(@PathVariable String id) throws ErrorException {
		try {
			valIdExist(id);
			Users user = userService.findById(id);
			userService.delete(user);
			user.setImage(null);
			return ResponseEntity.ok(user);
		}catch(Exception e) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
		}
		
	}
	
	@GetMapping("/user/id/{id}")
	public ResponseEntity<?> getById(@PathVariable String id) throws ErrorException {
		try {
			valIdExist(id);
			return ResponseEntity.ok(userService.findById(id));	
		}catch(Exception e) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
		}
		
	}
	
	@GetMapping("/user")
	public ResponseEntity<?> getAll()  throws ErrorException {
		try {
			return ResponseEntity.ok(userService.findAll());	
		}catch(Exception e) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
		}
		
	}
	
	@PostMapping("/user/report")
	public ResponseEntity<?> exportReport(@RequestBody ReportInput ri, HttpServletRequest request) throws FileNotFoundException, JRException{
			try {
				Users user = userService.findById(ri.getRecruiter());
				
				String fileName = "";
				List<ReportMasterPojo> listRp;
				
				System.out.println(ri.getRecruiter());
				System.out.println(ri.getYear());
				System.out.println(ri.getJob());
				
				if(ri.getRecruiter() == null && ri.getYear() == null) {
					listRp = stateService.reportPerJob(ri.getJob());
					fileName = userService.exportReportPerJob(listRp);
				}else {
					listRp = stateService.reportMaster(ri.getRecruiter(), ri.getYear());
					
					for(ReportMasterPojo rp : listRp) {
						rp.setRecruiterName(user.getFirstName()+" "+user.getLastName());
					}
					
					if(ri.getYear() != null) {
						fileName = userService.exportReportPerYear(listRp);
					}else {
						fileName = userService.exportReport(listRp);
					}
					
				}
				// Load file as Resource
		        Resource resource = userService.loadFileAsResource(fileName);

		        // Try to determine file's content type
		        String contentType = request.getServletContext().getMimeType(resource.getFile().getAbsolutePath());
		        

		        // Fallback to the default content type if type could not be determined
		        if(contentType == null) {
		            contentType = "application/octet-stream";
		        }

		        return ResponseEntity.ok()
		                .contentType(MediaType.parseMediaType(contentType))
		                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + resource.getFilename() + "\"")
		                .body(resource);
			}catch(Exception e) {
				return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
			}
	}
	
	@PostMapping("/user/json")
	public ResponseEntity<?> exportJSON(@RequestBody ReportInput ri, HttpServletRequest request) throws FileNotFoundException, JRException{
			try {
				Users user = userService.findById(ri.getRecruiter());
				
				//String fileName = "";
				List<ReportMasterPojo> listRp;
				
				System.out.println(ri.getRecruiter());
				System.out.println(ri.getYear());
				System.out.println(ri.getJob());
				
				if(ri.getRecruiter() == null && ri.getYear() == null) {
					listRp = stateService.reportPerJob(ri.getJob());
					return ResponseEntity.ok(listRp);
				}else {
					listRp = stateService.reportMaster(ri.getRecruiter(), ri.getYear());
					
					if(listRp == null) {
						Object obj = new Object();
						obj = "Report tidak ada";
						return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(obj);
					}else {
						for(ReportMasterPojo rp : listRp) {
							rp.setRecruiterName(user.getFirstName()+" "+user.getLastName());
						}
						if(ri.getYear() != null) {
							return ResponseEntity.ok(listRp);
						}else {
							return ResponseEntity.ok(listRp);
						}	
					}
					
					
				}
			}catch(Exception e) {
				e.printStackTrace();
				return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
			}
	}
	
	@GetMapping("/user/report/json/{id}")
	public ResponseEntity<?> reportJSON(@RequestBody ReportInput ri) throws FileNotFoundException, JRException{
		try {
			List<ReportMasterPojo> listRp = stateService.reportMaster(ri.getRecruiter(), ri.getYear());
			return ResponseEntity.ok(listRp);	
		}catch(Exception e) {
			e.printStackTrace();
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
		}
	}
	
	@PostMapping("/user/upload/{id}")
	public ResponseEntity<?> saveImage(@PathVariable String id, @RequestPart MultipartFile[] upload ) throws IOException {
		try {
			Users user = userService.findById(id);
			user.setImage(upload[0].getBytes());
			user.setImageType(upload[0].getContentType());
			user.setImageFileName(upload[0].getOriginalFilename());
			userService.insert(user);
			return ResponseEntity.status(HttpStatus.OK).body(user);
		}catch(Exception e) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
		}
	}
	
	@PostMapping("/user/upload/{id}/{path}")
	public ResponseEntity<?> uploadDocument(@RequestPart MultipartFile[] upload, @PathVariable String id, @PathVariable String path) {
		try {
			Users user = userService.findById(id);
			DocumentType dt = documentTypeService.findById(path);
			Document doc = documentService.findADUser(user.getId(), dt.getId());
			if(dt.isFlag() == false) {
				doc = new Document();
				doc.setFile(upload[0].getBytes());
				doc.setFileType(upload[0].getContentType());
				doc.setFileName(upload[0].getOriginalFilename());
				doc.setDocType(dt);
				doc.setUser(user);
				documentService.insert(doc);
			}
			if(doc == null) {
				doc = new Document();
				doc.setFile(upload[0].getBytes());
				doc.setFileType(upload[0].getContentType());
				doc.setFileName(upload[0].getOriginalFilename());
				doc.setDocType(dt);
				doc.setUser(user);
				documentService.insert(doc);
			}else if(doc != null) {
				doc.setFile(upload[0].getBytes());
				doc.setFileType(upload[0].getContentType());
				doc.setFileName(upload[0].getOriginalFilename());
				doc.setDocType(dt);
				doc.setUser(user);
				documentService.insert(doc);
			}
			return ResponseEntity.status(HttpStatus.OK).body(user);
		}catch(Exception e) {
			e.printStackTrace();
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
		}
	}
	
	private void valPasswordMatch(String oldPw, String newPw, UserPassword user) throws Exception {
		boolean flag = bcryptEncoder.matches(oldPw, user.getPassword());
		if(!flag) {
			throw new Exception("Password not match");
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
		if(userService.findByBk(user.getUsername())!=null) {
			throw new Exception("User already exists");
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
	
	private Exception valNonBk(Users user, Role role) throws Exception {
		if(user.getFirstName() == null || user.getLastName() == null) {
			throw new Exception("Please fill in the blanks");
		}
		if(role.getRoleName().equalsIgnoreCase("Applicant")) {
			if(user.getDateOfBirthday()==null || user.getGender() == null || user.getPhone() == null) {
				throw new Exception("Please fill in the blanks");
			}	
		}
		return null;
	}
}

