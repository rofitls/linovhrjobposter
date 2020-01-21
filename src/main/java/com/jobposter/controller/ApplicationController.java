package com.jobposter.controller;

import java.text.SimpleDateFormat;
import java.util.Date;

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

import com.jobposter.entity.Application;
import com.jobposter.entity.ApplicationStateChange;
import com.jobposter.entity.InterviewTestSchedule;
import com.jobposter.entity.JobPosting;
import com.jobposter.entity.Mail;
import com.jobposter.exception.ErrorException;
import com.jobposter.service.ApplicationService;
import com.jobposter.service.ApplicationStateChangeService;
import com.jobposter.service.ApplicationStateService;
import com.jobposter.service.DocumentService;
import com.jobposter.service.DocumentTypeService;
import com.jobposter.service.EmailService;
import com.jobposter.service.InterviewTestScheduleService;
import com.jobposter.service.JobPostingService;
import com.jobposter.service.JobQuotaService;
import com.jobposter.service.UserService;
import com.jobposter.service.ApplicantEducationService;
import com.jobposter.service.ApplicantSkillService;
import com.jobposter.service.ApplicantWorkExperienceService;


@RestController
//@RequestMapping("")
@CrossOrigin("*")
public class ApplicationController {
	
	@Autowired
	private ApplicationService applService;
	
	@Autowired
	private ApplicationStateChangeService applStateChangeService;
	
	@Autowired
	private ApplicationStateService applStateService;
	
	@Autowired
	private UserService userService;
	
	@Autowired
	private JobPostingService jobPostingService;
	
	@Autowired
	private DocumentService documentService;
	
	@Autowired
	private ApplicantWorkExperienceService applWorkExpService;
	
	@Autowired
	private ApplicantEducationService applEduService;
	
	@Autowired
	private ApplicantSkillService applSkillService;
	
	@Autowired
	private DocumentTypeService docTypeService;
	
	@Autowired
	private DocumentService docService;
	
	@Autowired
	private EmailService emailService;
	
	@Autowired
	private InterviewTestScheduleService interviewTestScheduleService;
	
	@Autowired
	private JobQuotaService jobQuotaService;
	
	@PostMapping("/apl/application")
	public ResponseEntity<?> insert(@RequestBody Application appl) throws ErrorException{
		try {
			valIdNull(appl);
			valBkNotNull(appl);
			valBkNotExist(appl);
			authenticateAppl(appl);
			//valNonBk(appl);
			ApplicationStateChange state = new ApplicationStateChange();
			state.setDateChanged(new Date());
			state.setState(applStateService.findByStateName("Not Viewed"));
			applService.insert(appl);
			Application application = applService.findByBk(appl.getUser().getId(), appl.getJobPosting().getId());
			state.setApplication(application);
			applStateChangeService.insert(state);
		}catch(Exception e) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
		}
		return ResponseEntity.status(HttpStatus.CREATED).body("Model Berhasil Ditambah");
	}
	
	@PutMapping("/apl/application")
	public ResponseEntity<?> update(@RequestBody Application appl) throws ErrorException{
		try {
			valIdNotNull(appl);
			valIdExist(appl.getId());
			valBkNotNull(appl);
			valBkNotChange(appl);
			//valNonBk(appl);
			applService.update(appl);
		}catch(Exception e) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
		}
		return ResponseEntity.status(HttpStatus.OK).body("Model Berhasil Diperbarui");
	}
	
	@DeleteMapping("/apl/application/{id}")
	public ResponseEntity<?> delete(@PathVariable String id) throws ErrorException {
		try {
			valIdExist(id);
			applService.delete(id);
		}catch(Exception e) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
		}
		return ResponseEntity.status(HttpStatus.OK).body("Model Berhasil Dihapus");
	}
	
	@PutMapping("/admin/application/interview/{id}/{date}")
	public ResponseEntity<?> interviewApplicant(@PathVariable String id, @PathVariable String date) throws ErrorException {
		try {
			valIdExist(id);
			Mail mail = new Mail();
			InterviewTestSchedule schedule = new InterviewTestSchedule();
			Date interviewDate = new SimpleDateFormat("yyyy-MM-dd").parse(date);
			Application appl = applService.findById(id);
			ApplicationStateChange applStateChange = new ApplicationStateChange();
			applStateChange.setState(applStateService.findByStateName("Interview"));
			applStateChange.setApplication(appl);
			applStateChange.setDateChanged(new Date());
			mail.setName(appl.getUser().getFirstName()+" "+appl.getUser().getLastName());
		    mail.setSubject("Interview invitation " + appl.getJobPosting().getUser().getFirstName() + " " + appl.getJobPosting().getUser().getLastName()); 
		    mail.setContent(date);
		    mail.setTo(appl.getUser().getUsername());
		    schedule.setApplication(appl);
		    Integer count = interviewTestScheduleService.countSchedule().intValue();
		    schedule.setInterviewCode("SCHEDULE-"+count);
		    schedule.setInterviewDate(interviewDate);
		    alreadySchedule(appl);
		    interviewTestScheduleService.insert(schedule);
			applStateChangeService.insert(applStateChange);
			emailService.sendEmail(mail);
			return ResponseEntity.status(HttpStatus.OK).body("Status changed");
		}catch(Exception e) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
		}
	}
	
	@PutMapping("/admin/application/hire/{id}")
	public ResponseEntity<?> hireApplicant(@PathVariable String id) throws ErrorException {
		try {
			valIdExist(id);
			Application appl = applService.findById(id);
			ApplicationStateChange applStateChange = new ApplicationStateChange();
			applStateChange.setState(applStateService.findByStateName("Hire"));
			applStateChange.setApplication(appl);
			applStateChange.setDateChanged(new Date());
			applStateChangeService.insert(applStateChange);
			Long appHire = applStateChangeService.findApplicationHire(appl.getJobPosting().getId());
			if(jobQuotaService.findJobQuota(appl.getJobPosting().getId()) == appHire.intValue()) {
				JobPosting jpost = jobPostingService.findById(appl.getJobPosting().getId());
				jpost.setActiveState(false);
			}
			InterviewTestSchedule its = interviewTestScheduleService.findScheduleByApplication(appl.getId());
			interviewTestScheduleService.delete(its.getId());
			return ResponseEntity.status(HttpStatus.OK).body("Status changed");
		}catch(Exception e) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
		}
	}
	
	@PutMapping("/admin/application/reject/{id}")
	public ResponseEntity<?> rejectApplicant(@PathVariable String id) throws ErrorException {
		try {
			valIdExist(id);
			Application appl = applService.findById(id);
			ApplicationStateChange applStateChange = new ApplicationStateChange();
			applStateChange.setState(applStateService.findByStateName("Reject"));
			applStateChange.setApplication(appl);
			applStateChange.setDateChanged(new Date());
			applStateChangeService.insert(applStateChange);
			InterviewTestSchedule its = interviewTestScheduleService.findScheduleByApplication(appl.getId());
			interviewTestScheduleService.delete(its.getId());
			return ResponseEntity.status(HttpStatus.OK).body("Status changed");
		}catch(Exception e) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
		}
	}
	
	@GetMapping("/apl/application/id/{id}")
	public ResponseEntity<?> getById(@PathVariable String id) throws ErrorException {
		return ResponseEntity.ok(applService.findById(id));
	}
	
	@GetMapping("/admin/application/detail/{id}")
	public ResponseEntity<?> detailApplicationApplicant(@PathVariable String id) throws ErrorException {
		try {
			valIdExist(id);
			Application appl = applService.findById(id);
			ApplicationStateChange applStateChange = applStateChangeService.findByBk(appl.getId());
			if(applStateChangeService.findByApplicantNotViewed(appl.getId())!=null) {
				applStateChange.setState(applStateService.findByStateName("Viewed"));
				applStateChangeService.update(applStateChange);
			}
			applStateChange = applStateChangeService.findByBk(appl.getId());
			return ResponseEntity.status(HttpStatus.OK).body(applStateChange);
		}catch(Exception e) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
		}
	}
	
	@GetMapping("/admin/application")
	public ResponseEntity<?> getAll()  throws ErrorException{
		return ResponseEntity.ok(applService.findAll());
	}
	
	@GetMapping("/admin/application/count/{id}")
	public ResponseEntity<?> countApplicationByJobPosting(@PathVariable String id) throws ErrorException {
		try {
			return ResponseEntity.ok(applService.countApplicationByJobPosting(id));	
		}catch(Exception e) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
		}
		
	}
	
	@GetMapping("/apl/application/schedule/{id}")
	public ResponseEntity<?> findApplicationInterview(@PathVariable String id) throws ErrorException {
		try {
			return ResponseEntity.status(HttpStatus.OK).body(interviewTestScheduleService.findScheduleByApplication(id));
		}catch(Exception e) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
		}
	}
	

	private Exception valIdNull(Application appl) throws Exception {
		if(appl.getId()!=null) {
			throw new Exception("Insert Failed");
		}
		return null;
	}
	
	private Exception valIdNotNull(Application appl) throws Exception{
		if(appl.getId()==null) {
			throw new Exception("Application doesn't exist");
		}
		return null;
	}
	
	private Exception valIdExist(String id) throws Exception{
		if(applService.findById(id)==null) {
			throw new Exception("Application doesn't exists");
		}
		return null;
	}
	
	private Exception valBkNotNull(Application appl) throws Exception{
		if(appl.getUser()==null) {
			throw new Exception("User must be filled");
		}else if(appl.getJobPosting()==null) {
			throw new Exception("Job Posting must be filled");
		}
		return null;
	}
	
	private Exception valBkNotExist (Application appl) throws Exception{
		if(applService.findByBk(appl.getUser().getId(),appl.getJobPosting().getId())!=null) {
			throw new Exception("Application already exists");
		}else if(userService.findById(appl.getUser().getId())==null) {
			throw new Exception("User doesn't exist");
		}else if(jobPostingService.findById(appl.getJobPosting().getId())==null || jobPostingService.findById(appl.getJobPosting().getId()).isActiveState()==false) {
			throw new Exception("Job Posting doesn't exist");
		}
		return null;
	}
	
	private Exception valBkNotChange(Application appl) throws Exception{
		if(!appl.getUser().getId().equalsIgnoreCase(userService.findById(appl.getUser().getId()).getId())) {
			throw new Exception("BK cannot change");
		}else if(!appl.getJobPosting().getId().equalsIgnoreCase(jobPostingService.findById(appl.getJobPosting().getId()).getId())) {
			throw new Exception("BK cannot change");
		}
		return null;
	}
	
	private Exception authenticateAppl(Application appl) throws Exception {
		if(applWorkExpService.findAWEUser(appl.getUser().getId())==null) {
			throw new Exception("Harus mengisi form work experience terlebih dahulu");
		}else if(applEduService.findAEUser(appl.getUser().getId())==null) {
			throw new Exception("Harus mengisi form education terlebih dahulu");
		}else if(applSkillService.findASUser(appl.getUser().getId())==null) {
			throw new Exception("Harus mengisi form skill terlebih dahulu");
		}else {
			if(documentService.findADUser(appl.getUser().getId())==null) {
				throw new Exception("Harus mengisi dokumen terlebih dahulu");
			}else {
				Long docType = docTypeService.filterDoc();
				Long docUser = docService.filterDoc(appl.getUser().getId());
				if(docUser < docType) {
					throw new Exception("Dokumen belum lengkap");
				}
			}
		}
			
		return null;
	}
	
	private Exception alreadySchedule(Application appl) throws Exception {
		if(interviewTestScheduleService.findScheduleByApplication(appl.getId())!=null) {
			throw new Exception("Schedule already exist");
		}
		return null;
	}
	
//	private Exception valNonBk(Application appl) throws Exception {
//		return null;
//	}
}


