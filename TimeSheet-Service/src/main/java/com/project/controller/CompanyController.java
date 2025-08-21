package com.project.controller;

import java.io.IOException;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.BiConsumer;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.FillPatternType;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
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
import com.project.dto.Employee;
import com.project.dto.ModuleAccess;
import com.project.dto.User;
import com.project.entity.TimeSheet;
import com.project.repository.TimeSheetRepository;

import jakarta.servlet.http.HttpServletResponse;

@RestController
@RequestMapping("/timesheet")
public class CompanyController {

	@Autowired
	private UserSerivceClinet userSerivceClinet;

	@Autowired
	private TimeSheetRepository timeSheetRepository;

	Company company;
	User user;
	Employee employee;
	ModuleAccess moduleAccess;

	@ModelAttribute
	public void getUserInfo() {

		user = userSerivceClinet.getUserInfo();
		moduleAccess =userSerivceClinet.getModuleAccessInfo();
		if(user.getRole().equalsIgnoreCase("ROLE_COMPANY")) {
			
			company = userSerivceClinet.getCompanyInfo();
		}else {
			employee=userSerivceClinet.getEmployeeInfo();
		}

	}

	@PostMapping("/createTimeSheet")
	public ResponseEntity<?> createCustomer(@RequestBody TimeSheet timesheet) {

		try {

			if (user.getRole().equalsIgnoreCase("ROLE_COMPANY")) {
				timesheet.setCompanyId(company.getCompanyId());
			} else {
				timesheet.setEmployeeId(employee.getEmployeeId());
				timesheet.setCompanyId(employee.getCompanyId());
			}

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

			if (user.getRole().equalsIgnoreCase("ROLE_COMPANY")) {
				timesheet.setCompanyId(company.getCompanyId());
			} else {
				timesheet.setEmployeeId(employee.getEmployeeId());
				timesheet.setCompanyId(employee.getCompanyId());
			}

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
				if(user.getRole().equalsIgnoreCase("ROLE_COMPANY")) {
				 timeSheetPage = timeSheetRepository
					        .findByCompanyIdAndDesignerNameContainingIgnoreCaseAndWorkOrderNoContainingIgnoreCaseAndCreateDateBetween(
					            company.getCompanyId(),
					            designer != null ? designer : "",
					            workOrderNumber != null ? workOrderNumber : "",
					            from,
					            to,
					            pageable
					        );
				}else if(moduleAccess.isTimeSheetViewAll()) {
					 timeSheetPage = timeSheetRepository
						        .findByCompanyIdAndDesignerNameContainingIgnoreCaseAndWorkOrderNoContainingIgnoreCaseAndCreateDateBetween(
						            employee.getCompanyId(),
						            designer != null ? designer : "",
						            workOrderNumber != null ? workOrderNumber : "",
						            from,
						            to,
						            pageable
						        );	
				}else {
					 timeSheetPage = timeSheetRepository
						        .findByEmployeeIdAndDesignerNameContainingIgnoreCaseAndWorkOrderNoContainingIgnoreCaseAndCreateDateBetween(
						            employee.getEmployeeId(),
						            designer != null ? designer : "",
						            workOrderNumber != null ? workOrderNumber : "",
						            from,
						            to,
						            pageable
						        );
				}
				timeSheetList = timeSheetPage.getContent();
			}

			else {
				if(user.getRole().equalsIgnoreCase("ROLE_COMPANY")) {
				timeSheetPage = timeSheetRepository
						.findByCompanyIdAndDesignerNameContainingIgnoreCaseAndWorkOrderNoContainingIgnoreCaseAndCreateDateBetweenAndItemNumber(
								company.getCompanyId(), designer != null ? designer : "", workOrderNumber != null ? workOrderNumber : "", from, to,
								itemNumber, pageable);
				
				}else if(moduleAccess.isTimeSheetViewAll()) {
					
					timeSheetPage = timeSheetRepository
							.findByCompanyIdAndDesignerNameContainingIgnoreCaseAndWorkOrderNoContainingIgnoreCaseAndCreateDateBetweenAndItemNumber(
									employee.getCompanyId(), designer != null ? designer : "", workOrderNumber != null ? workOrderNumber : "", from, to,
									itemNumber, pageable);
				}else {
					
					timeSheetPage = timeSheetRepository
							.findByEmployeeIdAndDesignerNameContainingIgnoreCaseAndWorkOrderNoContainingIgnoreCaseAndCreateDateBetweenAndItemNumber(
									employee.getEmployeeId(), designer != null ? designer : "", workOrderNumber != null ? workOrderNumber : "", from, to,
									itemNumber, pageable);
				}
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

	@GetMapping("/exportTimeSheet")
	public void exportTimeSheetToExcel(HttpServletResponse response,
			@RequestParam(defaultValue = "") String designer,@RequestParam(defaultValue = "") String workOrderNumber,
			 @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
		        @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate,
		        @RequestParam(required = false) Integer itemNumber
			) throws IOException {
		
		try {
	    response.setContentType("application/octet-stream");
	    String headerKey = "Content-Disposition";
	    String headerValue = "attachment; filename=timesheets.xlsx";
	    response.setHeader(headerKey, headerValue);
	    
	    
	    LocalDate from = (startDate != null) ? startDate : LocalDate.of(1900, 1, 1);
		LocalDate to = (endDate != null) ? endDate : LocalDate.now();
		List<TimeSheet> timeSheetList=null;
		if (itemNumber == null) {

			timeSheetList = timeSheetRepository
				        .findByCompanyIdAndDesignerNameContainingIgnoreCaseAndWorkOrderNoContainingIgnoreCaseAndCreateDateBetween(
				            String.valueOf(company.getCompanyId()),
				            designer != null ? designer : "",
				            workOrderNumber != null ? workOrderNumber : "",
				            from,
				            to
				          );
			
		}
		else {

			timeSheetList = timeSheetRepository
					.findByCompanyIdAndDesignerNameContainingIgnoreCaseAndWorkOrderNoContainingIgnoreCaseAndCreateDateBetweenAndItemNumber(
							String.valueOf(company.getCompanyId()), designer != null ? designer : "", workOrderNumber != null ? workOrderNumber : "", from, to,
							itemNumber);

		}


	//    List<TimeSheet> timeSheets = timeSheetRepository.findAll(); // You can add filters if needed

	    XSSFWorkbook workbook = new XSSFWorkbook();
	    XSSFSheet sheet = workbook.createSheet("TimeSheets");

	    // Header Row
	    Row headerRow = sheet.createRow(0);
	    String[] headers = { "Create Date ","Item No","Work Order No", "Designer Name","From Time","To Time","Total Time","Remarks"  };
	    for (int i = 0; i < headers.length; i++) {
	        headerRow.createCell(i).setCellValue(headers[i]);
	    }
	    
	 // Create styles for red background + strikethrough
	    CellStyle redStrikeThroughStyle = workbook.createCellStyle();
	    redStrikeThroughStyle.setFillForegroundColor(IndexedColors.RED.getIndex());
	    redStrikeThroughStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);

	    Font strikeFont = workbook.createFont();
	    strikeFont.setStrikeout(true);
	    strikeFont.setColor(IndexedColors.WHITE.getIndex()); // White text for contrast
	    redStrikeThroughStyle.setFont(strikeFont);

	    // Create normal style (optional, for other rows)
	    CellStyle normalStyle = workbook.createCellStyle();
	    Font normalFont = workbook.createFont();
	    normalFont.setStrikeout(false);
	    normalStyle.setFont(normalFont);


	    // Data Rows
	    int rowIndex = 1;
	    Double totalTime=0.0;
	    for (TimeSheet ts : timeSheetList) {
	    	Row row = sheet.createRow(rowIndex++);

	        boolean isCancelled = ts.isProcessStatus(); // processStatus == true

	        // Helper to set cell with style
	        BiConsumer<Integer, String> setCell = (colIndex, value) -> {
	            Cell cell = row.createCell(colIndex);
	            cell.setCellValue(value != null ? value : "");
	            cell.setCellStyle(isCancelled ? redStrikeThroughStyle : normalStyle);
	        };

	        setCell.accept(0, ts.getCreateDate() != null ? ts.getCreateDate().toString() : "");
	        setCell.accept(1, String.valueOf(ts.getItemNumber()));
	        setCell.accept(2, ts.getWorkOrderNo());
	        setCell.accept(3, ts.getDesignerName());
	        setCell.accept(4, ts.getStartTime() != null ? ts.getStartTime().toString() : "");
	        setCell.accept(5, ts.getEndTime() != null ? ts.getEndTime().toString() : "");
	        setCell.accept(6, String.valueOf(ts.getTotalTime()));
	        setCell.accept(7, ts.getRemarks());
	        
	        totalTime=totalTime+ts.getTotalTime();
	    }
	    double roundedTotalTime = Math.round(totalTime * 100.0) / 100.0;
	    Row row = sheet.createRow(rowIndex++);
	    row.createCell(5).setCellValue("Total Time");
	    row.createCell(6).setCellValue(roundedTotalTime +" Hrs");

	    workbook.write(response.getOutputStream());
	    workbook.close();
	    
		}catch(Exception e) {
			
			e.printStackTrace();
			
		}
	}
	
	
	@PutMapping("/updateWorkOrderStaus/{workOrder}/{status}")
	public ResponseEntity<?> updateWorkOrderStaus(@PathVariable String workOrder,@PathVariable boolean status) {
		try {
		     timeSheetRepository.updateWorkOrderStatus(workOrder, status);
			return ResponseEntity.ok("Status Updated");
		} catch (Exception e) {

			e.printStackTrace();
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
					.body("Error creating employee: " + e.getMessage());
		}
	}
	


}
