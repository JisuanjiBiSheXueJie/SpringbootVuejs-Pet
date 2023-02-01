package com.evan.aiu.controller;

import com.evan.aiu.dao.VolunteerDAO;
import com.evan.aiu.entity.Volunteer;
import com.evan.aiu.result.Result;
import com.evan.aiu.result.ResultFactory;
import com.evan.aiu.service.VolunteerService;
import org.elasticsearch.common.recycler.Recycler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.persistence.criteria.CriteriaBuilder;


@RestController
public class VolunteerController {
    @Autowired
    VolunteerService volunteerService;

    @GetMapping("/api/volunteer")
    public Result listVolunteer() {
        return ResultFactory.buildSuccessResult(volunteerService.list());
    }

    @GetMapping("/api/volunteerbyuid")
    public Result listVolunteerByUid(@RequestParam Integer uid) {
        return ResultFactory.buildSuccessResult(volunteerService.listbyuid(uid));
    }

    @GetMapping("/api/volunteer/delete")
    public Result deleteVolunteer(@RequestParam Integer id) {
        volunteerService.deleteVolunteer(id);
        return ResultFactory.buildSuccessResult("删除成功！");
    }

    @PostMapping("/api/volunteer")
    public void updateOrAddVolunteer(@RequestBody Volunteer volunteer) {
        volunteerService.updateOrInsert(volunteer);
    }
}
