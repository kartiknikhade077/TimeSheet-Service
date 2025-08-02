package com.project.controller;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.project.client.UserSerivceClinet;
import com.project.dto.Company;
import com.project.entity.TimeSheet;
import com.project.repository.TimeSheetRepository;

@RestController
@RequestMapping("/timesheet")
public class CompanyController {

	@Autowired
	private UserSerivceClinet userSerivceClinet;

	@Autowired
	private TimeSheetRepository timeSheetRepository;

	Company company;

	@ModelAttribute
	public void companyDetails() {

		company = userSerivceClinet.getCompanyInfo();

	}

	@PostMapping("/createTimeSheet")
	public ResponseEntity<?> createCustomer(@RequestBody TimeSheet timesheet) {

		try {

			timesheet.setCompanyId(String.valueOf(company.getCompanyId()));

			timeSheetRepository.save(timesheet);

			return ResponseEntity.ok(timesheet);

		} catch (Exception e) {

			e.printStackTrace();

			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error  " + e.getMessage());
		}

	}

	@PutMapping("/updateTimeSheet")
	public ResponseEntity<?> updateTimesheet(@RequestBody TimeSheet timesheet) {

		try {

			timesheet.setCompanyId(String.valueOf(company.getCompanyId()));

			timeSheetRepository.save(timesheet);

			return ResponseEntity.ok(timesheet);

		} catch (Exception e) {

			e.printStackTrace();

			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error  " + e.getMessage());
		}

	}

	@GetMapping("/getTimeSheetbyId/{timeSheetId}")
	public ResponseEntity<?> getTimesheetbyId(@PathVariable String timeSheetId) {

		try {

			return ResponseEntity.ok(timeSheetRepository.findByTimeSheetId(timeSheetId));

		} catch (Exception e) {

			e.printStackTrace();

			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error  " + e.getMessage());
		}

	}

	@DeleteMapping("/deletTimesheetbyId/{timeSheetId}")
	public ResponseEntity<?> deletTimesheetbyId(@PathVariable String timeSheetId) {

		try {

			timeSheetRepository.deleteById(timeSheetId);
			return ResponseEntity.ok("Deleted SuccessFully");

		} catch (Exception e) {

			e.printStackTrace();

			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error  " + e.getMessage());
		}

	}

	@GetMapping("/getAllTimeSheets/{page}/{size}")
	public ResponseEntity<?> getAllProjects(@PathVariable int page, @PathVariable int size,
			@RequestParam(defaultValue = "") String designer,@RequestParam(defaultValue = "") String workOrderNumber,
			 @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
		        @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate,
		        @RequestParam(required = false) Integer itemNumber)  {
		try {

			Map<String, Object> data = new HashMap<String, Object>();
			Pageable pageable = PageRequest.of(page, size, Sort.by("timeSheetId").descending());
			
			LocalDate from = (startDate != null) ? startDate : LocalDate.of(1900, 1, 1);
			LocalDate to = (endDate != null) ? endDate : LocalDate.now();
			List<TimeSheet> timeSheetList=null;
			Page<TimeSheet> timeSheetPage=null;
			if (itemNumber == null) {

				 timeSheetPage = timeSheetRepository
					        .findByCompanyIdAndDesignerNameContainingIgnoreCaseAndWorkOrderNoContainingIgnoreCaseAndCreateDateBetween(
					            String.valueOf(company.getCompanyId()),
					            designer != null ? designer : "",
					            workOrderNumber != null ? workOrderNumber : "",
					            from,
					            to,
					            pageable
					        );
				timeSheetList = timeSheetPage.getContent();
			}

			else {

				timeSheetPage = timeSheetRepository
						.findByCompanyIdAndDesignerNameContainingIgnoreCaseAndWorkOrderNoContainingIgnoreCaseAndCreateDateBetweenAndItemNumber(
								String.valueOf(company.getCompanyId()), designer != null ? designer : "", workOrderNumber != null ? workOrderNumber : "", from, to,
								itemNumber, pageable);
				timeSheetList = timeSheetPage.getContent();

			}

			data.put("timeSheetList", timeSheetList);
			data.put("totalPages", timeSheetPage.getTotalPages());
			data.put("currentPage", timeSheetPage.getNumber());
			return ResponseEntity.ok(data);

		} catch (Exception e) {

			e.printStackTrace();
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error " + e.getMessage());
		}
	}

//	@GetMapping("/getAllTimeSheets/{page}/{size}")
//	public ResponseEntity<?> getAllProjects(
//	        @PathVariable int page,
//	        @PathVariable int size,
//	        @RequestParam(defaultValue = "") String designer,
//	        @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
//	        @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate,
//	        @RequestParam(required = false) Integer itemNumber,
//	        @RequestParam(required = false) String workOrderNo
//	) {
//	    try {
//	        Pageable pageable = PageRequest.of(page, size, Sort.by("timeSheetId").descending());
//
//	        Page<TimeSheet> timeSheetPage = timeSheetRepository.searchTimeSheets(
//	                String.valueOf(company.getCompanyId()),
//	                designer != null ? designer : "",
//	                startDate,
//	                endDate,
//	                itemNumber,
//	                workOrderNo != null ? workOrderNo : "",
//	                pageable
//	        );
//
//	        Map<String, Object> data = new HashMap<>();
//	        data.put("timeSheetList", timeSheetPage.getContent());
//	        data.put("totalPages", timeSheetPage.getTotalPages());
//	        data.put("currentPage", timeSheetPage.getNumber());
//	        return ResponseEntity.ok(data);
//	    } catch (Exception e) {
//	        e.printStackTrace();
//	        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error: " + e.getMessage());
//	    }
//	}

}
