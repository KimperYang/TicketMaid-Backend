package com.whu.ticket.controller;

import com.whu.ticket.annotation.AdminLogin;
import com.whu.ticket.annotation.UserLogin;
import com.whu.ticket.pojo.Event;
import com.whu.ticket.pojo.Result;
import com.whu.ticket.service.EventService;
import com.whu.ticket.util.JwtUtil;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;


@RestController
@RequestMapping("/event")
public class EventController {
    private  static final Logger log = LoggerFactory.getLogger(EventController.class);

    @Autowired
    EventService eventService;

    private Date parseTime(String strTime) throws ParseException {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        if(StringUtils.isBlank(strTime)) return null;
        else return sdf.parse(strTime);
    }

    @AdminLogin
    @PostMapping("/add")
    public Result addEvent(HttpServletRequest request)  {
        String name = request.getParameter("name");
        String token = request.getHeader("access_token");
        int host_id = JwtUtil.getUserID(token);
        int quota = Integer.parseInt(request.getParameter("quota"));
        String strTime = request.getParameter("time");
        String strPrice = request.getParameter("price");
        String location = request.getParameter("location");
        String info = request.getParameter("info");
        String cover = request.getParameter("cover");
        Date time;
        double price;
        try {
            time = parseTime(strTime);
            price = Double.parseDouble(strPrice);
        } catch (ParseException e) {
            return new Result(-1, null, e.getMessage());
        }
        Event event = new Event();
        event.setName(name);
        event.setHost_id(host_id);
        event.setQuota(quota);
        event.setTime(time);
        event.setLocation(location);
        event.setInfo(info);
        event.setCover(cover);
        event.setPrice(price);
        try{
            eventService.addEvent(event);
            return new Result(0,null,"添加活动成功");
        }catch(Exception e){
            log.info("addEvent fail");
            return new Result(-1,null,e.getMessage());
        }
    }

    @AdminLogin
    @DeleteMapping("/remove")
    public Result deleteEvent(HttpServletRequest request) {
        int id = Integer.parseInt(request.getParameter("id"));
        String token = request.getHeader("access_token");
        int host_id = JwtUtil.getUserID(token);
        try {
            eventService.deleteEvent(id,host_id);
            return new Result(0, null, "删除活动成功");
        } catch (Exception e) {
            log.info("deleteEvent fail");
            return new Result(0, null, e.getMessage());
        }
    }


    @GetMapping("/query")
    public Result queryEvent(HttpServletRequest request){
        int pageNo = Integer.parseInt(request.getParameter("pageNo"));
        int pageSize = Integer.parseInt(request.getParameter("pageSize"));
        Date time = new Date();
        eventService.queryEvent(time,pageNo,pageSize);
        return new Result(0,eventService.queryEvent(time,pageNo,pageSize),"查询成功");
    }

    @AdminLogin
    @PutMapping("/modify")
    public Result modifyEventProfile(HttpServletRequest request) {
        int newQuota = Integer.parseInt(request.getParameter("quota"));
        String token = request.getHeader("access_token");
        int host_id = JwtUtil.getUserID(token);
        int id = Integer.parseInt(request.getParameter("id"));
        String strTime = request.getParameter("time");
        String strPrice = request.getParameter("price");
        String newLocation = request.getParameter("location");
        String newInfo = request.getParameter("info");
        String newCover = request.getParameter("cover");
        Date newTime;
        double newPrice;
        try {
            newTime = parseTime(strTime);
            newPrice = Double.parseDouble(strPrice);
        } catch (ParseException e) {
            return new Result(-1, null, e.getMessage());
        }
        Event newEvent = new Event();
        newEvent.setHost_id(host_id);
        newEvent.setId(id);
        newEvent.setQuota(newQuota);
        newEvent.setCover(newCover);
        newEvent.setInfo(newInfo);
        newEvent.setTime(newTime);
        newEvent.setPrice(newPrice);
        newEvent.setLocation(newLocation);
        eventService.modifyEvent(newEvent);
        return new Result(0,null,"修改活动信息成功");
    }

    @AdminLogin
    @GetMapping("/AdminQuery")
    public Result queryAdminEvent(HttpServletRequest request){
        int pageNo = Integer.parseInt(request.getParameter("pageNo"));
        int pageSize = Integer.parseInt(request.getParameter("pageSize"));
        String token = request.getHeader("access_token");
        int host_id = JwtUtil.getUserID(token);
        eventService.queryAdminEvent(host_id,pageNo,pageSize);
        return new Result(0,eventService.queryAdminEvent(host_id,pageNo,pageSize),"查询成功");
    }
}
