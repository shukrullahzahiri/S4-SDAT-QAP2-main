package com.golfclub.repository;

import com.golfclub.model.Tournament;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.time.LocalDate;
import java.util.List;

public interface TournamentRepo extends JpaRepository<Tournament, Long> {
    @Query("SELECT DISTINCT t FROM Tournament t LEFT JOIN FETCH t.participatingMembers WHERE t.status = :status")
    List<Tournament> findByStatus(@Param("status") Tournament.TournamentStatus status);

    @Query("SELECT DISTINCT t FROM Tournament t LEFT JOIN FETCH t.participatingMembers WHERE t.location LIKE %:location%")
    List<Tournament> findByLocationContainingIgnoreCase(@Param("location") String location);

    @Query("SELECT DISTINCT t FROM Tournament t LEFT JOIN FETCH t.participatingMembers WHERE t.startDate BETWEEN :startDate AND :endDate")
    List<Tournament> findByStartDateBetween(@Param("startDate") LocalDate startDate, @Param("endDate") LocalDate endDate);

    @Query("SELECT DISTINCT t FROM Tournament t LEFT JOIN FETCH t.participatingMembers")
    List<Tournament> findAllWithMembers();

    @Query("SELECT SUM(t.entryFee * SIZE(t.participatingMembers)) FROM Tournament t WHERE t.status = 'COMPLETED'")
    Double calculateTotalRevenue();

    @Query("SELECT DISTINCT t FROM Tournament t LEFT JOIN FETCH t.participatingMembers WHERE t.startDate <= :date AND t.endDate >= :date")
    List<Tournament> findCurrentTournaments(@Param("date") LocalDate date);

    @Query("SELECT DISTINCT t FROM Tournament t LEFT JOIN FETCH t.participatingMembers WHERE SIZE(t.participatingMembers) < t.maximumParticipants AND t.status = 'SCHEDULED'")
    List<Tournament> findAvailableTournaments();

    @Query("SELECT t FROM Tournament t WHERE t.cashPrizeAmount >= :minPrize")
    List<Tournament> findByMinimumPrize(@Param("minPrize") Double minPrize);

    @Query("SELECT t FROM Tournament t WHERE t.entryFee <= :maxFee")
    List<Tournament> findByMaximumEntryFee(@Param("maxFee") Double maxFee);

    @Query("SELECT t FROM Tournament t WHERE SIZE(t.participatingMembers) >= :minCount")
    List<Tournament> findByMinimumParticipants(@Param("minCount") Integer minCount);

    @Query("SELECT t FROM Tournament t WHERE t.status = 'SCHEDULED' AND t.startDate > :date ORDER BY t.startDate ASC")
    List<Tournament> findUpcomingTournaments(@Param("date") LocalDate date);

    @Query("SELECT t FROM Tournament t WHERE t.status = 'COMPLETED' ORDER BY t.endDate DESC")
    List<Tournament> findRecentlyCompletedTournaments();
}