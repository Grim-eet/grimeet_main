package com.grimeet.grimeet.domain.websocket.draw_data.repository;

import com.grimeet.grimeet.domain.websocket.draw_data.entity.DrawDataDocument;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DrawDataMongoRepository extends MongoRepository<DrawDataDocument, String> {
}
