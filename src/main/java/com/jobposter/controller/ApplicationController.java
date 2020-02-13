package com.jobposter.controller;


import java.sql.Time;
import java.text.DateFormat;
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

import com.google.api.client.auth.oauth2.AuthorizationCodeRequestUrl;
import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.auth.oauth2.TokenResponse;
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeFlow;
import com.google.api.client.googleapis.auth.oauth2.GoogleClientSecrets;
import com.google.api.client.googleapis.auth.oauth2.GoogleClientSecrets.Details;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.client.util.DateTime;
import com.google.api.services.calendar.Calendar.Events;
import com.google.api.services.calendar.CalendarScopes;
import com.google.api.services.calendar.model.Event;


@RestController
//@RequestMapping("")
@CrossOrigin("*")
public class ApplicationController {
	
//	private static final String APPLICATION_NAME = "GoogleCalendarJobPoster";
//	private static HttpTransport httpTransport;
//	private static final JsonFactory JSON_FACTORY = JacksonFactory.getDefaultInstance();
//	private static com.google.api.services.calendar.Calendar client;
//
//	GoogleClientSecrets clientSecrets;
//	GoogleAuthorizationCodeFlow flow;
//	Credential credential;
//
//	@Value("${google.client.client-id}")
//	private String clientId;
//	@Value("${google.client.client-secret}")
//	private String clientSecret;
//	@Value("${google.client.redirectUri}")
//	private String redirectURI;
//
//	private Set<Event> events = new HashSet<>();
//
//	final DateTime date1 = new DateTime("2017-05-05T16:30:00.000+05:30");
//	final DateTime date2 = new DateTime(new Date());
	
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
			appl.setDateOfApplication(new Date());
			ApplicationStateChange state = new ApplicationStateChange();
			state.setDateChanged(new Date());
			state.setState(applStateService.findByStateName("Not Viewed"));
			applService.insert(appl);
			Application application = applService.findByBk(appl.getUser().getId(), appl.getJobPosting().getId());
			state.setApplication(application);
			applStateChangeService.insert(state);
			appl.setUser(null);
		}catch(Exception e) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
		}
		return ResponseEntity.status(HttpStatus.CREATED).body(appl);
	}
	
//	public ResponseEntity<?> sendCalendar() throws ErrorException {
//		com.google.api.services.calendar.model.Events eventList;
//		String message;
//		try {
//			TokenResponse response = flow.newTokenRequest(code).setRedirectUri(redirectURI).execute();
//			credential = flow.createAndStoreCredential(response, "userID");
//			client = new com.google.api.services.calendar.Calendar.Builder(httpTransport, JSON_FACTORY, credential)
//					.setApplicationName(APPLICATION_NAME).build();
//			Events events = client.events();
//			eventList = events.list("primary").setTimeMin(date1).setTimeMax(date2).execute();
//			message = eventList.getItems().toString();
//			System.out.println("My:" + eventList.getItems());
//		} catch (Exception e) {
//			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
//		}
//
//		System.out.println("cal message:" + message);
//		return new ResponseEntity<>(message, HttpStatus.OK);
//	}
//	
	@PutMapping("/apl/application")
	public ResponseEntity<?> update(@RequestBody Application appl) throws ErrorException{
		try {
			valIdNotNull(appl);
			valIdExist(appl.getId());
			valBkNotNull(appl);
			valBkNotChange(appl);
			//valNonBk(appl);
			applService.update(appl);
			appl.setUser(null);
		}catch(Exception e) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
		}
		return ResponseEntity.status(HttpStatus.OK).body(appl);
	}
	
	@DeleteMapping("/apl/application/{id}")
	public ResponseEntity<?> delete(@PathVariable String id) throws ErrorException {
		try {
			valIdExist(id);
			Application appl = applService.findById(id);
			applService.delete(appl);
			appl.setUser(null);
			return ResponseEntity.ok(appl);
		}catch(Exception e) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
		}	
	}
	
	@PutMapping("/admin/application/interview/{id}")
	public ResponseEntity<?> interviewApplicant(@PathVariable String id, @RequestBody InterviewTestSchedule schedule) throws ErrorException {
		try {
			valIdExist(id);
			
			DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
			DateFormat timeFormat = new SimpleDateFormat("HH:mm");
		    String strDate = dateFormat.format(schedule.getInterviewDate());
			String strTime = timeFormat.format(schedule.getInterviewTime());
		    
			Mail mail = new Mail();
			Application appl = applService.findById(id);
			ApplicationStateChange applStateChange = applStateChangeService.findByBk(appl.getId());
			
			valInterview(schedule);
			
			applStateChange.setState(applStateService.findByStateName("Interview"));
			applStateChange.setDateChanged(new Date());
			mail.setName(appl.getUser().getFirstName()+" "+appl.getUser().getLastName());
		    mail.setSubject("Interview invitation " + appl.getJobPosting().getCompany()); 
		    mail.setTo(appl.getUser().getUsername());
		    mail.setPosition(appl.getJobPosting().getJobTitleName());
		    mail.setDate(strDate);
		    mail.setTime(strTime);
		    mail.setAddress(schedule.getInterviewLocation());
		    
		    schedule.setApplication(appl);
		    schedule.setInterviewCode("SCHEDULE-"+id);
		    schedule.setInterviewDate(schedule.getInterviewDate());
		    schedule.setInterviewTime(schedule.getInterviewTime());
		    schedule.setActiveState(true);
		    interviewTestScheduleService.insert(schedule);
			applStateChangeService.update(applStateChange);
			emailService.sendInterview(mail);
			applStateChange.getApplication().setUser(null);
			return ResponseEntity.status(HttpStatus.OK).body(applStateChange);
		}catch(Exception e) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
		}
	}
	
	@PutMapping("/admin/application/interview-reschedule")
	public ResponseEntity<?> interviewRescheduleApplicant(@RequestBody InterviewTestSchedule schedule) throws ErrorException {
		try {
			
			DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
			DateFormat timeFormat = new SimpleDateFormat("HH:mm");
			
		    String strDate = dateFormat.format(schedule.getInterviewDate());
			String strTime = timeFormat.format(schedule.getInterviewTime());
		    
			Mail mail = new Mail();
			ApplicationStateChange applStateChange = applStateChangeService.findByBk(schedule.getApplication().getId());
			
			valInterview(schedule);
			
			applStateChange.setState(applStateService.findByStateName("Interview"));
			applStateChange.setDateChanged(new Date());
			
			mail.setName(schedule.getApplication().getUser().getFirstName()+" "+schedule.getApplication().getUser().getLastName());
		    mail.setSubject("Interview Invitation " + schedule.getApplication().getJobPosting().getCompany()); 
		    mail.setTo(schedule.getApplication().getUser().getUsername());
		    mail.setPosition(schedule.getApplication().getJobPosting().getJobTitleName());
		    mail.setDate(strDate);
		    mail.setTime(strTime);
		    mail.setAddress(schedule.getInterviewLocation());
		    
		    schedule.setApplication(schedule.getApplication());
		    schedule.setInterviewDate(schedule.getInterviewDate());
		    schedule.setInterviewTime(schedule.getInterviewTime());
		    schedule.setInterviewLocation(schedule.getInterviewLocation());
		    
		    schedule.setReschedule(false);
		    schedule.setAttend(false);
		    schedule.setReject(false);
		    interviewTestScheduleService.update(schedule);
			applStateChangeService.update(applStateChange);
			emailService.sendInterview(mail);
			applStateChange.getApplication().setUser(null);
			return ResponseEntity.status(HttpStatus.OK).body(applStateChange);
		}catch(Exception e) {
			e.printStackTrace();
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
		}
	}
	
	@PutMapping("/apl/application/attend-interview")
	public ResponseEntity<?> attendInterview(@RequestBody InterviewTestSchedule schedule) throws ErrorException {
		try {
			
			Mail mail = new Mail();
			
			valInterview(schedule);
			
			schedule.setAttend(true);
			schedule.setReschedule(false);
			schedule.setReject(false);
			
			mail.setName(schedule.getApplication().getJobPosting().getUser().getFirstName()+" "+schedule.getApplication().getJobPosting().getUser().getLastName());
		    mail.setSubject("Attending Interview Invitation " + schedule.getApplication().getJobPosting().getCompany()); 
		    mail.setTo(schedule.getApplication().getJobPosting().getUser().getUsername());
		    mail.setPosition(schedule.getApplication().getJobPosting().getJobTitleName());
		    mail.setReasonRejected(schedule.getApplication().getUser().getFirstName()+" "+schedule.getApplication().getUser().getLastName());
		    
		    
		    interviewTestScheduleService.update(schedule);
			emailService.sendAttend(mail);
			return ResponseEntity.status(HttpStatus.OK).body(schedule);
		}catch(Exception e) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
		}
	}
	
	
	@PutMapping("/apl/application/applicant-request-reschedule")
	public ResponseEntity<?> applicantRequestRescheduleInterview(@RequestBody InterviewTestSchedule schedule) throws ErrorException {
		try {
			Mail mail = new Mail();
			DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
			DateFormat timeFormat = new SimpleDateFormat("HH:mm");
		    String strDate = dateFormat.format(schedule.getInterviewDate());
			String strTime = timeFormat.format(schedule.getInterviewTime());

			valRescheduleInterview(schedule);
			
			schedule.setReschedule(true);
			interviewTestScheduleService.update(schedule);
			
			mail.setName(schedule.getApplication().getJobPosting().getUser().getFirstName() + " " + schedule.getApplication().getJobPosting().getUser().getLastName());
			mail.setSubject("Reschedule Request for " + schedule.getApplication().getJobPosting().getJobTitleName() + " Position at " + schedule.getApplication().getJobPosting().getCompany());
			mail.setTo(schedule.getApplication().getJobPosting().getUser().getUsername());
			mail.setPosition(schedule.getApplication().getJobPosting().getJobTitleName());
			mail.setReasonReschedule(schedule.getRescheduleReason());
			mail.setDate(strDate);
			mail.setTime(strTime);
			mail.setAddress(schedule.getInterviewLocation());
			mail.setReasonRejected(schedule.getApplication().getUser().getFirstName() + " "+schedule.getApplication().getUser().getLastName());
			
			emailService.sendReschedule(mail);
			
			return ResponseEntity.ok(schedule);
		}catch(Exception e) {
			e.printStackTrace();
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
		}
	}
	
	@PutMapping("/admin/application/hire/{date}/{time}/{location}")
	public ResponseEntity<?> hireApplicant(@RequestBody Application appl, @PathVariable String date, @PathVariable String time, @PathVariable String location) throws ErrorException {
		try {
			valIdExist(appl.getId());
			
			Mail mail = new Mail();
			
			mail.setName(appl.getUser().getFirstName() + " " + appl.getUser().getLastName());
			mail.setTo(appl.getUser().getUsername());
			mail.setSubject("Taken Contract and Hiring for " + appl.getJobPosting().getJobTitleName() + " Position at " + appl.getJobPosting().getCompany());
			mail.setPosition(appl.getJobPosting().getJobTitleName());
			mail.setDate(date);
			mail.setTime(time);
			mail.setAddress(location);
			
			ApplicationStateChange applStateChange = applStateChangeService.findByBk(appl.getId());
			applStateChange.setState(applStateService.findByStateName("Hire"));
			applStateChange.setDateChanged(new Date());
			applStateChange.setApplication(appl);
			
			applStateChangeService.update(applStateChange);
			
			Long appHire = applStateChangeService.findApplicationHire(appl.getJobPosting().getId());
			if(jobQuotaService.findJobQuota(appl.getJobPosting().getId()) == appHire.intValue()) {
				JobPosting jpost = jobPostingService.findById(appl.getJobPosting().getId());
				jpost.setActiveState(false);
				jobPostingService.update(jpost);
			}
			
			emailService.sendHire(mail);
			
			applStateChange.getApplication().setUser(null);
			return ResponseEntity.status(HttpStatus.OK).body(applStateChange);
		}catch(Exception e) {
			e.printStackTrace();
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
		}
	}
	
	@PutMapping("/apl/application/applicant-reject/{reason}")
	public ResponseEntity<?> applicantRejectApplication(@RequestBody Application appl, @PathVariable String reason) throws ErrorException {
		try {
			valIdExist(appl.getId());
			
			Mail mail = new Mail();
			
			mail.setName(appl.getJobPosting().getUser().getFirstName() + " " + appl.getJobPosting().getUser().getLastName());
			mail.setAddress(appl.getUser().getFirstName() + " " + appl.getUser().getLastName());
			mail.setTo(appl.getJobPosting().getUser().getUsername());
			mail.setPosition(appl.getJobPosting().getJobTitleName());
			mail.setReasonRejected(reason);
			mail.setSubject("Reject Application for " + appl.getJobPosting().getJobTitleName() + " Position at " + appl.getJobPosting().getCompany());
			
			InterviewTestSchedule its = interviewTestScheduleService.findScheduleByApplication(appl.getId());
			its.setAttend(false);
			its.setReschedule(false);
			its.setReject(true);
		    its.setActiveState(false);
			interviewTestScheduleService.update(its);
			
			ApplicationStateChange applStateChange = applStateChangeService.findByBk(appl.getId());
			
			applStateChange.setState(applStateService.findByStateName("Reject"));
			applStateChange.setDateChanged(new Date());
			applStateChange.setApplication(appl);
			
			applStateChangeService.update(applStateChange);
			
			emailService.sendRejectByApplicant(mail);
			return ResponseEntity.status(HttpStatus.OK).body(appl);
		}catch(Exception e) {
			e.printStackTrace();
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
		}
	}
	
	@PutMapping("/admin/application/reject")
	public ResponseEntity<?> rejectApplicant(@RequestBody InterviewTestSchedule schedule) throws ErrorException {
		try {
			
			Mail mail = new Mail();
			
			mail.setName(schedule.getApplication().getUser().getFirstName() + " " + schedule.getApplication().getUser().getLastName());
			//mail.setAddress(appl.getUser().getFirstName() + " " + appl.getUser().getLastName());
			mail.setTo(schedule.getApplication().getUser().getUsername());
			mail.setPosition(schedule.getApplication().getJobPosting().getJobTitleName());
			mail.setReasonRejected(schedule.getInterviewResult());
			mail.setSubject("Reject Application for " + schedule.getApplication().getJobPosting().getJobTitleName() + " Position at " + schedule.getApplication().getJobPosting().getCompany());
			
			
			valIdExist(schedule.getApplication().getId());
			
			ApplicationStateChange applStateChange = applStateChangeService.findByBk(schedule.getApplication().getId());
			
			applStateChange.setState(applStateService.findByStateName("Reject"));
			applStateChange.setDateChanged(new Date());
			applStateChange.setApplication(schedule.getApplication());
			
			applStateChangeService.update(applStateChange);
			
//			if(its == null) {
//				applStateChange.getApplication().setUser(null);
//				return ResponseEntity.status(HttpStatus.OK).body(applStateChange);	
//			}
//			
//			
			schedule.setReject(true);
			schedule.setAttend(false);
			schedule.setReschedule(false);
		    schedule.setActiveState(false);
			interviewTestScheduleService.update(schedule);
			
			emailService.sendReject(mail);
			applStateChange.getApplication().setUser(null);
			return ResponseEntity.status(HttpStatus.OK).body(applStateChange);

		}catch(Exception e) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
		}
	}
	
	@GetMapping("/apl/application/id/{id}")
	public ResponseEntity<?> getById(@PathVariable String id) throws ErrorException {
		try {
			valIdExist(id);
			Application appl = applService.findById(id);
			appl.getUser().setImage(null);
			return ResponseEntity.ok(appl);	
		}catch(Exception e) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
		}
		
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
			applStateChange.getApplication().setUser(null);
			return ResponseEntity.status(HttpStatus.OK).body(applStateChange);
		}catch(Exception e) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
		}
	}
	
	@GetMapping("/admin/application")
	public ResponseEntity<?> getAll()  throws ErrorException{
		try {
			return ResponseEntity.ok(applService.findAll());	
		}catch(Exception e) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
		}
		
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
			valIdExist(id);
			InterviewTestSchedule its = interviewTestScheduleService.findScheduleByApplication(id);
			its.getApplication().setUser(null);
			return ResponseEntity.status(HttpStatus.OK).body(its);
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
	
	private Exception valRescheduleInterview(InterviewTestSchedule schedule) throws Exception {
		if(schedule.getRescheduleReason()==null || schedule.getRescheduleReason().equalsIgnoreCase("")) {
			throw new Exception("Reason must be field");
		}
		return null;
	}
	
	private Exception valRejectApplication(InterviewTestSchedule schedule) throws Exception {
		if(schedule.getRejectReason()==null || schedule.getRejectReason().equalsIgnoreCase("")) {
			throw new Exception("Reason must be field");
		}
		return null;
	}
	
	private Exception valInterview(InterviewTestSchedule schedule) throws Exception {
		if(schedule.getInterviewDate()==null) {
			throw new Exception("Interview date must be filled");
		}else if(schedule.getInterviewTime()==null) {
			throw new Exception("Interview time must be filled");
		}else if(schedule.getInterviewLocation()==null) {
			throw new Exception("Interview location must be filled");
		}
		return null;
	}
}


