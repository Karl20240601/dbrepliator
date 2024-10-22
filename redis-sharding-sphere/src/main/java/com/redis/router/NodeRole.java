package com.redis.router;

public enum  NodeRole {
    MASTER("master",Contanst.MASTER),
    SLAVE("slave",Contanst.SLAVE),
    PEER("peer",Contanst.PEER);
    NodeRole(String roleName, byte permesion) {
        this.roleName = roleName;
        this.permesion = permesion;
    }

    public String roleName;
    /**
     *
     */
    public byte permesion;

    public boolean support(byte operation){
        return (this.permesion&operation)==operation;
    }
}
