package com.xvjialing.user.repository;

import com.xvjialing.user.domain.User;
import org.springframework.data.repository.CrudRepository;

public interface UserRepository extends CrudRepository<User,Integer> {

    boolean existsByUserNameAndAndPassword(String userName,String password);

    boolean existsByUserName(String userName);
}
