package com.project.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;


import com.project.dto.Company;
import com.project.dto.Employee;
import com.project.dto.ModuleAccess;
import com.project.dto.User;



@FeignClient(name="USER-SERVICE")
public interface UserSerivceClinet {
	
	@GetMapping("/company/getCompanyInfo")
	public Company getCompanyInfo();
	
	@GetMapping("/employee/getEmployeeInfo")
	public Employee getEmployeeInfo();
	
	@GetMapping("/company/getUserInfo")
	public User getUserInfo();
	
	@GetMapping("/company/getModuleAccessInfo")
	public ModuleAccess getModuleAccessInfo();


}
