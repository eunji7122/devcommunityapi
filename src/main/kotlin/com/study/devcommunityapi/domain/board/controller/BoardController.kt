package com.study.devcommunityapi.domain.board.controller

import com.study.devcommunityapi.common.util.dto.BaseResponseDto
import com.study.devcommunityapi.domain.board.dto.BoardRequestDto
import com.study.devcommunityapi.domain.board.dto.BoardResponseDto
import com.study.devcommunityapi.domain.board.service.BoardService
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/boards")
class BoardController(
    private val boardService: BoardService
) {

    @GetMapping("/list")
    fun getAllBoards(): BaseResponseDto<List<BoardResponseDto>> {
        val boards = boardService.getAllBoards()
        return BaseResponseDto(data = boards)
    }

    @PostMapping()
    fun createBoard(@RequestBody boardRequestDto: BoardRequestDto): BaseResponseDto<BoardResponseDto> {
        val createdBoard = boardService.createBoard(boardRequestDto)
        return BaseResponseDto(data = createdBoard)
    }

    @GetMapping("/{id}")
    fun getBoard(@PathVariable id: Long): BaseResponseDto<BoardResponseDto> {
        val board = boardService.getBoard(id)
        return BaseResponseDto(data = board)
    }

    @PutMapping("/{id}")
    fun updateBoard(@PathVariable id: Long, @RequestBody boardRequestDto: BoardRequestDto): BaseResponseDto<BoardResponseDto> {
        val updatedBoard = boardService.updateBoard(id, boardRequestDto)
        return BaseResponseDto(data = updatedBoard)
    }

    @DeleteMapping("/{id}")
    fun deleteBoard(@PathVariable id: Long) {
        boardService.deleteBoard(id)
    }

}