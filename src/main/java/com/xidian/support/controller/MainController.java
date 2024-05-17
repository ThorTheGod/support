package com.xidian.support.controller;

import com.xidian.support.mapper.FollowMapper;
import com.xidian.support.pojo.Comment;
import com.xidian.support.pojo.Follow;
import com.xidian.support.pojo.Plan;
import com.xidian.support.pojo.User;
import com.xidian.support.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.servlet.server.Session;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpSession;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * @author thornoir
 * @date 2022/3/29
 * @apiNote
 */
@Controller
public class MainController {

    @Autowired
    private PlanService planService;
    @Autowired
    private ScoreService scoreService;
    @Autowired
    private UserService userService;
    @Autowired
    private CommentService commentService;
    @Autowired
    private InfoService infoService;
    @Autowired
    private FollowMapper followMapper;
    @Autowired
    private FollowService followService;

    private static final SimpleDateFormat sdf =  new SimpleDateFormat("yyyy-MM-dd");

    @RequestMapping("/main")
    public String toMain(HttpSession session, Model model){
        User user = (User)session.getAttribute("user");
//        if(user.getUserId()==null){
//            return "login";
//        }
        model.addAttribute("commentUsers",commentService.getAllComments());
        model.addAttribute("planScores",planService.getAllPlanAndFollow(user));
        return "main";
    }

    @RequestMapping("/follow/{userId1}")
    public String follow(@PathVariable("userId1")int userId1,Model model,HttpSession session){
        User user = (User)session.getAttribute("user");
        if(user.getUserId()==null){//游客
            return "main";
        }else if(userId1==user.getUserId()){//不能关注自己
            return "main";
        }
        Follow follow = followMapper.findFollowByUserId12(userId1,user.getUserId());
        if(follow!=null){//已关注则取关
            followMapper.cancelFollow(follow.getFollowId());
        }else{//未关注则关注
            followMapper.addFollow(new Follow(null,userId1,user.getUserId()));
        }

        model.addAttribute("commentUsers",commentService.getAllComments());
        model.addAttribute("planScores",planService.getAllPlanAndFollow(user));
        return "main";
    }

    @RequestMapping("/cancelFollow/{id}")
    public String cancelFollow(@PathVariable("id")int followId,Model model,HttpSession session){
        User user = (User)session.getAttribute("user");
        followMapper.cancelFollow(followId);

        model.addAttribute("user",user);
        model.addAttribute("comments", commentService.findCommentsByUserId(user.getUserId()));
        model.addAttribute("planScores",planService.findPlanByUserId(user.getUserId()));
        model.addAttribute("followUsers",followService.getFollowUsers(user.getUserId()));
        return "personal";
    }

    @RequestMapping("/like")
    @ResponseBody
    public String like(Integer id,int index){
        Comment comment = commentService.findCommentByCommentId(id);
        if(index==1){//发起点赞
            System.out.println("点赞");
            comment.setThumb(comment.getThumb()+1);
        }else{
            System.out.println("取消点赞");
            comment.setThumb(comment.getThumb()-1);
        }

        commentService.updateComment(comment);
        return "";
    }


    @RequestMapping("/match")
    public String toMatch(HttpSession session){
        return "match";
    }

    @RequestMapping("/info")
    public String toInfo(HttpSession session,Model model){
//        User user = (User)session.getAttribute("user");
//        if(user.getUserId()==null){
//            return "login";
//        }
        List<Map<String,Object>> infoUsers = infoService.getAllInfo();
        model.addAttribute("infoUsers",infoUsers);
        return "info";
    }

    @RequestMapping("/news")
    public String toNews(HttpSession session){
//        User user = (User)session.getAttribute("user");
//        if(user==null){
//            return "login";
//        }
        return "news";
    }

    @RequestMapping("/plan/{id}")
    public String toPlan(@PathVariable("id")int planId,Model model,HttpSession session){
        List<Map<String,Object>> res = planService.findPlanByPlanId(planId);
        List<Map<String,Object>> commentUsers =commentService.findCommentsByPlanId(planId);
        model.addAttribute("commentUsers",commentUsers);
        session.setAttribute("planId",planId);
        model.addAttribute("PlanScore",res);
        return "plan";
    }

    @RequestMapping("/plan")
    public String toPlan(HttpSession session,Model model){
        Integer planId = (Integer) session.getAttribute("planId");
        if(planId!=null){
            List<Map<String,Object>> res = planService.findPlanByPlanId(planId);
            List<Map<String,Object>> commentUsers =commentService.findCommentsByPlanId(planId);
            model.addAttribute("PlanScore",res);
            model.addAttribute("commentUsers",commentUsers);
            return "plan";
        }else{
            return "match";
        }
    }

    @RequestMapping("/personal")
    public String toPersonal(HttpSession session,Model model){
        User user = (User) session.getAttribute("user");
        if(user.getUserId()!=null){
            User user2 = userService.findUserByUserId(user.getUserId());
            model.addAttribute("user",user2);
            if(user.getPriority()==0){
                model.addAttribute("users",userService.getAllUser());
                model.addAttribute("planScores",planService.getAllPlan());
                model.addAttribute("infoUsers",infoService.getAllInfo());
                return "manage";
            }else{
                model.addAttribute("comments", commentService.findCommentsByUserId(user.getUserId()));
                model.addAttribute("planScores",planService.findPlanByUserId(user.getUserId()));
                model.addAttribute("followUsers",followService.getFollowUsers(user.getUserId()));
                return "personal";
            }
        }else{
            return "login";
        }
    }

    @RequestMapping("/personal/{id}")
    public String rootToPersonal(@PathVariable("id")int userId,Model model,HttpSession session){
        User user = userService.findUserByUserId(userId);
        model.addAttribute("user",user);
        session.setAttribute("user",user);
        List<Map<String,Object>> planScores = planService.findPlanByUserId(userId);
        List<Comment> comments = commentService.findCommentsByUserId(userId);
        model.addAttribute("comments",comments);
        model.addAttribute("planScores",planScores);
        return "personal";
    }

    @RequestMapping("/deleteUser/{id}")
    public String deleteUser(@PathVariable("id")int userId,Model model,HttpSession session){
        userService.deleteUser(userId);

        model.addAttribute("users",userService.getAllUser());
        model.addAttribute("planScores",planService.getAllPlan());

        return "manage";
    }

    @RequestMapping("/search")
    public String SearchPlan(String preTime,String sufTime,Integer planId,Model model){
        if(planId!=null){
            List<Map<String,Object>> plans = planService.findPlanByPlanId(planId);
            plans.get(0).put("sim","--");
            model.addAttribute("PlanScores",plans);
        }else{
            //由于SimpleDateFormat无法直接识别和转换 星期几 月份 日 年的格式，因此先用Date转换成时间戳，再用SimpleDateFormat将时间戳转换为指定格式
//            Long preDate = new Date(preTime).getTime();
//            Long sufDate = new Date(sufTime).getTime();
//            String pre = sdf.format(new Date(Long.parseLong(String.valueOf(preDate))));
//            String suf = sdf.format(new Date(Long.parseLong(String.valueOf(sufDate))));
            List<Map<String,Object>> plans = planService.findPlanByTime(preTime,sufTime);
            for (Map<String,Object> plan:plans) {
                plan.put("sim","--");
            }
            model.addAttribute("PlanScores",plans);
        }
        return "match";
    }


    @RequestMapping("/matchplan")
    public String matchPlan(Model model,Plan plan,String period,double similarity){
        System.out.println("Plan:"+plan+","+"period:"+period+","+"sim:"+similarity);
        List<Map<String,Object>> planSims = planService.match(plan,period,similarity);
        model.addAttribute("PlanScores",planSims);
        return "match";
    }

}
