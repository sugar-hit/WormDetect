package mode.detect;

import com.alibaba.fastjson.JSONObject;
import dao.Redis;
import dao.graph.aggregation.AggregationList;
import dao.graph.path.PathRecorder;
import mode.learn.output.GraphInfo;
import utils.Config;
import utils.FileController;
import utils.Time;
import utils.parse.JavaParser;

import java.util.*;

public class Alerter {
    /**
     *   Alerter 类
     *   该类是检测模式(Detect Mode)中用于发送报警信息的类。
     *   report方法用于向指定的redis表插入一条报警记录
     *   convertJsonString方法用于将有用的报警信息(数据)转换成json语句
     *
     *   报警功能在检测中使用，检测依赖spark操作，所以本类需要实现序列化接口
     *   如果有其他警报方式，可以直接在本类中进行扩展
     */

    public void report(Redis redis, HashMap<String, String> hashMap, Long start, Long end) {
        if (hashMap == null)
            return;
        if (hashMap.isEmpty())
            return;
// Redis 操作不好查看
        System.out.println("Redis report");
        StringBuilder srcIP = new StringBuilder();
        StringBuilder outDegree = new StringBuilder();
        StringBuilder routes = new StringBuilder();
        if (hashMap.get("SourceIP") != null)
            srcIP.append(hashMap.get("SourceIP"));
        if (hashMap.get("OutDegree") != null)
            outDegree.append(hashMap.get("OutDegree"));
        if (hashMap.get("Routes") != null)
            routes.append(hashMap.get("Routes"));
        report(redis, srcIP.toString(), outDegree.toString(), routes.toString(), start, end);
    }

   /**
     * report方法用于向指定的redis表插入一条报警记录
     */

    private void report(Redis redis, String srcIP, String outdegree, String routes, Long start, Long end) {
        if (redis == null)
            return;
        if (srcIP == null)
            return;
        if (outdegree == null)
            return;
        if (routes == null)
            return;
        JSONObject jsonBean = new JSONObject();
        jsonBean.put("i_mod", "ad");
        jsonBean.put("i_type", "wormdetect");
        jsonBean.put("i_start_time", Time.dateTimeFormat(start));
        jsonBean.put("i_end_time", Time.dateTimeFormat(end));
        jsonBean.put("i_src", srcIP);
        jsonBean.put("i_outdegree", outdegree);
        jsonBean.put("i_routes", routes);
//        redis.insertRedisList("ad_log", "ad_log_wormdetect|" + jsonBean.toJSONString());
        System.out.println("ad_log_wormdetect|" + jsonBean.toJSONString());
    }


}
