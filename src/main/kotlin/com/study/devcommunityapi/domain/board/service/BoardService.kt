package com.study.devcommunityapi.domain.board.service

import com.study.devcommunityapi.common.exception.NotFoundBoardException
import com.study.devcommunityapi.domain.board.dto.BoardRequestDto
import com.study.devcommunityapi.domain.board.dto.BoardResponseDto
import com.study.devcommunityapi.domain.board.entity.Board
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

    fun getBoardEntity(id: Long): Board {
        return boardRepository.findByIdOrNull(id) ?: throw NotFoundBoardException()
    }

    fun getAllBoards() : List<BoardResponseDto>? {
        return boardRepository.findAllByOrderByCreatedAt().stream().map { it.toResponseDto() }.toList()
    }

    fun updateBoard(id: Long, boardRequestDto: BoardRequestDto) : BoardResponseDto? {
        val foundBoard = boardRepository.findByIdOrNull(id) ?: throw NotFoundBoardException()

        foundBoard.name = boardRequestDto.name
        foundBoard.path = boardRequestDto.path
        foundBoard.usingStatus = boardRequestDto.usingStatus

        boardRepository.save(foundBoard)

        return foundBoard.toResponseDto()
    }

    fun deleteBoard(id: Long) {
        val foundBoard = boardRepository.findByIdOrNull(id)
        if (foundBoard != null) {
            boardRepository.delete(foundBoard)
        }
    }

}