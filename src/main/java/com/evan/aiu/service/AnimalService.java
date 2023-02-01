package com.evan.aiu.service;

import com.evan.aiu.dao.AnimalDAO;
import com.evan.aiu.dto.AnimalDTO;
import com.evan.aiu.entity.Animal;
import com.evan.aiu.entity.Area;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class AnimalService {
    @Autowired
    AnimalDAO animalDAO;
    @Autowired
    AreaService areaService;

    public List<AnimalDTO> list(){
        Sort sort = new Sort(Sort.Direction.DESC,"id");
        Map<String,String> animalMap = new HashMap<>();
        List<Animal> all = animalDAO.findAll(sort);
        animalMap.put("0","发布未收容");
        animalMap.put("1","收容未领养");
        animalMap.put("2","已申请领养未审核");
        animalMap.put("3","已领养");
        List<AnimalDTO> animalDTOS = new ArrayList<>();
        for (Animal animal: all) {
            AnimalDTO animalDTO = new AnimalDTO();
            BeanUtils.copyProperties(animal,animalDTO);
            animalDTO.setStatusName(animalMap.get(animal.getStatus()));
            animalDTOS.add(animalDTO);
        }
        return animalDTOS;
    }
    //当id存在时更新数据，当id不存在时插入数据
    public void addOrUpdate(Animal animal){
        animalDAO.save(animal);
    }

    public void deleteById(int id){
        animalDAO.deleteById(id);
    }

    public List<AnimalDTO> listByArea(int cid){
        Map<String,String> animalMap = new HashMap<>();
        List<Animal> all = animalDAO.findAllByStatus(String.valueOf(cid));
        animalMap.put("0","发布未收容");
        animalMap.put("1","收容未领养");
        animalMap.put("2","已申请领养未审核");
        animalMap.put("3","已领养");
        List<AnimalDTO> animalDTOS = new ArrayList<>();
        for (Animal animal: all) {
            AnimalDTO animalDTO = new AnimalDTO();
            BeanUtils.copyProperties(animal,animalDTO);
            animalDTO.setStatusName(animalMap.get(animal.getStatus()));
            animalDTOS.add(animalDTO);
        }
        return animalDTOS;
    }

    public List<AnimalDTO> listByAdoptname(String adoptname){
        Map<String,String> animalMap = new HashMap<>();
        List<Animal> all = animalDAO.findAllByAdoptnameAndStatus(adoptname,"2");
        animalMap.put("0","发布未收容");
        animalMap.put("1","收容未领养");
        animalMap.put("2","已申请领养未审核");
        animalMap.put("3","已领养");
        List<AnimalDTO> animalDTOS = new ArrayList<>();
        for (Animal animal: all) {
            AnimalDTO animalDTO = new AnimalDTO();
            BeanUtils.copyProperties(animal,animalDTO);
            animalDTO.setStatusName(animalMap.get(animal.getStatus()));
            animalDTOS.add(animalDTO);
        }
        return animalDTOS;
    }

    //根据关键词进行模糊查询
    public  List<AnimalDTO> search(String keywords){
        Map<String,String> animalMap = new HashMap<>();
        List<Animal> all = animalDAO.findAllByNameLikeOrBreedLike('%'+keywords + "%", "%"+keywords + "%");
        animalMap.put("0","发布未收容");
        animalMap.put("1","收容未领养");
        animalMap.put("2","已申请领养未审核");
        animalMap.put("3","已领养");
        List<AnimalDTO> animalDTOS = new ArrayList<>();
        for (Animal animal: all) {
            AnimalDTO animalDTO = new AnimalDTO();
            BeanUtils.copyProperties(animal,animalDTO);
            animalDTO.setStatusName(animalMap.get(animal.getStatus()));
            animalDTOS.add(animalDTO);
        }
        return animalDTOS;
    }

}
