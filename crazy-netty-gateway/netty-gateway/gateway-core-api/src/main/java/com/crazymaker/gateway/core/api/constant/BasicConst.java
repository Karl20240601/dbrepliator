package com.crazymaker.gateway.core.api.constant;

import java.util.regex.Pattern;

public interface BasicConst {
    String META_DATA_KEY = "meta";


    int ROUTER_FILTER_ORDER = Integer.MAX_VALUE;


    String DEFAULT_CHARSET = "UTF-8";

    String PATH_SEPARATOR = "/";

    String PATH_PATTERN = "/**";

    String QUESTION_SEPARATOR = "?";

    String ASTERISK_SEPARATOR = "*";

    String AND_SEPARATOR = "&";

    String EQUAL_SEPARATOR = "=";

    String BLANK_SEPARATOR_1 = "";

    String BLANK_SEPARATOR_2 = " ";

    String COMMA_SEPARATOR = ",";

    String SEMICOLON_SEPARATOR = ";";

    String DOLLAR_SEPARATOR = "$";

    String PIPELINE_SEPARATOR = "|";

    String BAR_SEPARATOR = "-";

    String COLON_SEPARATOR = ":";

    String DIT_SEPARATOR = ".";

    String HTTP_PREFIX_SEPARATOR = "http://";

    String HTTPS_PREFIX_SEPARATOR = "https://";

    String HTTP_FORWARD_SEPARATOR = "X-Forwarded-For";

    Pattern PARAM_PATTERN = Pattern.compile("\\{(.*?)\\}");

    String DATE_FORMAT = "yyyy-MM-dd HH:mm:ss";

    String ENABLE = "Y";

    String DISABLE = "N";

    //前缀匹配
    String PREFIX_PATH_MATCHER = "prefix";
    //ant风格的路径匹配就是通过独特的匹配符简化路径匹配。
    String ANT_PATH_MATCHER = "ant_path";


    String BALANCE_STRATEGY_RANDOM = "random";
    String BALANCE_STRATEGY_ROUND_ROBIN = "round_robin";


    String USER_AUTH_RULE_CONFIG = "user_auth";

}