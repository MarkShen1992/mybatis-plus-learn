package io.markshen.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import io.markshen.dao.UserDAO;
import io.markshen.entity.User;
import io.markshen.service.UserService;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl extends ServiceImpl<UserDAO, User> implements UserService {

}
