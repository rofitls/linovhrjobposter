package com.jobposter.service;

import java.io.File;
import java.io.FileNotFoundException;
import java.net.MalformedURLException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
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
import com.jobposter.entity.ReportMasterPojo;
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
	
	@Value("${reportdir}")
	private Path reportdir;

	public Users findById(String id) {
		Users user = userDao.findById(id);
		return user;
	}
	
	public void insert(Users user) throws ErrorException{
		userDao.save(user);
	}
	
	public void update(Users user) throws ErrorException{
		userDao.save(user);
	}
	
	public void delete(Users user) throws ErrorException{
		userDao.delete(user);
	}
	
	public Users findByBk(String username)  throws ErrorException {
		Users user = userDao.findByBk(username);
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
	
	public String exportReport(List<ReportMasterPojo> rp) throws ErrorException, FileNotFoundException, JRException {
        //load file and compile it
        File file = ResourceUtils.getFile("classpath:reportjob.jrxml");
        JasperReport jasperReport = JasperCompileManager.compileReport(file.getAbsolutePath());
        JRBeanCollectionDataSource dataSource = new JRBeanCollectionDataSource(rp);
        Map<String, Object> parameters = new HashMap<>();
        parameters.put("createdBy", "Java Techie");
        JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, parameters, dataSource);
        JasperExportManager.exportReportToPdfFile(jasperPrint, reportdir.toString() + "/report.pdf");
        String fileName = "report.pdf";
        return fileName;
	}
	
	public String exportReportPerYear(List<ReportMasterPojo> rp) throws ErrorException, FileNotFoundException, JRException {
        //load file and compile it
        File file = ResourceUtils.getFile("classpath:reportperyear.jrxml");
        JasperReport jasperReport = JasperCompileManager.compileReport(file.getAbsolutePath());
        JRBeanCollectionDataSource dataSource = new JRBeanCollectionDataSource(rp);
        Map<String, Object> parameters = new HashMap<>();
        parameters.put("createdBy", "Java Techie");
        JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, parameters, dataSource);
        JasperExportManager.exportReportToPdfFile(jasperPrint, reportdir.toString() + "/report_per_year.pdf");
        String fileName = "report.pdf";
        return fileName;
	}
	
	public String exportReportPerJob(List<ReportMasterPojo> rp) throws ErrorException, FileNotFoundException, JRException {
        //load file and compile it
        File file = ResourceUtils.getFile("classpath:reportperjob.jrxml");
        JasperReport jasperReport = JasperCompileManager.compileReport(file.getAbsolutePath());
        JRBeanCollectionDataSource dataSource = new JRBeanCollectionDataSource(rp);
        Map<String, Object> parameters = new HashMap<>();
        parameters.put("createdBy", "Java Techie");
        JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, parameters, dataSource);
        JasperExportManager.exportReportToPdfFile(jasperPrint, reportdir.toString() + "/report_per_job.pdf");
        String fileName = "report.pdf";
        return fileName;
	}
	
	public Resource loadFileAsResource(String fileName) throws Exception {
        try {
            Path filePath = reportdir.resolve(fileName).normalize();
            Resource resource = new UrlResource(filePath.toUri());
            if(resource.exists()) {
                return resource;
            } else {
                throw new Exception("File not found " + fileName);
            }
        } catch (MalformedURLException ex) {
            throw new Exception("File not found " + fileName, ex);
        }
    }
}
