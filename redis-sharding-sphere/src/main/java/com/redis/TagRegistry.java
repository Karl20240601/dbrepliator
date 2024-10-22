package com.redis;


import java.util.List;


public interface TagRegistry<T>  {
   T getTagNode(String tag);
   List<T> getTagNode(byte[] tag);
   List<T> getTagAllNode();
}
