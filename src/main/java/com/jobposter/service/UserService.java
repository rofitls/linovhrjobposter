package com.jobposter.service;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.ResourceUtils;

import com.jobposter.dao.ApplicationStateChangeDao;
import com.jobposter.dao.UserDao;
import com.jobposter.dao.UserPasswordDao;
import com.jobposter.entity.ReportInput;
import com.jobposter.entity.ReportPerJobPojo;
import com.jobposter.entity.ReportPojo;
import com.jobposter.entity.UserPassword;
import com.jobposter.entity.Users;
import com.jobposter.exception.ErrorException;
import com.lowagie.text.pdf.PdfObject;

import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import net.sf.jasperreports.engine.data.JRMapArrayDataSource;

@Service("userService")
public class UserService implements UserDetailsService {

	@Autowired
	private UserDao userDao;
	
	@Autowired
	private UserPasswordDao userPasswordDao;
	
//	@Autowired
//	private PasswordEncoder bcryptEncoder;

	public Users findById(String id) {
		Users user = userDao.findById(id);
		return user;
	}
	
	public void insert(Users user) throws ErrorException{
//		user.setPassword(bcryptEncoder.encode(user.getPassword()));
		userDao.save(user);
	}
	
	public void update(Users user) throws ErrorException{
		userDao.save(user);
	}
	
	public void delete(Users user) throws ErrorException{
		userDao.delete(user);
	}
	
	public Users findByBk(String username, String phone)  throws ErrorException {
		Users user = userDao.findByBk(username,phone);
		return user;
	}
	
	public Users findByUsername(String username) {
		Users user = userDao.findByUsername(username);
		return user;
	}
	
	public List<Users> findAll()  throws ErrorException {
		List<Users> user = userDao.findAll();
		return user;
	}

	@Override
	public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
		// TODO Auto-generated method stub
//		Users user = userDao.findByUsername(email);
		Users user = findByUsername(email);
		UserPassword up = userPasswordDao.findById(user.getId());
		
//		if(user == null) {
//			throw new UsernameNotFoundException("User not found with email: " + email);
//		}
		
		return new User(user.getUsername(), up.getPassword(),
				new ArrayList<>());
	}
	
	public String exportReport(String id, ReportPojo rp) throws ErrorException, FileNotFoundException, JRException {
//		 String path = "E:\\Report";
//		 File file = ResourceUtils.getFile("classpath:report_per_month.jrxml");
//		 JasperReport jasperReport = JasperCompileManager.compileReport(file.getAbsolutePath());
//	     List<ReportPojo> listRp = new ArrayList<ReportPojo>();
//	     listRp.add(rp);
//	     JRBeanCollectionDataSource dataSource = new JRBeanCollectionDataSource(listRp, false);
//	     Map<String, Object> parameters = new HashMap<>();
//	     parameters.put("createdBy", "Java Techie");
//	     JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, parameters, dataSource);
//	     JasperExportManager.exportReportToPdfFile(jasperPrint, "\\report_per_month.pdf");
//	     JasperExportManager.exportReportToPdf(jasperPrint);
//	     return "report generated in path : " + path;
		
		 String path = "E:\\Report";
		 File file = ResourceUtils.getFile("classpath:report.jrxml");
		 JasperReport jasperReport = JasperCompileManager.compileReport(file.getAbsolutePath());
		 ReportInput reportInput = new ReportInput();
		 reportInput.setInstituteName("PT Lawencon International");
		 List<ReportPojo> listRp = new ArrayList<>();
		 listRp.add(rp);
		 JRBeanCollectionDataSource studentDataSource = new JRBeanCollectionDataSource(listRp, false);
		 reportInput.setStudentDataSource(studentDataSource);
		 
		 for(ReportPojo rpp : listRp) {
			 System.out.println(rpp.getRecruiterName());
			 System.out.println(rpp.getTotalUploadJob());
			 for(ReportPerJobPojo rpjp : rpp.getJobList()) {
				 System.out.println(rpjp.getCountApplicant());
			 }
		 }
		 //byte[] reportData = null;
	       try {
	           JRMapArrayDataSource dataSource = new JRMapArrayDataSource(new Object[]{reportInput.getDataSources()});
	 
	           JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport,
	                   reportInput.getParameters(), dataSource);
	 
	           JasperExportManager.exportReportToPdfFile(jasperPrint, path + "\\report.pdf");
	       } catch (JRException e) {
	           e.printStackTrace();
	       } catch (Exception e) {
	           e.printStackTrace();
	       }
	    return "report generated in path : " + path;
	}
}
