package com.crazymaker.springcloud.base.service.impl;

import com.crazymaker.springcloud.base.auth.AuthUtils;
import com.crazymaker.springcloud.base.auth.Payload;
import com.crazymaker.springcloud.base.dao.UserDao;
import com.crazymaker.springcloud.base.dao.po.UserPO;
import com.crazymaker.springcloud.common.dto.UserDTO;
import com.crazymaker.springcloud.common.exception.BusinessException;
import com.crazymaker.springcloud.common.util.JsonUtil;
import com.crazymaker.springcloud.standard.redis.RedisRepository;
import com.crazymaker.springcloud.user.api.dto.LoginInfoDTO;
import com.crazymaker.springcloud.user.api.dto.LoginOutDTO;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.session.Session;
import org.springframework.session.data.redis.RedisOperationsSessionRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;
import java.util.Optional;

import static com.crazymaker.springcloud.common.context.SessionHolder.G_USER;

@Slf4j
@Service
public class UserServiceImpl {

    //Dao Bean ，用于查询数据库用户
    @Resource
    UserDao userDao;

    //加密器
    @Resource
    private PasswordEncoder passwordEncoder;

    //缓存操作服务
    @Resource
    RedisRepository redisRepository;

    //redis 会话存储服务
    @Resource
    private RedisOperationsSessionRepository sessionRepository;


    public UserDTO getUser(Long id) {
        UserPO userPO = userDao.findByUserId(id);
        if (userPO != null) {
            UserDTO userDTO = new UserDTO();
            BeanUtils.copyProperties(userPO, userDTO);
            return userDTO;
        }

        return null;
    }

    @Transactional(rollbackFor = Exception.class)
    public void save(UserPO user) {
        userDao.save(user);
    }


    @Transactional(rollbackFor = Exception.class)
    public void add(UserDTO req) {
        UserPO user = new UserPO();
        BeanUtils.copyProperties(req, user);

        userDao.save(user);
    }

    @Transactional(rollbackFor = Exception.class)
    public void delete(Long id) {
        Optional<UserPO> optional = userDao.findById(id);
        if (!optional.isPresent()) {
            throw new BusinessException("删除对象不存在");
        }
        UserPO user = optional.get();
        userDao.delete(user);
    }

    /**
     * 登陆 处理
     *
     * @param dto 用户名、密码
     * @return 登录成功的 dto
     */
    public LoginOutDTO produceUserToken(LoginInfoDTO dto) {
        String username = dto.getUsername();

        //从数据库获取用户
        List<UserPO> list = userDao.findAllByUsername(username);

        if (null == list || list.size() <= 0) {
            throw BusinessException.builder().errMsg("用户名或者密码错误").build();
        }
        UserPO userPO = list.get(0);

        //进行密码的验证
        //String encode = passwordEncoder.encode(dto.getPassword());
        String encoded = userPO.getPassword();
        String raw = dto.getPassword();
        boolean matched = passwordEncoder.matches(raw, encoded);
        if (!matched) {
            throw BusinessException.builder().errMsg("用户名或者密码错误").build();
        }



        String uid = String.valueOf(userPO.getUserId());

//        String salt = userPO.getPassword();
        // 构建JWT token
//        String token = AuthUtils.buildToken(sid, salt);
        String token = AuthUtils.buildRsaToken("no-sid", uid);

        /**
         * 将用户信息缓存起来
         */
        UserDTO cacheDto = new UserDTO();
        BeanUtils.copyProperties(userPO, cacheDto);
        cacheDto.setToken(token);

        LoginOutDTO outDTO = new LoginOutDTO();
        BeanUtils.copyProperties(cacheDto, outDTO);

        return outDTO;

    }

    /**
     * 账户 登陆 处理
     *
     * @param dto
     */
    public String produceAccountToken(LoginInfoDTO dto) {
        String username = dto.getUsername();

        String token = AuthUtils.buildRsaToken(null, username);

        return token;

    }



    public LoginOutDTO tokenRefresh(String token) {
//        DecodedJWT jwt =  AuthUtils.decodeToken(token);
        Payload<String> jwt = AuthUtils.decodeRsaToken(token);
        boolean overdue = AuthUtils.isOverdue(jwt.getExpiration());

        if (overdue) {
            throw BusinessException.builder().errMsg("token已经过期,请重新登录").build();
        }

        String uid = jwt.getId();

        Session session = null;

        try {
            /**
             * 查找现有的session
             */
            session = sessionRepository.findById(uid);
        } catch (Exception e) {
//            e.printStackTrace();
            log.info("查找现有的session 失败，将创建一个新的");
        }
        if (null == session) {
            throw BusinessException.builder().errMsg("token已经过期,请重新登录").build();
        }
        String json = session.getAttribute(G_USER);
        if (StringUtils.isEmpty(json)) {
            throw BusinessException.builder().errMsg("token已经过期,请重新登录").build();

        }

        UserDTO dto = JsonUtil.jsonToPojo(json, UserDTO.class);
        if (null == dto) {
            throw BusinessException.builder().errMsg("token已经过期,请重新登录").build();
        }

        UserPO userPO = userDao.findByUserId(dto.getUserId());

        if (userPO == null) {
            throw BusinessException.builder().errMsg("token 令牌有误").build();
        }
/*
        String salt = userPO.getPassword();
        try
        {
            Algorithm algorithm = Algorithm.HMAC256(salt);
            JWTVerifier verifier = JWT.require(algorithm).withSubject(uid).build();
            verifier.verify(jwt.getToken());
        } catch (Exception e)
        {
            throw BusinessException.builder().errMsg("token 令牌有误").build();
        }*/



//        String salt = userPO.getPassword();
        // 构建JWT token
//        String token = AuthUtils.buildToken(sid, salt);
        String rsaToken = AuthUtils.buildRsaToken("no-sid", uid);

        /**
         * 将用户信息缓存起来
         */
        UserDTO cacheDto = new UserDTO();
        BeanUtils.copyProperties(userPO, cacheDto);
        cacheDto.setToken(rsaToken);

        LoginOutDTO outDTO = new LoginOutDTO();
        BeanUtils.copyProperties(cacheDto, outDTO);

        return outDTO;
    }
}
