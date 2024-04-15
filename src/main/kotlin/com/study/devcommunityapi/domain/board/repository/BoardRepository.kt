package com.study.devcommunityapi.domain.board.repository

import com.study.devcommunityapi.domain.board.entity.Board
import org.springframework.data.jpa.repository.JpaRepository

interface BoardRepository: JpaRepository<Board, Long>{

}