package com.evan.aiu.dao;


import com.evan.aiu.entity.Volunteer;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface VolunteerDAO extends JpaRepository<Volunteer,Integer> {

    List<Volunteer> findAllByUid(Integer uid);
}
