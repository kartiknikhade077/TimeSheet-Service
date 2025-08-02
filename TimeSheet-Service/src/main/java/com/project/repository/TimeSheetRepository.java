package com.project.repository;


import java.time.LocalDate;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.project.entity.TimeSheet;
@Repository
public interface TimeSheetRepository extends JpaRepository<TimeSheet, String> {
	
     TimeSheet findByTimeSheetId(String timeSheetId);
 
     
     Page<TimeSheet> findByCompanyIdAndDesignerNameContainingIgnoreCase(String companyId,String designerName,Pageable pageable);
     
     Page<TimeSheet> findByCompanyIdAndDesignerNameContainingIgnoreCaseAndWorkOrderNoContainingIgnoreCaseAndCreateDateBetweenAndItemNumber(
    		    String companyId,
    		    String designerName,
    		    String workOrderNo,
    		    LocalDate startDate,
    		    LocalDate endDate,
    		    Integer itemNumber,
    		    Pageable pageable
    		);
     
     
     Page<TimeSheet> findByCompanyIdAndDesignerNameContainingIgnoreCaseAndWorkOrderNoContainingIgnoreCaseAndCreateDateBetween(
    		    String companyId,
    		    String designerName,
    		    String workOrderNo,
    		    LocalDate startDate,
    		    LocalDate endDate,
    		    Pageable pageable
    		);
     
     List<TimeSheet> findByCompanyIdAndDesignerNameContainingIgnoreCaseAndWorkOrderNoContainingIgnoreCaseAndCreateDateBetween(
 		    String companyId,
 		    String designerName,
 		    String workOrderNo,
 		    LocalDate startDate,
 		    LocalDate endDate
 		);
     
     List<TimeSheet> findByCompanyIdAndDesignerNameContainingIgnoreCaseAndWorkOrderNoContainingIgnoreCaseAndCreateDateBetweenAndItemNumber(
 		    String companyId,
 		    String designerName,
 		    String workOrderNo,
 		    LocalDate startDate,
 		    LocalDate endDate,
 		    Integer itemNumber
 		   
 		);

     
   

}
