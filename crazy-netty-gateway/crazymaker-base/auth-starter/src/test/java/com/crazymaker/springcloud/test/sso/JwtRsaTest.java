package com.crazymaker.springcloud.test.sso;

import com.crazymaker.springcloud.base.auth.Payload;
import com.crazymaker.springcloud.base.auth.RsaUtils;
import com.crazymaker.springcloud.base.service.impl.JwtServiceImpl;
import com.crazymaker.springcloud.common.dto.UserDTO;
import com.crazymaker.springcloud.common.util.JsonUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.codec.binary.StringUtils;
import org.junit.Test;

import java.security.PrivateKey;
import java.security.PublicKey;

/*

调试之前，修改自己的 key的目录
        String base= "D:/dev/push-mid-platform/message-push-platform/rsa-key/";


如果报错：WARNING: Could not open/create prefs root node Software\JavaSoft\Prefs at root 0x80000002. Windows RegCreateKeyEx(...) returned error code 5.
解决方案

Step1：搜索并运行regedit.exe

Step2: 进入HKEY_LOCAL_MACHINE\SOFTWARE\JavaSoft，右击JavaSoft目录，选择新建->项（key），命名为Prefs

计算机\HKEY_LOCAL_MACHINE\SOFTWARE\JavaSoft
Step3: 重新编译即可。
**/

@Slf4j

public class JwtRsaTest {
    private String privateKeyFileName = "id_key_rsa";
    private String publicKeyFileName = "id_key_rsa.pub";

    @Test
    public void testGenKeys() throws Exception {

        String base = getBase();

//        FileUtil.createParentPath(new File(base + publicKeyFileName));
        RsaUtils.generateKey(base + publicKeyFileName,
                base + privateKeyFileName,
                "cartisan", 1024);
    }

    private String getBase() {
        String base = "D:/dev/push-mid-platform/message-push-platform/rsa-key/";
      /* String base = getClass().getResource("/").getPath();

        if (OsUtil.isWindows()) {
            //去掉前面的 /
            base = base.substring(1);
            System.out.println("base = " + base);
        }*/
        return base;
    }


    @Test
    public void testGenToken() throws Exception {
        String base = getBase();
        //获取当前认证的对象
        UserDTO userDTO = new UserDTO();
        userDTO.setUsername("username");
        userDTO.setUserId(10000L);
        //获取生成token的私钥
        PrivateKey privateKey = RsaUtils.getPrivateKey(base + privateKeyFileName);
        //定义token过期时间为一天
        int expireTime = 24 * 60;
        //生成token
        String token = JwtServiceImpl.buildJwtRsaToken(userDTO.getUserId(), privateKey, expireTime);
        System.out.println("token = " + token);
    }

    @Test
    public void testShowJWT() {
        try {
            String token = getOneToken();

            System.out.println(token);

            //以·点分割 编码后的令牌
            String[] parts = token.split("\\.");

            /**
             * 对第一部分和第二部分进行解码
             * 解码后的第一部分：header 头部
             */
            String headerJson;
            headerJson = StringUtils.newStringUtf8(Base64.decodeBase64(parts[0]));
            log.info("parts[0]=" + headerJson);
            // 解码后的第一部分输出的示例为：
            // parts[0]={"typ":"JWT","alg":"HS256"}

            /**
             * 解码后的第二部分：payload 负载
             */
            String payloadJson;
            payloadJson = StringUtils.newStringUtf8(Base64.decodeBase64(parts[1]));
            log.info("parts[1]=" + payloadJson);
            //输出的示例为：
            //解码后第二部分 parts[1]={"sub":"session id","exp":1579315535,"iat":1578451535}


        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private String getOneToken() throws Exception {
        String base = getBase();
        //获取当前认证的对象
        UserDTO userDTO = new UserDTO();
        userDTO.setUsername("username");
        userDTO.setUserId(10000L);
        //获取生成token的私钥
        PrivateKey privateKey = RsaUtils.getPrivateKey(base + privateKeyFileName);
        //定义token过期时间为一天
        int expireTime = 24 * 60;
        //生成token
        String token = JwtServiceImpl.buildJwtRsaToken(userDTO.getUserId(), privateKey, expireTime);
        System.out.println(token);

        return token;
    }


    @Test
    public void testCheckToken() throws Exception {
        //如果token格式正确，就验证token

        String token = getOneToken();
        //获取验证token的公钥
        String base = getBase();
        PublicKey publicKey = RsaUtils.getPublicKey(base + publicKeyFileName);

        Payload<String> payload = JwtServiceImpl.decodeJwtRsaToken(token, publicKey);

        String json = JsonUtil.pojoToJson(payload);

        System.err.println(json);
    }

    @Test
    public void testDecodeWrongToken() throws Exception {
        //如果token格式正确，就验证token

        String token =
                "eyJhbGciOiJSUzI1NiJ9.eyJzaWQiOiJOV0kxWWpaaVlUY3ROMkkxWmkwMFptTmhMV0V4WWpFdFpHTmxORGcxTmpCbE5tUm0iLCJqdGkiOiIxMDAwMCIsImV4cCI6MTY0OTU5MzE2MywiaWF0IjoxNjQ5NTYwNzYzfQ.WlS7J7ferQkZVqIfaUhC0kEVTIeDZEl_kJUkl9MKehty6eo-VqvkoHX_BxzKQrr-td8gy48-vXU0B8dTI4fTHzCgAMtlt5vPA_X2E6fr8ToY800kiAEbm-SEuSEAbtcOdoscCiBA7DJXJaQlVbqNd4eQKa92GEMV4yvvQ49WbqUKUZQdoal0S1KKW4u4W0MHxP0da2YI6SWaMouqsVV6UMXNTQn1akSD9FMfMtzP66s5znJoKOTQXZ9YHTlvcuMveUbeZOAs_50D5IGyURBHoyfXlK0Oexu9zxsAWIaIntFDTJrnJVq2sKozhR6M6NqzzEHFACaidGBzzI--ga_UJ1";
//                "eyJhbGciOiJSUzI1NiJ9.eyJzaWQiOiJOemMyWkRFMllUY3ROVFpsWlMwMFlUUXpMV0pqTkRjdFlUVmhNMlk0WmpreU5qTmwiLCJqdGkiOiIxMDAwMCIsImV4cCI6MTY0OTU4NjQzMSwiaWF0IjoxNjQ5NTU0MDMxfQ.su1cLHWchKpwFfMQIJEa9h99DmjAGUPIdaxleYTSQBQEOOccCk83adQSn1kjxp_x_2e546zsWpd5mYinm6OcCxQsZ1i7ORN6bxEHJc7AH0Lia1GIowod7d_tJOj6e3uxROwT86OUBUvvWF0RUndGv_ciQr5vsEFMnTozocZ5regU_HPJxUFT7baS3Uo-LMi5WxZjZOEKndww1CT5pg9GJZW0ntWT16bPSpcUKIWw8SyJc-cFPcV_RQtzSclYe0JSv3X6liEJbZCHKOnfI7pQCKX3hXJtUZqffsHCbn3k4l9SYL9w9buyu_dvO2sOMaN92gI9ebaiZAtiQtwWEqNmKe";
        //获取验证token的公钥
        String base = getBase();
        PublicKey publicKey = RsaUtils.getPublicKey(base + publicKeyFileName);

        Payload<String> payload = JwtServiceImpl.decodeJwtRsaToken(token, publicKey);

        String json = JsonUtil.pojoToJson(payload);

        System.err.println(json);

    }
}