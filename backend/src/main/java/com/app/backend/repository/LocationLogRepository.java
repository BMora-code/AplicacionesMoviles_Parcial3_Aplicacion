package com.app.backend.repository;

import com.app.backend.model.LocationLog;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface LocationLogRepository extends MongoRepository<LocationLog, String> {
    List<LocationLog> findByUserId(String userId);
}
