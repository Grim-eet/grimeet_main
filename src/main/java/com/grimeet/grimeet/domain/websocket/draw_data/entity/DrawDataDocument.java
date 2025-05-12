package com.grimeet.grimeet.domain.websocket.draw_data.entity;

import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.util.List;
import java.time.LocalDateTime;

@Document(collection = "draw_data")
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Tag(name = "DrawDataDocument", description = "실시간 드로잉 좌표 Document")
public class DrawDataDocument {

  @Id
  @Schema(description = "MongoDB 자동 생성 ID")
  private String id;

  @Field(name = "project_id")
  @Schema(description = "프로젝트 ID", example = "project123")
  private String projectId;

  @Field(name = "user_id")
  @Schema(description = "유저 ID", example = "1")
  private long userId;

  @Field(name = "timestamp")
  @Schema(description = "좌표 저장 시간", example = "2025-05-02T13:30:00")
  private LocalDateTime timestamp;

  @Field(name = "coordinates")
  @Schema(description = "좌표 리스트")
  private List<Coordinate> coordinates;

  @Getter
  @Builder
  @AllArgsConstructor
  @NoArgsConstructor
  @Schema(description = "단일 좌표 정보")
  public static class Coordinate {
    @Field(name = "x")
    @Schema(description = "x 좌표", example = "153")
    private double x;

    @Field(name = "y")
    @Schema(description = "y 좌표", example = "342")
    private double y;

    @Field(name = "color")
    @Schema(description = "색상 (HEX)", example = "#000000")
    private String color;

    @Field(name = "stroke")
    @Schema(description = "붓의 두께", example = "3")
    private int stroke;

    @Field(name = "tool")
    @Schema(description = "도구 종류", example = "pen")
    private String tool;

    @Field(name = "timestamp")
    @Schema(description = "이 좌표의 시간(ms)", example = "1682847300000")
    private long timestamp;
  }
}

