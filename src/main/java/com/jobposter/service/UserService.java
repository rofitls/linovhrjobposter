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
		Users user = findByUsername(email);
		UserPassword up = userPasswordDao.findById(user.getId());
		
		return new User(user.getUsername(), up.getPassword(),
				new ArrayList<>());
	}
	
	public String exportReport(String id, List<ReportPojo> rp) throws ErrorException, FileNotFoundException, JRException {
		String path = "E:\\Report";
        //load file and compile it
        File file = ResourceUtils.getFile("classpath:master.jrxml");
        JasperReport jasperReport = JasperCompileManager.compileReport(file.getAbsolutePath());
        JRBeanCollectionDataSource dataSource = new JRBeanCollectionDataSource(rp);
        Map<String, Object> parameters = new HashMap<>();
        parameters.put("createdBy", "Java Techie");
        JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, parameters, dataSource);
        JasperExportManager.exportReportToPdfFile(jasperPrint, path + "\\report.pdf");
        return "report generated in path : " + path;
	}
}
