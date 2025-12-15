package com.gtech.food_api.domain.service;

import com.gtech.food_api.domain.model.User;
import com.gtech.food_api.domain.repository.UserRepository;
import com.gtech.food_api.domain.service.exceptions.BusinessException;
import com.gtech.food_api.domain.service.exceptions.EntityInUseException;
import com.gtech.food_api.domain.service.exceptions.UserNotFoundException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class UserService {

    private static final String USER_IN_USE_MESSAGE = "User with id %d cannot be deleted because it is in use";

    @Autowired
    private UserRepository userRepository;

    @Transactional(readOnly = true)
    public List<User> listAll(){
        return userRepository.findAll();
    }

    @Transactional
    public User save(User user) {
        return userRepository.save(user);
    }

    @Transactional
    public void updatePassword(Long id,  String currentPassword, String newPassword){
        User user = findOrFail(id);
        if (!user.correctPassword(currentPassword, newPassword)) {
            throw new BusinessException("Current password does not match with the password of the user");
        }
        user.setPassword(newPassword);
    }

    @Transactional(readOnly = true)
    public User findOrFail(Long userId) {
        return userRepository.findById(userId).orElseThrow(()
                -> new UserNotFoundException(userId));
    }
}
