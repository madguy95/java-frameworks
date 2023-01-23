package com.demo.mappers;

import org.apache.ibatis.annotations.Mapper;

import com.demo.model.Student;

@Mapper
public interface StudentXMLMapper {
		
	public Student getStudentByUserName(String userName);


}
