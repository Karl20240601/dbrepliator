package com.redis.router;

import com.redis.RedisMethodContext;
import com.redis.TagRedisConnectionNode;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class DefaultRouterChain implements RouterChain {
    private List<Router> routers = new ArrayList<>();

    @Override
    public void addRouter(Router router) {
        routers.addAll(routers);
    }

    @Override
    public void addRouter(List<Router> routerList) {
        routers.addAll(routerList);
        Collections.sort(this.routers, new Comparator<Router>() {
            @Override
            public int compare(Router o1, Router o2) {
                return Integer.valueOf(o1.getOrder()).compareTo(o2.getOrder());
            }
        });
    }

    @Override
    public List<TagRedisConnectionNode> route(List<TagRedisConnectionNode> list, RedisMethodContext redisMethodContext) {
        List<TagRedisConnectionNode> routeList  = list;
        for(Router router :this.routers){
            if(CollectionUtils.isEmpty(routeList)){
                return null;
            }
            routeList = router.route(routeList, redisMethodContext);
        }
        return routeList;
    }
}
