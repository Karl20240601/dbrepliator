package com.redis.datasource.config;



public class RedisHostConfig {
    public static  final  String CLUSTER_TYPE_STANDALOAN="standaloan";
    public static  final  String CLUSTER_TYPE_CLUSTER="cluster";
    public static  final  String CLUSTER_TYPE_SSENTINEL="ssentinel";

    private String hosts;
    private String tag;
    private String role;
    /**
     * standalone
     * redis_cluster
     * sentienl
     */
    private String clusterType;

    public String getHosts() {
        return hosts;
    }

    public void setHosts(String hosts) {
        this.hosts = hosts;
    }

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getClusterType() {
        return clusterType;
    }

    public void setClusterType(String clusterType) {
        this.clusterType = clusterType;
    }

    public boolean isStandaloan(){
        return CLUSTER_TYPE_STANDALOAN.equals(clusterType);
    }

    public boolean isRedisCluster(){
        return CLUSTER_TYPE_CLUSTER.equals(clusterType);
    }

    public boolean isSsentinel(){
        return CLUSTER_TYPE_SSENTINEL.equals(clusterType);
    }

}
