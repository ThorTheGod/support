package com.xidian.support.service;

import com.xidian.support.mapper.CommentMapper;
import com.xidian.support.mapper.UserMapper;
import com.xidian.support.pojo.Comment;
import com.xidian.support.pojo.Follow;
import com.xidian.support.pojo.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author thornoir
 * @date 2022/3/31
 * @apiNote
 */
@Service
public class CommentService {

    @Autowired
    private CommentMapper commentMapper;
    @Autowired
    private UserMapper userMapper;

    public void addComment(Comment comment){
        commentMapper.addComment(comment);
    }

    public void deleteComment(int commentId){
        commentMapper.deleteComment(commentId);
    }

    public void updateComment(Comment comment){
        commentMapper.updateComment(comment);
    }

    public Comment findCommentByCommentId(int commentId){
        return commentMapper.findCommentByCommentId(commentId);
    }


    public List<Comment> findCommentsByUserId(int userId){
        return commentMapper.findCommentsByUserId(userId);
    }

    public List<Map<String,Object>> findCommentsByPlanId(int planId){
        List<Map<String,Object>> commentUsers = new ArrayList<>();
        List<Comment> comments = commentMapper.findCommentsByPlanId(planId);
        User user = new User();
        for(Comment comment:comments){
            user = userMapper.findUserByUserId(comment.getUserId());
            Map<String,Object> commentUser = new HashMap<>();
            commentUser.put("user",user);
            commentUser.put("comment",comment);
            commentUsers.add(commentUser);
        }


        return commentUsers;
    }

    public List<Map<String,Object>> getAllComments(){
        List<Map<String,Object>> commentUsers = new ArrayList<>();
        List<Comment> comments = commentMapper.getAllComments();
        User user = new User();
        for(Comment comment:comments){
            user = userMapper.findUserByUserId(comment.getUserId());
            Map<String,Object> commentUser = new HashMap<>();
            commentUser.put("user",user);
            commentUser.put("comment",comment);
            commentUsers.add(commentUser);
        }

        return commentUsers;
    }


}
