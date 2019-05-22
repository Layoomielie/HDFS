package com.example.controller;

import com.example.service.HbaseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.ArrayList;
import java.util.List;

/**
 * @author zhanghongjian
 * @Date 2019/4/11 16:59
 * @Description
 */

@ResponseBody
@Controller
@RequestMapping("/zjxxw")
public class HbaseController {

    @Autowired
    HbaseService hbaseService;

    @RequestMapping("/year/all")
    public List getAllYearPv(@RequestParam("type")String type){
        List list = hbaseService.getAllYear(type);
        return list;
    }

    @RequestMapping("/month")
    public List getAllMonth(@RequestParam("year") String year,@RequestParam("type")String type){
        List list = hbaseService.getAllMonth(year,type);
        return list;
    }

    @RequestMapping("/day")
    public List getAllDay(@RequestParam("month")String month,@RequestParam("type")String type){
        List list = hbaseService.getAllDay(month,type);
        return list;
    }

    @RequestMapping("/day/data")
    public List getOnedayData(@RequestParam("date")String date){
        List list = hbaseService.getOnedayData(date);
        return list;
    }

    @RequestMapping("/month/qps")
    public String getMonthQPSData(@RequestParam("date")String date){
        String data = hbaseService.getQPSData(date);
        return data;
    }

    @RequestMapping("/month/dayAvg")
    public String getDayAvgforMonth(@RequestParam("date")String date,@RequestParam("type")String type){
        String data = hbaseService.getDayAvgforMonth(date,type);
        return data;
    }

    @RequestMapping("/year/dayAvg")
    public String getDayAvgforYear(@RequestParam("date")String date,@RequestParam("type")String type){
        String data = hbaseService.getDayAvgforYear(date,type);
        return data;
    }

    @RequestMapping("/year/monthAvg")
    public String getMonthAvgforYear(@RequestParam("date")String date,@RequestParam("type")String type){
        String data = hbaseService.getMonthAvgforYear(date,type);
        return data;
    }

    @RequestMapping("/hour/data")
    public String getHourofDay(@RequestParam("date")String date,@RequestParam("type")String type){
        String data = hbaseService.getHourofDay(date,type);
        return data;
    }

    @RequestMapping("/url/sort")
    public List getTop10pv(@RequestParam("date") String date){
        List list = hbaseService.getTop10url(date);
        return list;
    }

    @RequestMapping("/requestTime/rank")
    public List getTop10requestTime(@RequestParam("date") String date){
        List list = hbaseService.getTop10requestTime(date);
        return list;
    }

    @RequestMapping("/device/all")
    public List getDeviceList(@RequestParam("date") String date){
        List list = hbaseService.getDeviceList(date);
        return list;
    }

    @RequestMapping("/browser/all")
    public List getBrowserInfo(@RequestParam("date") String date){
        List list = hbaseService.getBrowsernfo(date);
        return list;
    }

    @RequestMapping("/browser/pc")
    public List getPCBrowserInfo(@RequestParam("date") String date){
        List list = hbaseService.getPCBrowserInfo(date);
        return list;
    }

    @RequestMapping("/browser/mobile")
    public List getMobileBrowserInfo(@RequestParam("date") String date){
        List list = hbaseService.getMobileBrowserInfo(date);
        return list;
    }

    @RequestMapping("/area")
    public void getAreaInfo(){
        System.out.println("area");
    }
}
