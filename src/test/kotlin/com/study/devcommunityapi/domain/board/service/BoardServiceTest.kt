package com.study.devcommunityapi.domain.board.service

//import org.junit.jupiter.api.Assertions

import com.study.devcommunityapi.domain.board.dto.BoardRequestDto
import jakarta.transaction.Transactional
import org.assertj.core.api.Assertions
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest

@SpringBootTest
class BoardServiceTest @Autowired constructor(
    val boardService: BoardService
) {

    @Test
    @Transactional
    @DisplayName("보드 생성")
    fun createBoard() {

        val boardDto = BoardRequestDto(null, "board_test_2", true)
        val createdBoard = boardService.createBoard(boardDto)

        Assertions.assertThat(createdBoard!!.name).isEqualTo("board_test_2")
        Assertions.assertThat(createdBoard.usingStatus).isEqualTo(true)

    }

    @Test
    @Transactional
    @DisplayName("보드 조회")
    fun getBoard() {

        val boardDto = BoardRequestDto(null, "board_test", true)
        val createdBoard = boardService.createBoard(boardDto)

        val foundBoard = boardService.getBoard(createdBoard!!.id)

        Assertions.assertThat(foundBoard!!.name).isEqualTo("board_test")
        Assertions.assertThat(foundBoard.usingStatus).isEqualTo(true)

    }

    @Test
    @Transactional
    @DisplayName("보드 목록 조회")
    fun getAllBoards() {

        Assertions.assertThat(boardService.getAllBoards()!!.size).isEqualTo(6)

    }

    @Test
    @Transactional
    @DisplayName("보드 수정")
    fun updateBoard() {

        val boardDto = BoardRequestDto(null, "board_test", true)
        val createdBoard = boardService.createBoard(boardDto)

        val updatedBoard = boardService.updateBoard(
            createdBoard!!.id,
            BoardRequestDto(createdBoard.id, "board_update_test", false)
        )

        Assertions.assertThat(updatedBoard!!.name).isEqualTo("board_update_test")
        Assertions.assertThat(updatedBoard.usingStatus).isEqualTo(false)

    }

    @Test
    @Transactional
    @DisplayName("보드 삭제")
    fun deleteBoard() {



    }
}