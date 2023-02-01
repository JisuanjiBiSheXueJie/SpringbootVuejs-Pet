package com.evan.aiu.service;

import com.evan.aiu.dao.TeamDAO;
import com.evan.aiu.dao.UserDAO;
import com.evan.aiu.dto.UserDTO;
import com.evan.aiu.entity.AdminRole;
import com.evan.aiu.entity.Team;
import com.evan.aiu.entity.User;
import org.apache.shiro.crypto.SecureRandomNumberGenerator;
import org.apache.shiro.crypto.hash.SimpleHash;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.util.HtmlUtils;

import java.util.List;
import java.util.stream.Collectors;


@Service
public class TeamService {
    @Autowired
    TeamDAO teamDAO;

    public List<Team> list() {
        List<Team> teamList = teamDAO.findAll();
        return teamList;
    }
    public void deleteTeam(Integer id) {
        teamDAO.deleteById(id);
    }

    public void saveAndUpdateTeam(Team team) {
        teamDAO.save(team);
    }

}
