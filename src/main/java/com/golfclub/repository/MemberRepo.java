package com.golfclub.repository;

import com.golfclub.model.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface MemberRepo extends JpaRepository<Member, Long> {
    Optional<Member> findByMemberEmail(String email);
    Optional<Member> findByMemberPhone(String phone);
    List<Member> findByMemberNameContainingIgnoreCase(String name);
    List<Member> findByMemberPhoneContaining(String phonePartial);
    List<Member> findByStatus(Member.MembershipStatus status);
    List<Member> findByStartDateBetween(LocalDate start, LocalDate end);
    List<Member> findByTotalTournamentsPlayedGreaterThan(Integer count);
    List<Member> findByTotalWinningsGreaterThan(Double amount);

    @Query("SELECT m FROM Member m JOIN m.tournaments t WHERE t.id = :tournamentId")
    List<Member> findMembersByTournamentId(@Param("tournamentId") Long tournamentId);

    @Query("SELECT m FROM Member m JOIN m.tournaments t WHERE t.startDate = :date")
    List<Member> findByTournamentStartDate(@Param("date") LocalDate date);

    @Query("SELECT m FROM Member m WHERE m.startDate <= :date AND DATEADD(MONTH, m.duration, m.startDate) > :date")
    List<Member> findActiveMembers(@Param("date") LocalDate date);

    @Query("SELECT m FROM Member m WHERE m.status = 'ACTIVE' ORDER BY m.totalTournamentsPlayed DESC")
    List<Member> findTopParticipants();
}