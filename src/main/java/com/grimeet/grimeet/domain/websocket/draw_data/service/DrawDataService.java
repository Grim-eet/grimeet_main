package com.grimeet.grimeet.domain.websocket.draw_data.service;

import com.grimeet.grimeet.domain.websocket.draw_data.dto.DrawDataRequestDto;

public interface DrawDataService {
  void saveDrawData(DrawDataRequestDto requestDto, String userEmail);

}
