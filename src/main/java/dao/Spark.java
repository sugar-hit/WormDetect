package dao;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaSparkContext;

public class Spark {
    private static String sparkRunMode = "local";                              // Default setting to local.
    private static String sparkAppname = "SparkES";                            // Default setting to SparkES.
    private static String sparkBrodacastCompress = "true";                     // Default OPEN to speed up the plugin.
    private static String sparkRDDCompress = "true";                           // Default OPEN to speed up the plugin.
    private static String sparkIOCompressJarSourcePath = "org.apache.spark.io.LZFCompressionCodec";
    private static String sparkShuffleFileBuffer = "1280k";
    private static String sparkReducerMaxSizeInFlight = "1024m";
    private static String sparkReducerMaxMblnFlight = "1024m";
    private static SparkConf sparkConf;
    private static JavaSparkContext sc;                                        // Only one SparkContext over-all.

    /**
     * static blog keep class default setting always run when it joint into jvm.
     */
    static {
        // Default setting run.
        setSparkConf();
    }

    // Spark Configuration and Getting functions:

    /**
     * setSparkConf()
     * 设定spark设置项为默认项的函数
     */
    private static void setSparkConf() {
        sparkConf = new SparkConf();
        sparkConf.setMaster(sparkRunMode);
        sparkConf.setAppName(sparkAppname);
        sparkConf.set("spark.rdd.compress", sparkRDDCompress);
        sparkConf.set("spark.broadcast.compress", sparkBrodacastCompress);
        sparkConf.set("spark.io.compression.codec", sparkIOCompressJarSourcePath);
        sparkConf.set("spark.shuffle.file.buffer", sparkShuffleFileBuffer);
        sparkConf.set("spark.reducer.maxSizeInFlight", sparkReducerMaxSizeInFlight);
        sparkConf.set("spark.reducer.maxMblnFlight", sparkReducerMaxMblnFlight);
    }


    public static void appendSparkConf(String configKey, int configValue) {
        appendSparkConf(configKey, Integer.toString(configValue));
    }

    public static void appendSparkConf(String configKey, Long configValue) {
        appendSparkConf(configKey, Long.toString(configValue));
    }

    /**
     * appendSparkConf()
     * 在已有的Spark配置项中增加其他配置项。配置项的内容以 K-V 对的形式传入。
     * @param configKey 配置项目名
     * @param configValue 配置项目量
     */
    public static void appendSparkConf(String configKey, String configValue) {
        if (sparkConf == null)
            setSparkConf();
        if (configKey == null || configValue == null)
            return;
        sparkConf.set(configKey, configValue);
    }

    /**
     * 获取spark配置项变量
     * @return spark配置项 (SparkConf 类型)
     */
    public static SparkConf getSparkConf() {
        if (sparkConf == null)
            setSparkConf();
        return sparkConf;
    }

    /**
     * setLogLevel()
     * 设置spark的日志打印等级，为了保证日志整洁，这里默认关闭全部spark日志
     * @param sc: spark内容操作主体
     */
    private static void setLogLevel(JavaSparkContext sc) {
//        String configFileValue = Config.getSparkNoticeLevel().toLowerCase();
        String log_level = "off";
        /**
         * @value:   configFileValue
         * off : close all notices
         * info : showing all information
         * error : showing out any errors but no these execute information
         * DEFAULT: info
         * **/
//        if (log_level.equals("off")) {
            sc.setLogLevel("OFF");
            Logger.getLogger("org").setLevel(Level.OFF);
            Logger.getLogger("org.apache.spark").setLevel(Level.OFF);
            Logger.getLogger("org.apache.eclipse.jetty.server").setLevel(Level.OFF);
            return ;
//        }

//        if (log_level.equals("error")) {
//            sc.setLogLevel("ERROR");
//            return ;
//        }
//
//        sc.setLogLevel("INFO");
    }

    /**
     * getSparkContext()
     * 获取全局唯一的Spark Context，如果没有spark context实例，则生成后返回实例。
     * @return spark context实例(JavaSparkContext类型)
     * @return spark context实例(JavaSparkContext类型)
     */
    public static JavaSparkContext getSparkContext() {
        if (sc == null) {
            sc = new JavaSparkContext(getSparkConf());
            setLogLevel(sc);
        }
        return sc;
    }

}
