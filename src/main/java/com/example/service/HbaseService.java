package com.example.service;

import com.example.controller.HbaseController;
import com.example.entity.ExceptionEntity;
import com.example.entity.UrlEntity;
import com.example.hbase.HbaseUtil;
import com.example.util.DateUtil;
import com.example.util.MyUtil;
import org.apache.hadoop.hbase.Cell;
import org.apache.hadoop.hbase.client.Result;
import org.apache.hadoop.hbase.util.Bytes;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.unit.DataUnit;

import javax.xml.crypto.Data;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * @author zhanghongjian
 * @Date 2019/4/11 16:59
 * @Description
 */

@Service
public class HbaseService {


    private static String tableName = "zjxxw";


    public List getAllYear(String type) {
        String startRow = "2018";
        String stopRow = DateUtil.getNextYear("yyyy");
        List<Result> results = HbaseUtil.getManyRecord(tableName, startRow, stopRow, "year", type + "All");
        List<String> list = new ArrayList<>();
        for (Result result : results) {
            byte[] row = result.getRow();
            String rowdata = Bytes.toString(row);
            String yearData = HbaseUtil.getResultValue(result, "year", type + "All");
            list.add(rowdata + ":" + yearData);
        }
        return list;
    }

    public List getAllMonth(String year, String type) {
        String startRow = year + "01";
        String stopRow = year + "13";
        System.out.println(stopRow);
        List<Result> results = HbaseUtil.getManyRecord(tableName, startRow, stopRow, "month", type + "All");
        List<String> list = new ArrayList<>();
        for (Result result : results) {
            byte[] row = result.getRow();
            String rowdata = Bytes.toString(row);
            String yearData = HbaseUtil.getResultValue(result, "month", type + "All");
            System.out.println("yearData    "+yearData);
            yearData = yearData.split(":")[1];
            yearData = yearData.replace(")", "").replace("]", "");
            String date = DateUtil.changeFormat("yyyymm", "yyyy-mm", rowdata);
            list.add(date + ":" + yearData);
        }
        return list;
    }

    public List getAllDay(String month, String type) {
        month = DateUtil.changeFormat("yyyy-mm", "yyyymm", month);
        String startRow = month + "01";
        String stopRow = month + "32";
        System.out.println(stopRow);
        List<Result> results = HbaseUtil.getManyRecord(tableName, startRow, stopRow, "day", type);
        List<String> list = new ArrayList<>();
        for (Result result : results) {
            String yearData = HbaseUtil.getResultValue(result, "day", type);
            String date = yearData.split(":")[0];
            yearData = yearData.split(":")[1];
            list.add(MyUtil.removeBracket(date) + ":" + MyUtil.removeBracket(yearData));
        }
        return list;
    }

    /*
     * @date: 2019/4/12
     * @Desc: 获得一天的pv uv ip
     */
    public List getOnedayData(String date) {
        String row = DateUtil.changeFormat("yyyy-mm-DD", "yyyymmDD", date);
        System.out.println(row);
        List<Object> list = new ArrayList<>();
        Result result = HbaseUtil.getOneRecord(tableName, row, "day");
        String pv = HbaseUtil.getResultValue(result, "day", "pv");
        String uv = HbaseUtil.getResultValue(result, "day", "uv");
        String ip = HbaseUtil.getResultValue(result, "day", "ip");
        list.add(pv);
        list.add(uv);
        list.add(ip);
        return list;
    }

    /*
     * @date: 2019/4/12
     * @Desc: 获取按月统计的平均每天访问量
     */
    public String getDayAvgforMonth(String month, String type) {
        String row = DateUtil.changeFormat("yyyy-mm", "yyyymm", month);
        Result result = HbaseUtil.getOneRecord(tableName, row, "month", type + "Avg");
        String avgdata = HbaseUtil.getResultValue(result, "month", type + "Avg");
        return avgdata;

    }

    /*
     * @date: 2019/4/12
     * @Desc: 获取按年统计的平均每天访问量
     */
    public String getDayAvgforYear(String year, String type) {
        Result result = HbaseUtil.getOneRecord(tableName, year, "year", type + "DayAvg");
        String avgdata = HbaseUtil.getResultValue(result, "year", type + "DayAvg");
        return avgdata;

    }

    /*
     * @date: 2019/4/12
     * @Desc: 获取按年统计的平均每月访问量
     */
    public String getMonthAvgforYear(String year, String type) {
        Result result = HbaseUtil.getOneRecord(tableName, year, "year", type + "MonthAvg");
        String avgdata = HbaseUtil.getResultValue(result, "year", type + "MonthAvg");
        return avgdata;

    }

    /*
     * @date: 2019/4/12
     * @Desc: 获取当月中最高访问量那天的数据  * 算法待重新考究  注意格式不能为MM 只能为mm
     */
    public String getQPSData(String month) {
        String row = DateUtil.changeFormat("yyyy-mm", "yyyymm", month);
        Result result = HbaseUtil.getOneRecord(tableName, row, "month", "qps");
        String qps = HbaseUtil.getResultValue(result, "month", "qps");
        return qps;
    }


    /*
     * @date: 2019/4/12
     * @Desc: 获取日访问量
     */
    public String getHourofDay(String date, String type) {
        String row = DateUtil.changeFormat("yyyy-mm-dd", "yyyymmdd", date);
        Result result = HbaseUtil.getOneRecord(tableName, row, "hour", type);
        String hourData = HbaseUtil.getResultValue(result, "hour", type);
        return hourData;
    }

    /*
     * @date: 2019/4/12
     * @Desc: 获取每日十大访问量页面
     */
    public List getTop10url(String date) {
        String row = DateUtil.changeFormat("yyyy-mm-dd", "yyyymmdd", date);
        Result result = HbaseUtil.getOneRecord(tableName, row, "url", "sort");
        String exceptionrank = HbaseUtil.getResultValue(result, "url", "sort");
        String[] ranks = exceptionrank.split("\\)");
        List<UrlEntity> list = new ArrayList<>();
        for (String rank : ranks
        ) {
            String lineData = MyUtil.removeBracket(rank);
            String[] datas = lineData.split("\":");
            UrlEntity entity = new UrlEntity();
            if(datas.length<2){
                continue;
            }
            entity.setUrl(datas[0].replace("\"","").replace(" ",""));
            entity.setFre(datas[1]);
            list.add(entity);
        }
        return list;
    }

    /*
     * @date: 2019/4/12
     * @Desc: 获取每天top10请求时长
     */
    public List getTop10requestTime(String date) {
        String row = DateUtil.changeFormat("yyyy-mm-dd", "yyyymmdd", date);
        List list = getResultList(row, "request_time", "lelay");
        return list;
    }
    /*
     * @date: 2019/4/12
     * @Desc: 获取 每天 device 统计
     */

    public List getDeviceList(String date) {
        String row = DateUtil.changeFormat("yyyy-mm-dd", "yyyymmdd", date);
        List list = getResultList(row, "device", "all");
        return list;
    }

    /*
     * @date: 2019/4/12
     * @Desc:  获得浏览器访问情况
     */
    public List getBrowsernfo(String date) {
        String row = DateUtil.changeFormat("yyyy-mm-dd", "yyyymmdd", date);
        List list = getResultList(row, "browser", "all");
        return list;
    }


    /*
     * @date: 2019/4/12
     * @Desc:  获得PC端浏览器访问量
     */
    public List getPCBrowserInfo(String date){
        String row = DateUtil.changeFormat("yyyy-mm-dd", "yyyymmdd", date);
        List list = getResultList(row, "browser", "pc");
        return list;
    }


    /*
     * @date: 2019/4/12
     * @Desc:  获得手机端端浏览器访问量
     */
    public List getMobileBrowserInfo(String date){
        String row = DateUtil.changeFormat("yyyy-mm-dd", "yyyymmdd", date);
        List list = getResultList(row, "browser", "mobile");
        return list;
    }

    /*
    * @date: 2019/4/12
    * @Desc:
    */
    public List getResultList(String row, String colFamily, String column) {
        Result result = HbaseUtil.getOneRecord(tableName, row, colFamily, column);
        String alldata = HbaseUtil.getResultValue(result, colFamily, column);
        String[] browsers = alldata.split("\\)");
        List<String> list = new ArrayList<>();
        for (String browser : browsers) {
            String linedata = (MyUtil.removeBracket(browser)).trim();
            if(linedata.equals("")){
                continue;
            }
            list.add(linedata);
        }
        return list;
    }


}
