package com.xidian.support.mapper;

import com.xidian.support.pojo.Comment;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author thornoir
 * @date 2022/3/31
 * @apiNote
 */
@Repository
public interface CommentMapper {

    void addComment(Comment comment);

    void deleteComment(int commentId);

    void updateComment(Comment comment);

    Comment findCommentByCommentId(int commentId);

    List<Comment> findCommentsByUserId(int userId);

    List<Comment> findCommentsByPlanId(int PlanId);

    List<Comment> getAllComments();


}
