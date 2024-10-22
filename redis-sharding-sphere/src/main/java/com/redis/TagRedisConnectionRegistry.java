package com.redis;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
public class TagRedisConnectionRegistry implements TagRegistry<TagRedisConnectionNode>  {
   private final Map<String,TagRedisConnectionNode> tagRedisConnectionNodeMap = new HashMap<>();

   @Override
   public TagRedisConnectionNode getTagNode(String tag) {
      return tagRedisConnectionNodeMap.get(tag);
   }

   @Override
   public List<TagRedisConnectionNode> getTagNode(byte[] tag) {
      return null;
   }

   @Override
   public List<TagRedisConnectionNode> getTagAllNode() {
      return null;
   }
}
