package com.gtech.food_api.domain.service;

import com.gtech.food_api.domain.model.Group;
import com.gtech.food_api.domain.model.User;
import com.gtech.food_api.domain.repository.UserRepository;
import com.gtech.food_api.domain.service.exceptions.BusinessException;
import com.gtech.food_api.domain.service.exceptions.UserNotFoundException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class UserService {

    private static final String USER_IN_USE_MESSAGE = "Usuário com id %d não pode ser excluído pois está em uso";

    @Autowired
    private UserRepository userRepository;
    
    @Autowired
    private GroupService groupService;

    @Transactional(readOnly = true)
    public List<User> listAll(){
        return userRepository.findAll();
    }

    @Transactional
    public User save(User user) {
        // detach desativa o monitoramento do jpa, que nao salve qualquer alteracao no objeto antes do fim do contexto de persistencia
        userRepository.detach(user);

        Optional<User> userByEmail = userRepository.findByEmail(user.getEmail());

        // se o email ja existe e nao é o mesmo usuario, lança exceção
        if (userByEmail.isPresent() && !userByEmail.get().equals(user)) {
            throw new BusinessException(
                String.format("Email %s já está em uso", user.getEmail())
            );
        }
        return userRepository.save(user);
    }

    @Transactional
    public void updatePassword(Long id,  String currentPassword, String newPassword){
        User user = findOrFail(id);
        if (!user.correctPassword(currentPassword, newPassword)) {
            throw new BusinessException("Current password does not match with the password of the user");
        }
        // Dirty Checking: Como a entidade está gerenciada (carregada do banco), ao alterar user.setPassword(),
        // o JPA detecta automaticamente a mudança. No commit da transação, o flush automático persiste
        // as alterações no banco, por isso não é necessário chamar userRepository.save(user).
        user.setPassword(newPassword);
    }

    @Transactional
    public void associateGroup(Long userId, Long groupId){
        User user = findOrFail(userId);
        Group group = groupService.findOrFail(groupId);
        user.addGroup(group);
    }

    @Transactional
    public void disassociateGroup(Long userId, Long groupId){
        User user = findOrFail(userId);
        Group group = groupService.findOrFail(groupId);
        user.removeGroup(group);
    }

    @Transactional(readOnly = true)
    public User findOrFail(Long userId) {
        return userRepository.findById(userId).orElseThrow(()
                -> new UserNotFoundException(userId));
    }
}
