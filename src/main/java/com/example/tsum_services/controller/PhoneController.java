package com.example.tsum_services.controller;

import com.example.tsum_services.DAO.SqlDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.core.SqlTypeValue;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

import java.sql.SQLType;
import java.sql.Types;
import java.util.*;

@RestController
public class PhoneController {
    @Autowired
private SqlDao sqlDao;

    @GetMapping(
            value = "/index",
            produces = { "application/json" }
    )
     Map<String,Object> transfersUtilitiesParserIBANToAccountIBANGet() {
        Map<String, Integer> mapOuts = new LinkedHashMap<String, Integer>();
        Map<String, Object> mapIns = new HashMap<String, Object>();

        Map<String, Integer> mapInTypes = new LinkedHashMap<>();
       Map<String,Object>  result =sqlDao.mapExecute("dbo","GetCustomer",mapIns,mapInTypes,mapOuts);
        return result;
    }
    @GetMapping(
            value = "/index1",
            produces = { "application/json" }
    )
   List<Map<String,Object>>transfer() {
        String queryTemplate = "select *from Customer order by ID desc";
        List<Map<String,Object>> result =sqlDao.listExecute(queryTemplate, Collections.emptyList());
        return result;
    }
    @GetMapping(
            value = "/index2",
            produces = { "application/json" }
    )
    Map<String,Object>transfer1() {
        Map<String, Integer> mapOuts = new LinkedHashMap<String, Integer>();
        Map<String, Object> mapIns = new HashMap<String, Object>();
mapIns.put("@ID",12);

        Map<String, Integer> mapInTypes = new LinkedHashMap<>();
        mapInTypes.put("@ID", Types.INTEGER);
        Map<String,Object> result =sqlDao.mapExecute("dbo","Details_Cust",mapIns,mapInTypes,mapOuts);
        return result;
    }
}
