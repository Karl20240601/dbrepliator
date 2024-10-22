package com.redis.router;

public class Contanst {
    public static  final byte READ=1;
    public static  final byte WRITE=2;
    public static  final byte EXEC=4;
    public static  final byte ALL=READ|WRITE|EXEC;
    public static  final byte MASTER=READ|WRITE|EXEC;
    public static  final byte SLAVE=READ|EXEC;
    public static  final byte PEER=READ|WRITE|EXEC;
}
