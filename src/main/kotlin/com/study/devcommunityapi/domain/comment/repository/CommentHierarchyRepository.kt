package com.study.devcommunityapi.domain.comment.repository

import com.study.devcommunityapi.domain.comment.entity.CommentHierarchy
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Modifying
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param

interface CommentHierarchyRepository: JpaRepository<CommentHierarchy, Long> {

    //
    @Modifying
    @Query(value ="INSERT INTO comment_hierarchy (ancestor_comment_id, depth, descendant_comment_id) " +
            "SELECT ancestor_comment_id, depth + 1, :descendantCommentId " +
            "FROM comment_hierarchy " +
            "WHERE descendant_comment_id = :ancestorCommentId ", nativeQuery = true)
    fun insertCommentHierarchy(@Param("ancestorCommentId") ancestorCommentId: Long, @Param("descendantCommentId") descendantCommentId: Long)

    // union 활용 쿼리
    //    insert into comment_hierarchy (main_comment_id, sub_comment_id, depth)
    //    select main_comment_id, 78, depth + 1
    //    from comment_hierarchy
    //    where sub_comment_id = 77
    //    union all
    //    select 78, 78, 0

}