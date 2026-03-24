package com.damh.qlnt.service.impl;

import com.damh.qlnt.entity.ApprovalStatus;
import com.damh.qlnt.entity.FavoriteRoom;
import com.damh.qlnt.entity.Room;
import com.damh.qlnt.entity.User;
import com.damh.qlnt.repository.AppointmentRepository;
import com.damh.qlnt.repository.ContractRepository;
import com.damh.qlnt.repository.FavoriteRoomRepository;
import com.damh.qlnt.repository.RoomRepository;
import com.damh.qlnt.service.RoomService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class RoomServiceImpl implements RoomService {

    private final RoomRepository roomRepository;
    private final FavoriteRoomRepository favoriteRoomRepository;
    private final ContractRepository contractRepository;
    private final AppointmentRepository appointmentRepository;

    @Override
    public Room saveRoom(Room room) {
        return roomRepository.save(room);
    }

    @Override
    public Optional<Room> getRoomById(Long id) {
        return roomRepository.findById(id);
    }

    @Override
    public List<Room> getAllRooms() {
        return roomRepository.findAll();
    }

    @Override
    public List<Room> getRoomsByOwner(User owner) {
        return roomRepository.findByOwner(owner);
    }

    @Override
    @Transactional
    public void deleteRoom(Long id) {
        Room room = roomRepository.findById(id).orElseThrow();
        
        // Manual Cascade Delete for child entities
        favoriteRoomRepository.deleteAll(favoriteRoomRepository.findByRoom(room));
        appointmentRepository.deleteAll(appointmentRepository.findByRoom(room));
        contractRepository.deleteAll(contractRepository.findByRoom(room));
        
        roomRepository.delete(room);
    }

    @Override
    @Transactional
    public void toggleFavorite(User user, Long roomId) {
        Room room = roomRepository.findById(roomId)
                .orElseThrow(() -> new IllegalArgumentException("Room not found"));
        if (favoriteRoomRepository.existsByUserAndRoom(user, room)) {
            favoriteRoomRepository.deleteByUserAndRoom(user, room);
        } else {
            favoriteRoomRepository.save(FavoriteRoom.builder().user(user).room(room).build());
        }
    }

    @Override
    public List<Room> getFavoriteRooms(User user) {
        return favoriteRoomRepository.findByUser(user).stream()
                .map(FavoriteRoom::getRoom)
                .collect(Collectors.toList());
    }

    @Override
    public List<Room> getPendingRooms() {
        return roomRepository.findAll().stream()
                .filter(r -> r.getApprovalStatus() == ApprovalStatus.PENDING)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public void approveRoom(Long id) {
        Room room = roomRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Room not found"));
        room.setApprovalStatus(ApprovalStatus.APPROVED);
        roomRepository.save(room);
    }

    @Override
    @Transactional
    public void rejectRoom(Long id, String reason) {
        Room room = roomRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Room not found"));
        room.setApprovalStatus(ApprovalStatus.REJECTED);
        room.setRejectionReason(reason);
        roomRepository.save(room);
    }

    @Override
    @Transactional
    public void hideRoom(Long id) {
        System.out.println(">>> Hiding Room ID: " + id);
        Room room = roomRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Room not found"));
        room.setApprovalStatus(ApprovalStatus.HIDDEN);
        roomRepository.save(room);
    }

    @Override
    @Transactional
    public void banRoom(Long id) {
        System.out.println(">>> Banning Room ID: " + id);
        Room room = roomRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Room not found"));
        room.setApprovalStatus(ApprovalStatus.BANNED);
        roomRepository.save(room);
    }

    @Override
    public List<Room> searchRooms(String kw, Double minP, Double maxP, Double minA) {
        return roomRepository.searchRooms(kw, minP, maxP, minA);
    }
}
