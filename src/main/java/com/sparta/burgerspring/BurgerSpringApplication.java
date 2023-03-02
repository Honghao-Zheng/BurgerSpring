package com.sparta.burgerspring;


import com.sparta.burgerspring.model.entities.Department;
import com.sparta.burgerspring.model.repositories.DepartmentRepository;
import com.sparta.burgerspring.model.repositories.DeptEmpRepository;
import com.sparta.burgerspring.model.repositories.EmployeeRepository;
import com.sparta.burgerspring.model.service.EmployeesService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.time.LocalDate;


@SpringBootApplication
public class BurgerSpringApplication {
    private Logger logger= LoggerFactory.getLogger(BurgerSpringApplication.class);
    private final DeptEmpRepository deptEmpRepository;

    public BurgerSpringApplication(DeptEmpRepository deptEmpRepository) {
        this.deptEmpRepository = deptEmpRepository;
    }

    public static void main(String[] args) {
        SpringApplication.run(BurgerSpringApplication.class, args);
    }


    @Bean
    public CommandLineRunner run(DepartmentRepository departmentRepository, DeptEmpRepository deptEmpRepository, EmployeeRepository employeeRepository){
    return args -> {
        Department department=departmentRepository.findByDeptName("Development");
    logger.info(

            new EmployeesService(
            departmentRepository,
            deptEmpRepository ,
            employeeRepository).
            getEmployeesByDateAndDepartment(
                    LocalDate.parse("1986-07-24"),
                    LocalDate.parse("1986-07-24"),
                    "Development").toString()
//            departmentRepository.findByDeptName("Development").getId().toString()
//            deptEmpRepository.findByFromDateIsBeforeAndToDateAfterAndDeptNo(
//                    LocalDate.parse(
//                            "1986-07-24"),
//                    LocalDate.parse(
//                            "1986-07-24"),
//                    department).toString()
    );

    };

}

}
