package com.linkedlist;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * @author sunwj
 *
 * LinkedHashMap 天然的LRU
 */
public class LinkedHashMapLRU {
    public static void main(String[] args) {
        //accessOrder为true表示按照访问顺序排序
        LinkedHashMap<String, String> map = new LinkedHashMap<String, String>(5, 0.75F, true) {
            @Override
            protected boolean removeEldestEntry(Map.Entry<String, String> eldest) {
                //当LinkHashMap的容量大于5的时候,再插入就移除旧的元素
                return this.size() > 5;
            }
        };
        map.put("aa", "aa");
        map.put("bb", "bb");
        map.put("cc", "cc");
        map.put("dd", "dd");
        map.put("ee", "ee");
        map.keySet().iterator().forEachRemaining(System.out::println);
        System.out.println("===================================");
        map.get("ee");
        map.get("aa");
        map.keySet().iterator().forEachRemaining(System.out::println);
        System.out.println("====================================");
        map.put("ff","ff");
        map.keySet().iterator().forEachRemaining(System.out::println);
    }
}
