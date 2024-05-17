package com.xidian.support.controller;

import com.xidian.support.pojo.Comment;
import com.xidian.support.pojo.Plan;
import com.xidian.support.pojo.User;
import com.xidian.support.service.CommentService;
import com.xidian.support.service.FollowService;
import com.xidian.support.service.PlanService;
import com.xidian.support.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpSession;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * @author thornoir
 * @date 2022/4/4
 * @apiNote
 */
@Controller
public class PlanController {

    @Autowired
    private PlanService planService;
    @Autowired
    private UserService userService;
    @Autowired
    private CommentService commentService;
    @Autowired
    private FollowService followService;

    private static final SimpleDateFormat sdf =  new SimpleDateFormat("yyyy-MM-dd");

    @RequestMapping ("/deletePlan/{id}")
    public String deletePlan(@PathVariable("id")int planId, Model model, HttpSession session){
        planService.deletePlan(planId);
        User user = (User)session.getAttribute("user");
        User user2 = userService.findUserByUserId(user.getUserId());
        List<Map<String,Object>> planScores = planService.findPlanByUserId(user.getUserId());
        model.addAttribute("user",user2);
        model.addAttribute("planScores",planScores);
        return "personal";
    }


    @RequestMapping("/addPlan")
    public String addPlan(Plan plan,Model model,HttpSession session) throws ParseException {
        plan.setPlanTime(sdf.parse(sdf.format(plan.getPlanTime())));
        planService.addPlan(plan);
        model.addAttribute("plans",planService.getAllPlan());

        return "main";
    }

    @RequestMapping("/updatePlan")
    public String toUpdate(Plan plan,Model model,HttpSession session){
        planService.updatePlan(plan);
        User user = (User)session.getAttribute("user");

        if(user.getPriority()==0){
            model.addAttribute("users",userService.getAllUser());
            model.addAttribute("planScores",planService.getAllPlan());
            return "manage";
        }
        User user2 = userService.findUserByUserId(user.getUserId());
        List<Map<String,Object>> planScores = planService.findPlanByUserId(user.getUserId());
        model.addAttribute("user",user2);
        model.addAttribute("planScores",planScores);
        return "personal";
    }

    @RequestMapping("/addComment")
    public String addComment(Comment comment,Model model,HttpSession session){
        Integer planId = (Integer) session.getAttribute("planId");
        if(planId!=null){
            commentService.addComment(comment);
            model.addAttribute("PlanScore",planService.findPlanByPlanId(planId));
            model.addAttribute("commentUsers",commentService.findCommentsByPlanId(planId));
            return "plan";
        }else{
            return "match";
        }
    }

    @RequestMapping("/searchCategory")
    public String searchCategory(String category,Model model){

        List<Map<String,Object>> plans = planService.findPlanByCategory(category);
        for (Map<String,Object> plan:plans) {
            plan.put("sim","--");
        }
        model.addAttribute("PlanScores",plans);
        return "match";
    }


    @RequestMapping("/updateComment")
    public String updateComment(Comment comment,Model model,HttpSession session){
        User user = (User)session.getAttribute("user");
        commentService.updateComment(comment);
        model.addAttribute("user",user);
        model.addAttribute("comments", commentService.findCommentsByUserId(user.getUserId()));
        model.addAttribute("planScores",planService.findPlanByUserId(user.getUserId()));
        model.addAttribute("followUsers",followService.getFollowUsers(user.getUserId()));
        return "personal";
    }

}
