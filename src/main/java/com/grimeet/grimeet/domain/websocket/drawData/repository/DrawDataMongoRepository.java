package com.grimeet.grimeet.domain.websocket.drawData.repository;

import com.grimeet.grimeet.domain.websocket.drawData.entity.DrawDataDocument;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DrawDataMongoRepository extends MongoRepository<DrawDataDocument, String> {
}
