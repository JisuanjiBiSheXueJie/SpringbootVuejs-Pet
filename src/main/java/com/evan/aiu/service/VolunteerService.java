package com.evan.aiu.service;

import com.evan.aiu.dao.TeamDAO;
import com.evan.aiu.dao.UserDAO;
import com.evan.aiu.dao.VolunteerDAO;
import com.evan.aiu.dto.VolunteerDTO;
import com.evan.aiu.entity.Team;
import com.evan.aiu.entity.User;
import com.evan.aiu.entity.Volunteer;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;


@Service
public class VolunteerService {
    @Autowired
    VolunteerDAO volunteerDAO;

    @Autowired
    UserDAO userDAO;

    public List<VolunteerDTO> list() {
        List<Volunteer> VolunteerList = volunteerDAO.findAll();
        Map<String,String> statusMap = new HashMap<>();
        statusMap.put("0","未审核");
        statusMap.put("1","已通过");
        statusMap.put("2","被驳回");
        List<VolunteerDTO> volunteerDTOS = new ArrayList<>();
        for (Volunteer volunteer : VolunteerList) {
            VolunteerDTO volunteerDTO = new VolunteerDTO();
            Optional<User> byId = userDAO.findById(volunteer.getUid());
            volunteerDTO.setId(volunteer.getId());
            volunteerDTO.setStatus(volunteer.getStatus());
            volunteerDTO.setUid(volunteer.getUid());
            volunteerDTO.setStatusName(statusMap.get(volunteer.getStatus()));
            volunteerDTO.setUserName(byId.get().getName());
            volunteerDTOS.add(volunteerDTO);
        }
        return volunteerDTOS;
    }

    public List<VolunteerDTO> listbyuid(Integer uid) {
        List<Volunteer> VolunteerList = volunteerDAO.findAllByUid(uid);
        Map<String,String> statusMap = new HashMap<>();
        statusMap.put("0","未审核");
        statusMap.put("1","已通过");
        statusMap.put("2","被驳回");
        List<VolunteerDTO> volunteerDTOS = new ArrayList<>();
        for (Volunteer volunteer : VolunteerList) {
            VolunteerDTO volunteerDTO = new VolunteerDTO();
            Optional<User> byId = userDAO.findById(volunteer.getUid());
            volunteerDTO.setId(volunteer.getId());
            volunteerDTO.setStatus(volunteer.getStatus());
            volunteerDTO.setUid(volunteer.getUid());
            volunteerDTO.setStatusName(statusMap.get(volunteer.getStatus()));
            volunteerDTO.setUserName(byId.get().getName());
            volunteerDTOS.add(volunteerDTO);
        }
        return volunteerDTOS;
    }

    public void deleteVolunteer(Integer id) {
        volunteerDAO.deleteById(id);
    }

    public void updateOrInsert(Volunteer volunteer) {
        if (null == volunteer.getId()) {
            List<Volunteer> allByUid = volunteerDAO.findAllByUid(volunteer.getUid());
            if (!allByUid.isEmpty()) {
                return;
            }
        }
        volunteerDAO.save(volunteer);
    }
}
