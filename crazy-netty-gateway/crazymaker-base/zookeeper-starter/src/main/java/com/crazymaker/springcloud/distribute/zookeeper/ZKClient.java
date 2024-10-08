package com.crazymaker.springcloud.distribute.zookeeper;

import com.crazymaker.springcloud.standard.context.SpringContextUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.utils.CloseableUtils;
import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.data.Stat;

import java.util.ArrayList;
import java.util.List;

/**
 * build by 尼恩 @ 疯狂创客圈
 **/
@Slf4j
public class ZKClient {
    public static final int DEPEND_DELAY = 5;
    private static ZKClient singleton = null;

    public ZKClient(String zkAddress) {
        this.zkAddress = zkAddress;
    }

    private volatile CuratorFramework client;

    //Zk集群地址
    private String zkAddress = "127.0.0.1:2181";

    private ZKClient instance = null;


    private ZKClient() {

    }

    public static ZKClient getSingleton() {
        if (null == singleton) {
            singleton = (ZKClient) SpringContextUtil.getBean("zKClient");

        }
        return singleton;
    }

    public CuratorFramework getClient() {
        if (null != client) {
            return client;
        }
        init();
        return client;

    }

    public synchronized void init() {
        if (null != client) return;

        //创建客户端
        client = ClientFactory.createSimple(zkAddress);

        //启动客户端实例,连接服务器
        client.start();

    }

    public void destroy() {
        CloseableUtils.closeQuietly(client);
    }


    /**
     * 创建节点
     */
    public void createNode(String zkPath, String data) {
        try {
            // 创建一个 ZNode 节点
            // 节点的数据为 payload
            byte[] payload = "to set content".getBytes("UTF-8");
            if (data != null) {
                payload = data.getBytes("UTF-8");
            }
            client.create()
                    .creatingParentsIfNeeded()
                    .withMode(CreateMode.PERSISTENT)
                    .forPath(zkPath, payload);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 删除节点
     */
    public void deleteNode(String zkPath) {
        try {
            if (!isNodeExist(zkPath)) {
                return;
            }
            client.delete()
                    .forPath(zkPath);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    /**
     * 检查节点
     */
    public boolean isNodeExist(String zkPath) {
        try {

            Stat stat = client.checkExists().forPath(zkPath);
            if (null == stat) {
                log.info("节点不存在:", zkPath);
                return false;
            } else {

                log.info("节点存在 stat is:", stat.toString());
                return true;

            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * 创建 临时 顺序 节点
     */
    public String createEphemeralSeqNode(String pathPrefix) {
        try {

            // 创建一个 ZNode 节点
            String path = client.create()
                    .creatingParentsIfNeeded()
                    .withMode(CreateMode.EPHEMERAL_SEQUENTIAL)
                    .forPath(pathPrefix);

            return path;

        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 创建 临时 顺序 节点
     */
    public String createEphemeralNode(String pathPrefix, String subPath) {
        try {

            // 创建一个 ZNode 节点
            String path = client.create()
                    .creatingParentsIfNeeded()
                    .withMode(CreateMode.EPHEMERAL)
                    .forPath(pathPrefix + "/" + subPath);

            return path;

        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }


    //删除该路径
    public boolean deleteZnode(String path) {
        boolean b = false;

        //检测是否存在该路径。
        try {
            Void stat = client.delete().forPath(path);
            b = stat == null ? false : true;
        } catch (Exception e) {
            e.printStackTrace();
        }

        return b;
    }

    //获取子节点
    public List<String> getChildren(String path) {

        try {
            List<String> children = client.getChildren().forPath(path);
            return children;
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    //获取子节点
    public List<String> getChildrenData(String parentNode) {
        List<String> out = null;
        try {
            List<String> children = client.getChildren().forPath(parentNode);
            out = new ArrayList<>(children.size());
            for (String tmp : children) {
                String childNode = parentNode + "/" + tmp;
                String data = readZnode(childNode);
                out.add(data);
            }


            return out;
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    private String readZnode(String childNode) {
        try {
            //读取节点的数据
            byte[] payload = client.getData().forPath(childNode);

            String data = new String(payload, "UTF-8");
            log.debug("read data:", data);
            return data;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public void deleteWhenHasNoChildren(String path) {

        int index = path.lastIndexOf("/");

        String parent = path.substring(0, index);

        boolean exist = ZKClient.getSingleton().isNodeExist(parent);
        if (exist) {
            List<String> children = getChildren(parent);
            if (null != children && children.size() == 0) {
                deleteZnode(parent);
                log.info("删除空的 父节点:" + parent);

            }
        }
    }

    public boolean isReady() {

        if (null == singleton) {
            return false;
        }
        CuratorFramework client0 = getClient();
        return client0 != null;
    }

}
