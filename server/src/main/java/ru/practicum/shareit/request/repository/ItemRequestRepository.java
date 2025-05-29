package ru.practicum.shareit.request.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import ru.practicum.shareit.request.model.ItemRequest;

import java.util.List;
import java.util.Optional;

public interface ItemRequestRepository extends JpaRepository<ItemRequest, Long> {
    @Query("SELECT DISTINCT r FROM ItemRequest r " +
            "LEFT JOIN FETCH r.items " +
            "WHERE r.requestor.id = :userId " +
            "ORDER BY r.created DESC")
    List<ItemRequest> findAllRequestsByUserId(@Param("userId") Long userId);

    @Query("SELECT DISTINCT r FROM ItemRequest r " +
            "LEFT JOIN FETCH r.items " +
            "WHERE r.requestor.id != :userId " +
            "ORDER BY r.created DESC")
    List<ItemRequest> findAllOtherUsersRequests(@Param("userId") Long userId);

    @Query("SELECT DISTINCT r FROM ItemRequest r " +
            "LEFT JOIN FETCH r.items " +
            "WHERE r.id = :id")
    Optional<ItemRequest> findByItemRequestId(@Param("id") Long id);
}
