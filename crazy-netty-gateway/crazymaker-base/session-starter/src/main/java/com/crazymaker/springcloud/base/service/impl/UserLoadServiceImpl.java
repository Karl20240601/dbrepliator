package com.crazymaker.springcloud.base.service.impl;

import com.crazymaker.springcloud.base.dao.UserDao;
import com.crazymaker.springcloud.base.dao.po.UserPO;
import com.crazymaker.springcloud.common.dto.UserDTO;
import com.crazymaker.springcloud.standard.context.SpringContextUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

/**
 * Created by 尼恩 on 2019/7/18.
 */

@Slf4j
@Service
public class UserLoadServiceImpl {


    private UserDao userDao;


    public UserLoadServiceImpl() {
    }

    /**
     * 装载用户
     *
     * @param userId
     * @return
     */
    public UserDTO loadUser(Long userId) {

        if (null == userDao) {
            userDao = SpringContextUtil.getBean(UserDao.class);
        }

        UserPO userPO = userDao.findByUserId(userId);
        if (userPO != null) {
            UserDTO dto = new UserDTO();

            BeanUtils.copyProperties(userPO, dto);

            return dto;
        }
        return null;
    }


}
