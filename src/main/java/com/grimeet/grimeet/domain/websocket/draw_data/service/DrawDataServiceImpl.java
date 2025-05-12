package com.grimeet.grimeet.domain.websocket.draw_data.service;

import com.grimeet.grimeet.common.exception.ExceptionStatus;
import com.grimeet.grimeet.common.exception.GrimeetException;
import com.grimeet.grimeet.domain.user.dto.UserResponseDto;
import com.grimeet.grimeet.domain.user.service.UserFacade;
import com.grimeet.grimeet.domain.websocket.draw_data.dto.DrawDataRequestDto;
import com.grimeet.grimeet.domain.websocket.draw_data.entity.DrawDataDocument;
import com.grimeet.grimeet.domain.websocket.draw_data.repository.DrawDataMongoRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.stream.Collectors;
import java.util.List;

@Slf4j
@Transactional
@Service
@RequiredArgsConstructor
public class DrawDataServiceImpl implements DrawDataService {

    private final DrawDataMongoRepository drawDataMongoRepository;
    private final UserFacade userFacade;

    @Override
    public void saveDrawData(DrawDataRequestDto dto, String userEmail) {
      UserResponseDto findUser = userFacade.findUserByEmail(userEmail);

      DrawDataDocument document = DrawDataDocument.builder()
              .projectId(dto.getProjectId())
              .userId(findUser.getId())
              .timestamp(LocalDateTime.now())
              .coordinates(toCoordinates(dto))
              .build();

      drawDataMongoRepository.save(document);
      log.info("✅ [DrawData Service] Draw data 저장 완료: projectId={}, userId={}", dto.getProjectId(), userEmail);
    }

  private List<DrawDataDocument.Coordinate> toCoordinates(DrawDataRequestDto dto) {
    return dto.getCoordinates().stream()
            .map(c -> {
              long timestamp;
              try {
                timestamp = Long.parseLong(c.getTimestamp());
              } catch (NumberFormatException e) {
                log.warn("❌ timestamp 파싱 실패: {}", c.getTimestamp(), e);
                throw new GrimeetException(ExceptionStatus.INVALID_TIMESTAMP_FORMAT);
              }
              return DrawDataDocument.Coordinate.builder()
                      .x(c.getX())
                      .y(c.getY())
                      .color(c.getColor())
                      .stroke(c.getStroke())
                      .tool(c.getTool())
                      .timestamp(timestamp)
                      .build();
            })
            .toList();
  }


}
