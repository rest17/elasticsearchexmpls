package com.pc.searchapi.controller;

/**
 * Created by chitra_chitralekha on 10/28/19.
 */

import com.pc.searchapi.dao.PlanDao;
import com.pc.searchapi.model.PlanData;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/search")
public class PlanSearchController {

    private PlanDao planDao;

    public PlanSearchController(PlanDao planDao) {
        this.planDao = planDao;
    }

    @PostMapping
    public PlanData insertBook(@RequestBody PlanData planData) throws Exception{
        return planDao.insertData(planData);
    }

    @GetMapping("/planname/{name}")
    public Map<String, Object> getBookById(@PathVariable String planName){
        return planDao.getPlanBySearchCriteria(planName);
    }

    @GetMapping("/sponsname/{name}")
    public Map<String, Object> updateBookById(@PathVariable String sponsName){
        return planDao.getPlanBySearchCriteria(sponsName);
    }

    @GetMapping("/sponsstate/{state}")
    public void deleteBookById(@PathVariable String state){
        planDao.getPlanBySearchCriteria(state);
    }
}

