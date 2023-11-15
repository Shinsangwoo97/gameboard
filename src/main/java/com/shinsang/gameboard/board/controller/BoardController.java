package com.shinsang.gameboard.board.controller;


import com.shinsang.gameboard.board.dto.requestDto.CreateBoardRequestDto;
import com.shinsang.gameboard.board.dto.responseDto.BoardListResponseDto;
import com.shinsang.gameboard.board.service.BoardService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class BoardController {
    private final BoardService boardMainService;

    @PostMapping("/api/board/add")
    public ResponseEntity createBoard(@RequestBody CreateBoardRequestDto createBoardRequestDto){
        System.out.println("요청이 왔어요");
        return boardMainService.createBoard(createBoardRequestDto);
    }
    @GetMapping("/api/board/list")
    public List<BoardListResponseDto> getBoardList(){
        return boardMainService.getBoardList();
    }

    @PatchMapping("/api/board/list/{id}")
    public ResponseEntity updateBoard(@RequestBody CreateBoardRequestDto createBoardRequestDto,
                                      @PathVariable Long id){
        return boardMainService.updateBoard(createBoardRequestDto, id);
    }
    @DeleteMapping("/api/board/list/delete/{id}")
    public ResponseEntity deleteBoard(@PathVariable Long id,
                                      @RequestBody CreateBoardRequestDto createBoardRequestDto){
        return boardMainService.deleteBoard(createBoardRequestDto, id);
    }

}
