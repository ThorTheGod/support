package com.xidian.support.controller;

import com.xidian.support.mapper.PlanMapper;
import com.xidian.support.pojo.User;
import com.xidian.support.service.CommentService;
import com.xidian.support.service.PlanService;
import com.xidian.support.service.UserService;
import com.xidian.support.util.WordUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.*;
import java.net.URLEncoder;
import java.util.List;
import java.util.Map;

/**
 * @author thornoir
 * @date 2022/1/26
 * @apiNote
 */
@Controller
public class UserController {

    @Autowired
    private PlanService planService;
    @Autowired
    private UserService userService;
    @Autowired
    private PlanMapper planMapper;
    @Autowired
    private CommentService commentService;

    @GetMapping("/")
    public String toLogin(){
        return "login";
    }

    @PostMapping("/login")
    public String login(User user, HttpSession session, Model model){
        User user2 = userService.findUserByUsername(user.getUsername());
        if(user2!=null){
            if(user2.getPassword().equals(user.getPassword())){
                user2.setPassword("*");
                session.setAttribute("user",user2);
                model.addAttribute("commentUsers",commentService.getAllComments());
                model.addAttribute("planScores",planService.getAllPlanAndFollow(user));
                return "redirect:/main";
            }else{
                model.addAttribute("msg","密码错误!");
                return "login";
            }
        }else{
            model.addAttribute("msg","该用户不存在！");
            return "login";
        }
    }

    @PostMapping("/register")
    public String register(User user,Model model,HttpSession session){
        User user2 = userService.findUserByUsername(user.getUsername());
        if(user2==null){//不重名
            user.setHeadImg("bg_9.jpg");
            userService.addUser(user);
            user.setPassword("*");
            session.setAttribute("user",user);
            model.addAttribute("planScores",planService.getAllPlanAndFollow(user));
        }else{
            model.addAttribute("msg2","用户名重复！");
        }
        return "login";
    }

    @RequestMapping("/visit")
    public String visit(HttpSession session,Model model){
        User user = new User();
        user.setUsername("游客");
        user.setHeadImg("bg_9.jpg");
        session.setAttribute("user",user);
        model.addAttribute("planScores",planService.getAllPlanAndFollow(user));
        model.addAttribute("commentUsers",commentService.getAllComments());
        return "main";
    }

    @RequestMapping("/logout")
    public String logout(HttpSession session){
        session.removeAttribute("user");
        return "login";
    }


    @RequestMapping("/updateUser")
    public String updateUser(User user,Model model,HttpSession session){
        userService.updateUser(user);
        model.addAttribute("user",user);

//        user.setPassword("*");
        List<Map<String,Object>> planScores = planService.findPlanByUserId(user.getUserId());
        model.addAttribute("planScores",planScores);
        session.setAttribute("user",user);
        return "personal";
    }

    /**
     * 用户上传头像
     */
    @PostMapping("/updateHeadImg")
    public String updateHeadImg(MultipartFile file, Model model, HttpSession session) {
        User user = (User) session.getAttribute("user");
        User user2 = userService.findUserByUserId(user.getUserId());

        if(file.isEmpty()){
            model.addAttribute("user", user2);
            session.setAttribute("user",user2);
            List<Map<String,Object>> planScores = planService.findPlanByUserId(user.getUserId());
            model.addAttribute("planScores",planScores);
            System.out.println("文件为空");
            return "personal";
        }
        //保存图片的本地路径
        String path = System.getProperty("user.dir") + "/upload/";
        //获取图片的后缀类型
        String originalFilename = file.getOriginalFilename();
        String prefix = originalFilename.substring(originalFilename.lastIndexOf(".") + 1);
        //按理说应该统一为jpg格式
        if(!prefix.equals(".jpg")){

        }

        File realPath = new File(path);
        if (!realPath.exists()) {
            realPath.mkdir();
        }
        //文件名
        String filename = user.getUserId() + "".concat("-user");
        filename = filename.concat('.' + prefix);
        user2.setHeadImg(filename);
        System.out.println(user2);
        //把本地文件上传到封装上传文件位置的全路径
        try {
            file.transferTo(new File(realPath + "/" + filename));
        } catch (IOException e) {
            e.printStackTrace();
        }
        userService.updateUser(user2);
        List<Map<String,Object>> planScores = planService.findPlanByUserId(user.getUserId());
        model.addAttribute("planScores",planScores);
        model.addAttribute("user", user2);
        session.setAttribute("user",user2);
        return "redirect:/personal";
    }



    @RequestMapping(value="/download")
    public String downloads(HttpServletResponse response , HttpServletRequest request,HttpSession session) throws Exception{
        //要下载的方案地址
        String path = System.getProperty("user.dir");
        //产生对应的word文档供下载
        Integer planId = (Integer) session.getAttribute("planId");
        WordUtil.genWord(planMapper.findPlanByPlanId(planId));
        path+="/output/";
        String  fileName = "plan.doc";

        //1、设置response 响应头
        response.reset(); //设置页面不缓存,清空buffer
        response.setCharacterEncoding("UTF-8"); //字符编码
        response.setContentType("multipart/form-data"); //二进制传输数据
        //设置响应头
        response.setHeader("Content-Disposition",
                "attachment;fileName="+ URLEncoder.encode(fileName, "UTF-8"));

        File file = new File(path,fileName);
        //2、 读取文件--输入流
        InputStream input=new FileInputStream(file);
        //3、 写出文件--输出流
        OutputStream out = response.getOutputStream();

        byte[] buff =new byte[1024];
        int index=0;
        //4、执行 写出操作
        while((index= input.read(buff))!= -1){
            out.write(buff, 0, index);
            out.flush();
        }
        out.close();
        input.close();
        return null;
    }




}
