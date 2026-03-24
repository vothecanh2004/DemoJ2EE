package com.damh.qlnt.repository;

import com.damh.qlnt.entity.Message;
import com.damh.qlnt.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface MessageRepository extends JpaRepository<Message, Long> {
    List<Message> findBySenderOrReceiverOrderBySentAtDesc(User sender, User receiver);
}
