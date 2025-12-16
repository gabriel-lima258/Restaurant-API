package com.gtech.food_api.domain.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.gtech.food_api.domain.model.Group;
import com.gtech.food_api.domain.model.Permission;
import com.gtech.food_api.domain.repository.GroupRepository;
import com.gtech.food_api.domain.service.exceptions.EntityInUseException;
import com.gtech.food_api.domain.service.exceptions.GroupNotFoundException;

@Service
public class GroupService {
    
    private static final String GROUP_IN_USE_MESSAGE = "Group with id %d cannot be deleted because it is in use";
    
    @Autowired
    private GroupRepository groupRepository;

    @Autowired
    private PermissionService permissionService;

    @Transactional(readOnly = true)
    public List<Group> listAll() {
        return groupRepository.findAll();
    }

    @Transactional
    public Group save(Group group) {
        return groupRepository.save(group);
    }

    @Transactional
    public void addPermission(Long groupId, Long permissionId) {
        Group group = findOrFail(groupId);
        Permission permission = permissionService.findOrFail(permissionId);
        group.addPermission(permission);
    }

    @Transactional
    public void removePermission(Long groupId, Long permissionId) {
        Group group = findOrFail(groupId);
        Permission permission = permissionService.findOrFail(permissionId);
        group.removePermission(permission);
    }

    @Transactional
    public void delete(Long id){
        if (!groupRepository.existsById(id)) {
            throw new GroupNotFoundException(id);
        }
        try {
            groupRepository.deleteById(id);
            groupRepository.flush();
        } catch (DataIntegrityViolationException e) {
            throw new EntityInUseException(String.format(GROUP_IN_USE_MESSAGE, id));
        }
    }

    @Transactional(readOnly = true)
    public Group findOrFail(Long groupId) {
        return groupRepository.findById(groupId).orElseThrow(
            () -> new GroupNotFoundException(groupId));
    }
}
