package com.example.demo.controller;

import com.example.demo.dto.DepartmentDTO;
import com.example.demo.dto.DepartmentRequestDTO;
import com.example.demo.dto.DeptEmpListDTO;
import com.example.demo.dto.WorkplaceDTO;
import com.example.demo.service.DepartmentService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping("system/user/departments")
public class DepartmentController {

    @Autowired
    private DepartmentService departmentService;

    @GetMapping("/getDeptList/{CO_CD}")
    public ResponseEntity<List<DepartmentDTO>> getDepartmentHierarchy(@PathVariable("CO_CD") String CO_CD) {
        try {
            List<DepartmentDTO> departmentHierarchy = departmentService.getDepartmentHierarchy(CO_CD);
            log.info("Get Department List Controller", CO_CD);
            return new ResponseEntity<>(departmentHierarchy, HttpStatus.OK);
        } catch (Exception e) {
            log.error("Error while fetching departments for CO_CD: " + CO_CD, e);
            return new ResponseEntity<>(Collections.emptyList(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    //부서 상세
    @GetMapping("getDeptInfo/{deptCd}")
    public ResponseEntity<DepartmentDTO> getDepartmentInfo(@PathVariable String deptCd,
                                                           @RequestParam String coCd, @RequestParam String divCd) {
        try {
            Map<String, String> params = new HashMap<>();
            params.put("CO_CD", coCd);
            params.put("DIV_CD", divCd);
            params.put("DEPT_CD", deptCd);
            DepartmentDTO deptInfo = departmentService.selectDepartmentInfoByDEPTCD(params);
            log.info("Get Department Detail Controller deptCd: {} deptInfo: {}", deptCd, deptInfo);
            return new ResponseEntity<>(deptInfo, HttpStatus.OK);
        } catch (Exception e) {
            log.error("Error while fetching Department info for deptCd {}: ", deptCd, e);
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    //부서 추가
    @PostMapping("insert")
    public ResponseEntity<Integer> insertDepartment(@RequestBody DepartmentDTO departmentDTO) {
        try {
            int result = departmentService.insertDepartment(departmentDTO);
            //log.info("Inserted Department Controller", departmentDTO, result);
            log.info("Insert Department Controller");
            return new ResponseEntity<>(result, HttpStatus.OK);
        } catch (Exception e) {
            log.error("Error while inserting Department: ", e);
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    //부서 수정
    @PutMapping("update")
    public ResponseEntity<Integer> updateDepartment(@RequestBody DepartmentDTO departmentDTO) {
        try {
            int result = departmentService.updateDepartment(departmentDTO);
            //log.info("Update Department Controller", departmentDTO, result);
            log.info("Update Department Controller");
            return new ResponseEntity<>(result, HttpStatus.OK);
        } catch (Exception e) {
            log.error("Error while updating Department: ", e);
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    //부서코드중복검사
    @GetMapping("/deptCheck")
    public boolean checkDuplicate(@RequestParam String coCd, @RequestParam String deptCd) {
        Map<String, String> params = new HashMap<>();
        params.put("CO_CD", coCd);
        params.put("DEPT_CD", deptCd);
        return departmentService.isDepartmentDuplicate(params);
    }


    @GetMapping("/list")
    public List<DeptEmpListDTO> getDeptEmpList(@RequestParam String CO_CD, @RequestParam String DEPT_CD) {
        Map<String, String> params = new HashMap<>();
        params.put("CO_CD", CO_CD);
        params.put("DEPT_CD", DEPT_CD);
        return departmentService.getDeptEmpList(params);
    }

    @GetMapping("/check-data")
    public boolean checkDataExistence(@RequestParam String CO_CD, @RequestParam String DEPT_CD) {
        return departmentService.checkExistence(CO_CD, DEPT_CD);
    }

    @PutMapping("/update-department-employee")
    public ResponseEntity<String> updateDepartmentAndEmployee(@RequestBody Map<String, String> body) {
        String CO_CD = body.get("CO_CD");
        String DEPT_CD = body.get("DEPT_CD");

        Map<String, Object> params = new HashMap<>();
        params.put("CO_CD", CO_CD);
        params.put("DEPT_CD", DEPT_CD);

        int resultCount = departmentService.updateDepartmentAndEmployee(params);
        if (resultCount >= 0) {
            return new ResponseEntity<>("Total rows affected: " + resultCount, HttpStatus.OK);
        } else {
            return new ResponseEntity<>("Error occurred.", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @DeleteMapping("/delete")
    public ResponseEntity<String> deleteDepartment(@RequestBody DepartmentRequestDTO departmentRequestDTO) {
        int resultCount = departmentService.deleteDepartment(departmentRequestDTO);
        log.info("Delete Department Controller" +  departmentRequestDTO);
        if (resultCount >= 0) {
            return new ResponseEntity<>("Total rows deleted: " + resultCount, HttpStatus.OK);
        } else {
            return new ResponseEntity<>("Error occurred.", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }



}
