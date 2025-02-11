package com.cart_page.cart_page.service;

import com.cart_page.cart_page.daos.UserDao;
import com.cart_page.cart_page.entities.SpringUser;
import com.cart_page.cart_page.entities.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class SpringUserService implements UserDetailsService {
    private final UserDao userDao;

    public SpringUserService(UserDao userDao) {
        this.userDao = userDao;
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        User user = userDao.findByEmail(email);
        return new SpringUser(user);
    }
}
