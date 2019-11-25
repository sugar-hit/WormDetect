package util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;

public class Config {
    private static String configFileAddress = "D:\\Project\\2020\\WormDetect\\config.cfg"; // 调试用
//    private static String configFilePath = "/root/napp/ad_wormdetect/config.cfg";
    private static String configFileCoding = "UTF-8";
    private static Long contiousQueneLengthLimit ;
    private static Long radialNeighbourLimit ;

    /**
     * retrieve()
     * 根据提供的key值查询配置文件对应项。如果不存在返回null值。
     * 考虑配置文件读取频次，不对外部类提供该方法接口，获取对应的配置信息时应该在本类中设置对应的公有方法并调用该方法
     * @param key 配置文件中的key
     * @return 返回配置文件key对应的值，如果没有查询到，返回null.
     */
    synchronized private static String readFile(String key) {
        if (key == null)
            return null;

        if (configFileAddress == null) {
            return null;
        }

        File file = new File(configFileAddress);
        BufferedReader bufferedReader = null;
        try {
            bufferedReader = new BufferedReader(new FileReader(file));
        } catch (FileNotFoundException e) {
            return null;
        }
        String str = null;
        String keyFromFile = null;
        int cutter = 0;
        try {
            while (null != (str = bufferedReader.readLine())) {
                cutter = str.indexOf("=");
                if (cutter <= 0)
                    continue;
                keyFromFile = str.substring(0, cutter);
                if (keyFromFile.equals(key)) {
                    bufferedReader.close();
                    return str.substring(cutter + 1);
                }
            }
            bufferedReader.close();
        } catch (Exception e) {
            return null;
        }
        return null;
    }

    /**
     * get()
     * 获取某个配置项的值，如果为空则返回空值
     * @param key 所要获取的配置项名key
     * @return 所要获取的配置项内容（String类型）
     */
    synchronized private static String get(String key) {
        return readFile(key);
    }

    /**
     * get()
     * 获取某个配置项的值，如果配置项被设置为空则填充默认设定，以此保护系统能够在设置项缺省时正常运行。
     * @param key 所要获取的配置项名key
     * @param defaultValue 默认设定
     * @return 所要获取的配置项内容（或者是默认的配置项，String类型）
     */
    synchronized private static String get(String key, String defaultValue) {
        if (get(key) != null)
            return get(key);
        return defaultValue;
    }

    synchronized private static String get(String key, int defaultValue) {
        if (get(key) != null)
            return get(key);
        return Integer.toString(defaultValue);
    }

    synchronized private static String get(String key, double defaultValue) {
        if (get(key) != null)
            return get(key);
        return Double.toString(defaultValue);
    }

    public static String getTimeInterval () {
        return get("time_interval", 300);
    }

    public static String getElasticSearchIP () {
        return get("elasticsearch_ip", "10.245.142.213");
    }

    public static String getElasticSearchPort () {
        return get("elasticsearch_port", 9200);
    }

    public static String getElasticSearchUser () {
        return get("elasticsearch_user");
    }

    public static String getElasticSearchPass () {
        return get("elasticsearch_pass");
    }

    public static String getRedisIP () {
        return get("redis_ip", "10.245.142.213");
    }

    public static String getRedisPort () {
        return get("redis_port", 6380);
    }

    public static String getRedisMaxTotal () {
        return get("redis_max_total", 20);
    }

    public static String getRedisMaxIdle () {
        return get("redis_max_idle",15);
    }

    public static String getWebSocketIP () {
        return get("websocket_ip", "10.245.142.213");
    }

    public static String getWebSocketPort () {
        return get("websocket_port", 20000);
    }

    public static String getLearningModeSavingPath () {
//        return get("LearningMode_FileDir", "/root/napp/ad_cycledetect/rule");
        return get("LearningMode_FileDir", "D:\\Project\\2020\\dig-lib"); // For test only.
    }

    public static Long getContiousQueneLength () {
        if (contiousQueneLengthLimit == null)
            contiousQueneLengthLimit = Long.parseLong(get("continuous_quene_length_limit", 5));
        return contiousQueneLengthLimit;
    }

    public static Long setContiousQueneLength (Long value) {
        contiousQueneLengthLimit = value;
        return contiousQueneLengthLimit;
    }

    public static Long getRadialNeighbourLimit () {
        if (radialNeighbourLimit == null)
            radialNeighbourLimit = Long.parseLong(get("radial_neighbor_limit", 5));
        return radialNeighbourLimit;
    }

    public static Long setRadialNeighbourLimit (Long value) {
        radialNeighbourLimit = value;
        return radialNeighbourLimit;
    }

    public static double getExpansionRatio () {
        return Double.parseDouble(get("expansion_ratio", 1.5));
    }

}
