package com.study.devcommunityapi.domain.board.service

import com.study.devcommunityapi.domain.board.dto.BoardRequestDto
import com.study.devcommunityapi.domain.board.dto.BoardResponseDto
import com.study.devcommunityapi.domain.board.repository.BoardRepository
import jakarta.transaction.Transactional
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service

@Service
@Transactional
class BoardService(
    private val boardRepository: BoardRepository,
) {

    fun createBoard(boardRequestDto: BoardRequestDto) : BoardResponseDto? {
        return boardRepository.save(boardRequestDto.toEntity()).toResponseDto()
    }

    fun getBoard(id: Long) : BoardResponseDto? {
        val foundBoard = boardRepository.findByIdOrNull(id)
        return foundBoard?.toResponseDto()
    }

    fun getAllBoards() : List<BoardResponseDto>? {
        return boardRepository.findAll().stream().map { it.toResponseDto() }.toList()
    }

    fun updateBoard(id: Long, boardResponseDto: BoardResponseDto) : BoardResponseDto? {
        val foundBoard = boardRepository.findByIdOrNull(id)

        if (foundBoard != null) {
            foundBoard.name = boardResponseDto.name
            foundBoard.usingStatus = boardResponseDto.usingStatus
            boardRepository.save(foundBoard)
            return foundBoard.toResponseDto()
        }

        return null
    }

    fun deleteBoard(id: Long) {
        val foundBoard = boardRepository.findByIdOrNull(id)
        if (foundBoard != null) {
            boardRepository.delete(foundBoard)
        }
    }

}