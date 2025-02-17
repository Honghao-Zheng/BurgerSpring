package com.sparta.burgerspring.controller;

import com.sparta.burgerspring.model.entities.Salary;
import com.sparta.burgerspring.model.entities.SalaryId;
import com.sparta.burgerspring.model.repositories.DepartmentRepository;
import com.sparta.burgerspring.model.repositories.SalaryRepository;
import com.sparta.burgerspring.service.EmployeeService;
import com.sparta.burgerspring.service.SalaryService;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;

@Controller
public class SalaryWebController {

    private final SalaryService salaryService;
    private final EmployeeService employeeService;
    private final SalaryRepository salaryRepository;
    private final DepartmentRepository departmentRepository;

    public SalaryWebController(SalaryService salaryService, EmployeeService employeeService, SalaryRepository salaryRepository,
                               DepartmentRepository departmentRepository) {
        this.salaryService = salaryService;
        this.employeeService = employeeService;
        this.salaryRepository = salaryRepository;
        this.departmentRepository = departmentRepository;
    }

    @PreAuthorize("hasRole('ROLE_USER')")
    @GetMapping("/salary")
    public String salaryHome(){
        return "salary/salary-home";
    }

    @PreAuthorize("hasRole('ROLE_USER')")
    @GetMapping("/salaries")
    public String getSalaries(){
        return "salary/salary-search-form";
    }

    @PreAuthorize("hasRole('ROLE_USER')")
    @GetMapping("/salaries/search")
    public String getSalaryDetails(Model model, @RequestParam Integer empNo) {
        model.addAttribute("salaries", salaryRepository.getDetailsByEmpNo(empNo));
        return "salary/salary";
    }

    @PreAuthorize("hasRole('ROLE_USER')")
    @GetMapping(value = "/salaries/{salary}")
    public String getEmployeesEarningAboveSalary(Model model, @PathVariable Integer salary){
        model.addAttribute("employees", salaryService.getEmployeeEarningAboveGivenSalary(salary));
        return "salary/salariesemployees";
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @GetMapping("/salary/create")
    public String getSalaryToCreate() {
        return "salary/salary-create-form";
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PostMapping("/createsalary")
    public String createSalary(@ModelAttribute("salaryToCreate")Salary newSalary, @ModelAttribute("salaryIdToCreate")SalaryId newSalaryId){
        newSalary.setId(newSalaryId);
        if(newSalary.getToDate() == null){
            newSalary.setToDate(LocalDate.of(9999, 01, 01));
        }
        System.out.println(newSalaryId);
        salaryRepository.saveAndFlush(newSalary);
        return "salary/add-salary-success";
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @GetMapping("/salary/edit/{id}/{fromDate}")
    public String getUpdatedSalaryDetails(@PathVariable Integer id,@PathVariable @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate fromDate, Model model) {
        SalaryId salaryId = new SalaryId();
        salaryId.setEmpNo(id);
        salaryId.setFromDate(fromDate);
        Salary salary = salaryRepository.findById(salaryId).orElse(null);
        model.addAttribute("salaryToEdit", salary);
        return "salary/salary-update-form";
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PostMapping("/updatesalary")
    public String updateSalary(@ModelAttribute("updatedSalary")Salary updatedSalary, @ModelAttribute("salaryIdToCreate")SalaryId newSalaryId){
        if(updatedSalary.getToDate() == null){
            updatedSalary.setToDate(LocalDate.of(9999, 01, 01));
        }
        salaryRepository.saveAndFlush(updatedSalary);
        return "salary/salary-update-success";
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @GetMapping("/salary/edit")
    public String getSalaryToUpdate(){
        return "salary/salary-to-update-form";
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PostMapping("/findsalary")
    public String findSalaryToUpdate(@ModelAttribute("salaryToUpdate") SalaryId foundSalary) {
        return "redirect:/salary/edit/" + foundSalary.getEmpNo() + "/" + foundSalary.getFromDate();
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @GetMapping("salary/delete")
    public String getSalaryToDelete(){
        return "salary/salary-to-delete-form";
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PostMapping("/deletesalary")
    public String findSalaryToDelete(@ModelAttribute("salaryToDelete") SalaryId foundSalary) {
        return "redirect:/salary/delete/" + foundSalary.getEmpNo() + "/" + foundSalary.getFromDate();
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @GetMapping("/salary/delete/{id}/{fromDate}")
    public String deleteSalary(@PathVariable Integer id,@PathVariable @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate fromDate){
        SalaryId salaryId = new SalaryId();
        salaryId.setEmpNo(id);
        salaryId.setFromDate(fromDate);
        salaryRepository.deleteById(salaryId);
        return "salary/salary-delete-success";
    }

//    @GetMapping("/salaries")
//    public String getAllSalaries(Model model) {
//        model.addAttribute("salaries", salaryRepository.findAll());
//        return "salary/salaries";
//    }
@GetMapping("/salary/averages")
    public String getSalariesAverageByDepartmentAndDateDefault(Model model){
        model.addAttribute("departments",departmentRepository.findAll());
        model.addAttribute("dept","");
        model.addAttribute("average", "Choose a department and see what the average salary was at the given time!");
        return "/salary/salary-average.html";
}
    @GetMapping("/salary/averages/")
    public String getSalariesAverageByDepartmentAndDate(Model model,@RequestParam String department, @RequestParam LocalDate date){
        model.addAttribute("departments",departmentRepository.findAll());
        System.out.println(departmentRepository.getListOfSalariesByDept(department,date));
        model.addAttribute("average", "The average salary of those working in this department at this given time is £" + departmentRepository.getListOfSalariesByDept(department,date));

        return "/salary/salary-average.html";
    }

}