package com.damh.qlnt.repository;

import com.damh.qlnt.entity.Room;
import com.damh.qlnt.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface RoomRepository extends JpaRepository<Room, Long> {
    @Query("SELECT r FROM Room r WHERE r.owner = :owner AND r.approvalStatus <> com.damh.qlnt.entity.ApprovalStatus.DELETED")
    List<Room> findByOwner(@Param("owner") User owner);

    List<Room> findAllByOrderByIdDesc();

    @Query("SELECT r FROM Room r WHERE r.approvalStatus = com.damh.qlnt.entity.ApprovalStatus.APPROVED " +
           "AND r.status = com.damh.qlnt.entity.RoomStatus.AVAILABLE " +
           "AND (:kw IS NULL OR LOWER(r.title) LIKE LOWER(CONCAT('%', :kw, '%')) OR LOWER(r.address) LIKE LOWER(CONCAT('%', :kw, '%'))) " +
           "AND (:minP IS NULL OR r.price >= :minP) " +
           "AND (:maxP IS NULL OR r.price <= :maxP) " +
           "AND (:minA IS NULL OR r.area >= :minA)")
    List<Room> searchRooms(@Param("kw") String keyword, 
                          @Param("minP") Double minPrice, 
                          @Param("maxP") Double maxPrice, 
                          @Param("minA") Double minArea);
}
