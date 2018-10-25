package com.itheima.crm.service;

import com.itheima.crm.domain.User;

public interface UserService {
    User login(User user);

    void regist(User user);
}
