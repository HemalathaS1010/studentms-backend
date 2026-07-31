package com.trainingsapp.schoolms.service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import javax.transaction.Transactional;
import javax.validation.Valid;
import javax.ws.rs.Consumes;
import javax.ws.rs.core.MediaType;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.trainingsapp.schoolms.dao.IStudentRepository;
import com.trainingsapp.schoolms.dto.AddStudentRequest;
import com.trainingsapp.schoolms.dto.StudentDetails;
import com.trainingsapp.schoolms.dto.UpdateStudentDetails;
import com.trainingsapp.schoolms.entities.Student;
import com.trainingsapp.schoolms.exception.StudentNotFoundException;
import com.trainingsapp.schoolms.util.DateUtil;
import com.trainingsapp.schoolms.util.StudentUtil;

@Transactional
@Service
public class StudentServiceImp implements IStudentService {
	@Autowired
	private IStudentRepository dao;
	@Autowired
	private StudentUtil util;

	@Autowired
	private DateUtil dateutil;

	@Override
	public StudentDetails add(AddStudentRequest request) {

		String firstName = request.getFirstName();
		String lastName = request.getLastName();
		String birthDateText = request.getBirthDate();
		Student student = new Student();
		student.setFirstName(firstName);
		student.setLastName(lastName);
		LocalDate date = dateutil.convertToDate(birthDateText);
		student.setBirthDate(date);
		student = dao.save(student);
		StudentDetails response = util.convert(student);
		return response;

	}

	@Override
	public StudentDetails findById(long id) throws StudentNotFoundException {
		Student student = findId(id);
		StudentDetails response = util.convert(student);
		return response;

	}

	public Student findId(long id) {
		Optional<Student> optional = dao.findById(id);
		if (!(optional.isPresent())) {
			throw new StudentNotFoundException("Student Not Found");
		}
		Student student = optional.get();
		return student;
	}

	@Override
	public List<StudentDetails> findByFullName(String firstName, String lastName) {

		List<Student> student = dao.findByFirstnameAndLastname(firstName, lastName);
		if (student.isEmpty()) {
			throw new StudentNotFoundException("Student not found");
		}
		List<StudentDetails> detail = util.convertToStudentList(student);
		return detail;
	}

	@Override
	public List<StudentDetails> findAll() {
		List<Student> student = dao.findAll();
		List<StudentDetails> detail = util.convertToStudentList(student);
		return detail;
	}

	@Override
	public void remove(long id) {
		Optional<Student> optional = dao.findById(id);
		if (!(optional.isPresent())) {
			throw new StudentNotFoundException("Student Not Found");
		}
		Student student = optional.get();
		dao.delete(student);
		System.out.println("student detail is removed");
	}

	@Override
	public StudentDetails update(UpdateStudentDetails student) {

		long id = student.getId();
		Student getStudent = findId(id);
		getStudent.setFirstName(student.getFirstName());
		getStudent.setLastName(student.getLastName());
		LocalDate date = dateutil.convertToDate(student.getBirthDate());
		getStudent.setBirthDate(date);
		getStudent = dao.save(getStudent);
		StudentDetails response = util.convert(getStudent);
		return response;

	}

	@Override
	
	public List<StudentDetails> addList(@Valid List<AddStudentRequest> request) {
		List<Student> student = new ArrayList<>();

		
for (AddStudentRequest st : request) {

Student st1=new Student();		
st1.setFirstName(st.getFirstName());
st1.setLastName(st.getLastName());
LocalDate date = dateutil.convertToDate(st.getBirthDate());
st1.setBirthDate(date);

st1=dao.save(st1);
student.add(st1);


}
		List<StudentDetails> detail = util.convertToStudentList(student);
		return detail;
	}

	@Override
	public List<StudentDetails> updateList(@Valid List<UpdateStudentDetails> request) {
		List<Student> student = new ArrayList<>();
		for(UpdateStudentDetails st:request) {
			long id=st.getId();
			Student getStudent = findId(id);
			getStudent.setFirstName(st.getFirstName());
			getStudent.setLastName(st.getLastName());
			LocalDate date = dateutil.convertToDate(st.getBirthDate());
			getStudent.setBirthDate(date);
			getStudent = dao.save(getStudent);
			student.add(getStudent);
			
		}
		List<StudentDetails> detail = util.convertToStudentList(student);
		return detail;
			}

}
