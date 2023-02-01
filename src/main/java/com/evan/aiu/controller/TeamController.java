package com.evan.aiu.controller;

import com.evan.aiu.entity.Team;
import com.evan.aiu.result.Result;
import com.evan.aiu.result.ResultFactory;
import com.evan.aiu.service.TeamService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;


@RestController
public class TeamController {
    @Autowired
    TeamService teamService;

    @GetMapping("/api/team")
    public Result listTeam() {
        return ResultFactory.buildSuccessResult(teamService.list());
    }

    @GetMapping("/api/team/delete")
    public Result deleteTeam(@RequestParam Integer id) {
        teamService.deleteTeam(id);
        return ResultFactory.buildSuccessResult("删除成功！");
    }

    @PostMapping ("/api/team")
    public Result saveTeam(@RequestBody Team team) {
        teamService.saveAndUpdateTeam(team);
        return ResultFactory.buildSuccessResult("更新成功！");
    }

}
