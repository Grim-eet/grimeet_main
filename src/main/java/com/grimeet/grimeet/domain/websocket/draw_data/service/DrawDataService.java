package com.grimeet.grimeet.domain.websocket.drawData.service;

import com.grimeet.grimeet.domain.websocket.drawData.dto.DrawDataRequestDto;

public interface DrawDataService {
  void saveDrawData(DrawDataRequestDto requestDto, String userEmail);

}
