package com.stepupbackend.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.stepupbackend.domain.Board;

public interface BoardRepository extends JpaRepository<Board, Long> {

    List<Board> findByCategoryOrderByIdDesc(String category);

    @Query("""
            select b from Board b left join b.author author
            where b.category <> :noticeCategory
              and (:category is null or b.category = :category)
              and (
                    :keyword is null
                    or (:searchType = 'title' and lower(b.title) like lower(concat('%', :keyword, '%')))
                    or (:searchType = 'content' and lower(b.content) like lower(concat('%', :keyword, '%')))
                    or (:searchType = 'author' and lower(author.id) like lower(concat('%', :keyword, '%')))
                  )
            """)
    Page<Board> findRegularBoards(
            @Param("noticeCategory") String noticeCategory,
            @Param("category") String category,
            @Param("searchType") String searchType,
            @Param("keyword") String keyword,
            Pageable pageable);
}
