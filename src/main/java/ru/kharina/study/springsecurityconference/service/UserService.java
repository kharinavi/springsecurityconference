package ru.kharina.study.springsecurityconference.service;

import org.springframework.stereotype.Service;
import ru.kharina.study.springsecurityconference.model.User;
import ru.kharina.study.springsecurityconference.repository.UserRepository;

import java.util.List;

@Service
public class UserService {
    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    //POST сохранение в базу данных
    public User saveUser(User user){
        userRepository.save(user);
        return user;
    }

    //PUT Обновление в бд по id
    public User updateUser(User newUser, int id) {
        User result = getUserById(id);
        result.setId((long) id);
        result.setFirstName(newUser.getFirstName());
        result.setLastName(newUser.getLastName());
        result.setEmail(newUser.getEmail());
        result.setPassword(newUser.getPassword());
        result.setRole(newUser.getRole());
        result.setStatus(newUser.getStatus());
        userRepository.save(result);
        return result;
    }

    //GET по id
    public User getUserById(int id) {
        return userRepository.getOne((long) id);
    }

    //DELETE по id
    public void deleteUserById(int id) {
        userRepository.deleteById((long) id);
    }
}
