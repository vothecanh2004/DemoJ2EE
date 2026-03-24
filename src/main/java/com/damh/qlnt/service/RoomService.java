package com.damh.qlnt.service;

import com.damh.qlnt.entity.Room;
import com.damh.qlnt.entity.User;
import java.util.List;
import java.util.Optional;

public interface RoomService {
    Room saveRoom(Room room);
    Optional<Room> getRoomById(Long id);
    List<Room> getAllRooms();
    List<Room> getRoomsByOwner(User owner);
    void deleteRoom(Long id);
    
    // User features
    void toggleFavorite(User user, Long roomId);
    List<Room> getFavoriteRooms(User user);
    
    // Admin features
    List<Room> getPendingRooms();
    void approveRoom(Long id);
    void rejectRoom(Long id, String reason);
    void hideRoom(Long id);

    List<Room> searchRooms(String kw, Double minP, Double maxP, Double minA);
}
