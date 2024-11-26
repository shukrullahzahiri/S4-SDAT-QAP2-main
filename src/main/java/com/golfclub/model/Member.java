package com.golfclub.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "members", indexes = {
        @Index(name = "idx_member_email", columnList = "memberEmail"),
        @Index(name = "idx_member_phone", columnList = "memberPhone")
})
public class Member {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    @Pattern(regexp = "^[a-zA-Z\\s]{2,50}$")
    private String memberName;

    @NotBlank
    private String memberAddress;

    @Email
    @NotBlank
    @Column(unique = true)
    private String memberEmail;

    @Pattern(regexp = "^\\d{3}-\\d{3}-\\d{4}$")
    @Column(unique = true)
    private String memberPhone;

    @NotNull
    @PastOrPresent
    private LocalDate startDate;

    @Min(1)
    @Max(60)
    private Integer duration;

    @JsonIgnore
    @ManyToMany(mappedBy = "participatingMembers", fetch = FetchType.EAGER)
    private List<Tournament> tournaments = new ArrayList<>();

    @Version
    private Long version;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private MembershipStatus status = MembershipStatus.ACTIVE;

    @Column(name = "total_tournaments_played")
    private Integer totalTournamentsPlayed = 0;

    @Column(name = "total_winnings")
    private Double totalWinnings = 0.0;

    public enum MembershipStatus {
        ACTIVE, EXPIRED, SUSPENDED, PENDING
    }

    public Member() {
    }

    public Member(String memberName, String memberAddress, String memberEmail, String memberPhone, LocalDate startDate, Integer duration) {
        this.memberName = memberName;
        this.memberAddress = memberAddress;
        this.memberEmail = memberEmail;
        this.memberPhone = memberPhone;
        this.startDate = startDate;
        this.duration = duration;
        this.totalTournamentsPlayed = 0;
        this.totalWinnings = 0.0;
        this.status = MembershipStatus.ACTIVE;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getMemberName() {
        return memberName;
    }

    public void setMemberName(String memberName) {
        this.memberName = memberName;
    }

    public String getMemberAddress() {
        return memberAddress;
    }

    public void setMemberAddress(String memberAddress) {
        this.memberAddress = memberAddress;
    }

    public String getMemberEmail() {
        return memberEmail;
    }

    public void setMemberEmail(String memberEmail) {
        this.memberEmail = memberEmail;
    }

    public String getMemberPhone() {
        return memberPhone;
    }

    public void setMemberPhone(String memberPhone) {
        this.memberPhone = memberPhone;
    }

    public LocalDate getStartDate() {
        return startDate;
    }

    public void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
    }

    public Integer getDuration() {
        return duration;
    }

    public void setDuration(Integer duration) {
        this.duration = duration;
    }

    public List<Tournament> getTournaments() {
        return tournaments;
    }

    public void setTournaments(List<Tournament> tournaments) {
        this.tournaments = tournaments;
    }

    public MembershipStatus getStatus() {
        return status;
    }

    public void setStatus(MembershipStatus status) {
        this.status = status;
    }

    public Integer getTotalTournamentsPlayed() {
        return totalTournamentsPlayed;
    }

    public void setTotalTournamentsPlayed(Integer totalTournamentsPlayed) {
        this.totalTournamentsPlayed = totalTournamentsPlayed;
    }

    public Double getTotalWinnings() {
        return totalWinnings;
    }

    public void setTotalWinnings(Double totalWinnings) {
        this.totalWinnings = totalWinnings;
    }

    public boolean isActive() {
        return status == MembershipStatus.ACTIVE;
    }

    public boolean isMembershipExpired() {
        return LocalDate.now().isAfter(startDate.plusMonths(duration));
    }

    public void incrementTournamentsPlayed() {
        this.totalTournamentsPlayed++;
    }

    public void addWinnings(Double amount) {
        this.totalWinnings += amount;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Member)) return false;
        Member member = (Member) o;
        return getId() != null && getId().equals(member.getId());
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}