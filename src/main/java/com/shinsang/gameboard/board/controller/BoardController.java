package com.shinsang.gameboard.board.controller;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.shinsang.gameboard.board.dto.requestDto.CreateBoardRequestDto;
import com.shinsang.gameboard.board.dto.responseDto.BoardListResponseDto;
import com.shinsang.gameboard.board.service.BoardService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class BoardController {
    private final BoardService boardMainService;
    @PostMapping("/api/board/add")
    public ResponseEntity createBoard(@RequestParam("image") MultipartFile imagefile, @RequestParam("post_data") String data) throws JsonProcessingException {
        return boardMainService.createBoard(imagefile, data);
    }

//    @GetMapping("/api/board/list")
//    public List<BoardListResponseDto> getBoardList(){
//        return boardMainService.getBoardList();
//    }
//
//    @PatchMapping("/api/board/list/{id}")
//    public ResponseEntity updateBoard(@RequestBody CreateBoardRequestDto createBoardRequestDto,
//                                      @PathVariable Long id){
//        return boardMainService.updateBoard(createBoardRequestDto, id);
//    }
//    @DeleteMapping("/api/board/list/delete/{id}")
//    public ResponseEntity deleteBoard(@PathVariable Long id,
//                                      @RequestBody CreateBoardRequestDto createBoardRequestDto){
//        return boardMainService.deleteBoard(createBoardRequestDto, id);
//    }

}
