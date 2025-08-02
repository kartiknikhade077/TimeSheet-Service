package com.project.repository;


import java.time.LocalDate;

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
     
     @Query("SELECT t FROM TimeSheet t " +
    	       "WHERE t.companyId = :companyId " +
    	       "AND (:designer IS NULL OR LOWER(t.designerName) LIKE LOWER(CONCAT('%', :designer, '%'))) " +
    	       "AND (:startDate IS NULL OR t.createDate >= :startDate) " +
    	       "AND (:endDate IS NULL OR t.createDate <= :endDate) " +
    	       "AND (:itemNumber IS NULL OR t.itemNumber = :itemNumber) " +
    	       "AND (:workOrderNo IS NULL OR t.workOrderNo = :workOrderNo)")
    	Page<TimeSheet> searchTimeSheets(
    	        @Param("companyId") String companyId,
    	        @Param("designer") String designer,
    	        @Param("startDate") LocalDate startDate,
    	        @Param("endDate") LocalDate endDate,
    	        @Param("itemNumber") Integer itemNumber,
    	        @Param("workOrderNo") String workOrderNo,
    	        Pageable pageable);


}
