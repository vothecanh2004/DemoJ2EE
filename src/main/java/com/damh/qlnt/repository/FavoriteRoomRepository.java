package com.damh.qlnt.repository;

import com.damh.qlnt.entity.FavoriteRoom;
import com.damh.qlnt.entity.Room;
import com.damh.qlnt.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface FavoriteRoomRepository extends JpaRepository<FavoriteRoom, Long> {
    List<FavoriteRoom> findByUser(User user);
    boolean existsByUserAndRoom(User user, Room room);
    void deleteByUserAndRoom(User user, Room room);
}
