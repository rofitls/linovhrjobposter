package com.jobposter.controller;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.jobposter.entity.FilterJob;
import com.jobposter.entity.JobPosting;
import com.jobposter.entity.JobPostingPojo;
import com.jobposter.entity.JobQuota;
import com.jobposter.exception.ErrorException;
import com.jobposter.service.CityService;
import com.jobposter.service.JobPositionService;
import com.jobposter.service.JobPostingService;
import com.jobposter.service.JobQuotaService;
import com.jobposter.service.UserService;

@RestController
//@RequestMapping("/admin")
@CrossOrigin("*")
public class JobPostingController {
	
	@Autowired
	private JobPostingService jobPostingService;
	
	@Autowired
	private UserService userService;
	
	@Autowired
	private JobPositionService jobPositionService;
	
	@Autowired
	private CityService cityService;
	
	@Autowired
	private JobQuotaService jobQuotaService;
	
	@PostMapping("/admin/job-posting")
	public ResponseEntity<?> insert(@RequestBody JobPostingPojo jPostPojo) throws ErrorException{
		try {
			JobPosting jpost = new JobPosting();
			jpost.setJobTitleName(jPostPojo.getJobTitleName());
			jpost.setSalary(jPostPojo.getSalary());
			jpost.setStartDate(new Date());
			jpost.setEndDate(jPostPojo.getEndDate());
			jpost.setAddress(jPostPojo.getAddress());
			jpost.setCompany(jPostPojo.getCompany());
			jpost.setCity(cityService.findById(jPostPojo.getIdCity()));
			jpost.setJobPosition(jobPositionService.findById(jPostPojo.getIdJobPosition()));
			jpost.setUser(userService.findById(jPostPojo.getIdUser()));
			valIdNull(jpost);
			valBkNotNull(jpost);
			valBkNotExist(jpost);
			valNonBk(jpost);
			jpost.setActiveState(true);
			jobPostingService.insert(jpost);
			JobPosting jobPosting = jobPostingService.findByBk(jpost.getUser().getId(), jpost.getJobPosition().getId(), jpost.getCity().getId(), jpost.getStartDate(), jpost.getEndDate(), jpost.getCompany());
			JobQuota jquota = new JobQuota();
			jquota.setJobPosting(jobPosting);
			jquota.setJobQuota(jPostPojo.getQuota());
			jobQuotaService.insert(jquota);
			jpost.setUser(null);
			return ResponseEntity.status(HttpStatus.CREATED).body(jobPosting);
		}catch(Exception e) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
		}
		
	}
	
	@PutMapping("/admin/job-posting")
	public ResponseEntity<?> update(@RequestBody JobPosting jpost) throws ErrorException{
		try {
			valIdNotNull(jpost);
			valIdExist(jpost.getId());
			valBkNotNull(jpost);
			valBkNotChange(jpost);
			valNonBk(jpost);
			jobPostingService.update(jpost);
			jpost.setUser(null);
		}catch(Exception e) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
		}
		return ResponseEntity.status(HttpStatus.OK).body(jpost);
	}
	
	@PutMapping("/admin/job-posting/close")
	public ResponseEntity<?> closeJob(@RequestBody JobPosting jpost) throws ErrorException{
		try {
			valIdExist(jpost.getId());
			jpost.setActiveState(false);
			jobPostingService.update(jpost);
			jpost.setUser(null);
			return ResponseEntity.status(HttpStatus.OK).body(jpost);
		}catch(Exception e) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
		}
	}
	
	@DeleteMapping("/admin/job-posting/{id}")
	public ResponseEntity<?> delete(@PathVariable String id) throws ErrorException {
		try {
			valIdExist(id);
			JobPosting jpost = jobPostingService.findById(id);
			jobPostingService.delete(jpost);
			jpost.setUser(null);
			return ResponseEntity.ok(jpost);
		}catch(Exception e) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
		}
		
	}
	
	@GetMapping("/apl/job-posting/id/{id}")
	public ResponseEntity<?> getById(@PathVariable String id) throws ErrorException {
		try {
			valIdExist(id);
			JobPosting jpost = jobPostingService.findById(id);
			jpost.getUser().setImage(null);
			return ResponseEntity.ok(jpost);
		}catch(Exception e) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
		}
		
	}
	
	@GetMapping("/admin/job-posting/list/{id}")
	public ResponseEntity<?> getJobByRecruiter(@PathVariable String id) throws ErrorException {
		try {
			List<JobPosting> jposts = jobPostingService.findJobByRecruiter(id);
			for(JobPosting jpost : jposts) {
				jpost.getUser().setImage(null);
			}
			return ResponseEntity.ok(jposts);
		}catch(Exception e) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
		}
	}
	
	@GetMapping("/apl/job-posting")
	public ResponseEntity<?> getAll()  throws ErrorException{
		try {
			List<JobPosting> jposts = jobPostingService.findAll();
			for(JobPosting jpost : jposts) {
				jpost.getUser().setImage(null);
			}
			return ResponseEntity.ok(jposts);	
		}catch(Exception e) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
		}
	}
	
	@GetMapping("/apl/job-recomedation/{jobCategory}/{salary}")
	public ResponseEntity<?> getRecomendationJob(@PathVariable String jobCategory, @PathVariable Double salary) throws ErrorException {
		try {
			List<JobPosting> jposts = jobPostingService.recomendationJob(jobCategory, salary);
			for(JobPosting jpost : jposts) {
				jpost.getUser().setImage(null);
			}
			return ResponseEntity.ok(jposts);
		}catch(Exception e) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
		}
	}
	
	@PostMapping("/apl/job-posting/filter")
	public ResponseEntity<?> filterJob(@RequestBody FilterJob filter) throws ErrorException {
		try {
			List<JobPosting> jposts = jobPostingService.filterJob(filter.getProvinceName(), filter.getJobCategoryName(),filter.getSalaryMin(),filter.getSalaryMax());
			for(JobPosting jpost : jposts) {
				jpost.getUser().setImage(null);
			}
			return ResponseEntity.status(HttpStatus.OK).body(jposts);
		}catch(Exception e) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
		}
	}
	
	private Exception valIdNull(JobPosting jpost) throws Exception {
		if(jpost.getId()!=null) {
			throw new Exception("Insert Failed");
		}
		return null;
	}
	
	private Exception valIdNotNull(JobPosting jpost) throws Exception{
		if(jpost.getId()==null) {
			throw new Exception("Job Posting doesn't exist");
		}
		return null;
	}
	
	private Exception valIdExist(String id) throws Exception{
		if(jobPostingService.findById(id)==null) {
			throw new Exception("Job Posting doesn't exists");
		}
		return null;
	}
	
	private Exception valBkNotNull(JobPosting jpost) throws Exception{
		if(jpost.getUser()==null) {
			throw new Exception("User must be filled");
		}else if(jpost.getJobPosition()==null) {
			throw new Exception("Job Position must be filled");
		}else if(jpost.getCity()==null) {
			throw new Exception("City must be filled");
		}else if(jpost.getStartDate()==null) {
			throw new Exception("Start date must be filled");
		}else if(jpost.getEndDate()==null) {
			throw new Exception("End date must be filled");
		}else if(jpost.getCompany()==null) {
			throw new Exception("Company must be filled");
		}
		return null;
	}
	
	private Exception valBkNotExist (JobPosting jpost) throws Exception{
		if(jobPostingService.findByBk(jpost.getUser().getId(),jpost.getJobPosition().getId(),jpost.getCity().getId(), jpost.getStartDate(), jpost.getEndDate(), jpost.getCompany())!=null) {
			throw new Exception("Job Posting already exists");
		}else if(userService.findById(jpost.getUser().getId())==null) {
			throw new Exception("User doesn't exist");
		}else if(jobPositionService.findById(jpost.getJobPosition().getId())==null || jobPositionService.findById(jpost.getJobPosition().getId()).isActiveState()==false) {
			throw new Exception("Job position doesn't exist");
		}else if(cityService.findById(jpost.getCity().getId())==null || cityService.findById(jpost.getCity().getId()).isActiveState()==false) {
			throw new Exception("City doesn't exist");
		}
		return null;
	}
	
	private Exception valBkNotChange(JobPosting jpost) throws Exception{
		if(!jpost.getUser().getId().equalsIgnoreCase(userService.findById(jpost.getUser().getId()).getId())) {
			throw new Exception("BK cannot change");
		}else if(!jpost.getJobPosition().getId().equalsIgnoreCase(jobPositionService.findById(jpost.getJobPosition().getId()).getId())) {
			throw new Exception("BK cannot change");
		}else if(!jpost.getCity().getId().equalsIgnoreCase(cityService.findById(jpost.getCity().getId()).getId())) {
			throw new Exception("BK cannot change");
		}else if(!jpost.getStartDate().equals(jobPostingService.findById(jpost.getId()).getStartDate())) {
			throw new Exception("BK cannot change");
		}else if(!jpost.getCompany().equalsIgnoreCase(jobPostingService.findById(jpost.getId()).getCompany())) {
			throw new Exception("BK cannot change");
		}
		return null;
	}
	
	private Exception valNonBk(JobPosting jpost) throws Exception {
		if(jpost.getJobTitleName() == null) {
			throw new Exception("Job Posting name must be filled");
		}else if(jpost.getAddress()==null) {
			throw new Exception("Address must be filled");
		}
		return null;
	}
}

