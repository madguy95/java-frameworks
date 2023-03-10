package com.demo.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.demo.mappers.StudentMapper;
import com.demo.mappers.StudentXMLMapper;
import com.demo.model.Student;

@Service("studentService")
public class StudentServiceImpl implements StudentService {

	@Autowired
	private StudentMapper studentMapper;
	
	@Autowired
	private StudentXMLMapper studentXMLMapper;
	
	@Transactional(rollbackFor = Exception.class)
	public void insertStudent(Student student) {
		studentMapper.insertStudent(student);
//		throw new RuntimeException("test rollback");
	}

	public boolean getStudentByLogin(String userName, String password) {
		Student student = studentXMLMapper.getStudentByUserName(userName);
		
		if(student != null && student.getPassword().equals(password)) {
			return true;
		}
		
		return false;
	}

	public boolean getStudentByUserName(String userName) {
		Student student = studentXMLMapper.getStudentByUserName(userName);
		
		if(student != null) {
			return true;
		}
		
		return false;
	}

}
