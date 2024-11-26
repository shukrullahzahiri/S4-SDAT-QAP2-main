package com.golfclub.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "tournaments")
public class Tournament {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull(message = "Start date is required")
    @FutureOrPresent(message = "Start date must be present or future")
    @Column(name = "start_date")
    private LocalDate startDate;

    @NotNull(message = "End date is required")
    @FutureOrPresent(message = "End date must be present or future")
    @Column(name = "end_date")
    private LocalDate endDate;

    @NotBlank(message = "Location is required")
    @Column(name = "location")
    private String location;

    @Positive(message = "Entry fee must be positive")
    @Column(name = "entry_fee", nullable = false)
    private Double entryFee;

    @PositiveOrZero(message = "Cash prize must be zero or positive")
    @Column(name = "cash_prize_amount", nullable = false)
    private Double cashPrizeAmount;

    @JsonIgnoreProperties("tournaments")
    @ManyToMany
    @JoinTable(
            name = "tournament_members",
            joinColumns = @JoinColumn(name = "tournament_id"),
            inverseJoinColumns = @JoinColumn(name = "member_id")
    )
    private Set<Member> participatingMembers = new HashSet<>();

    @Version
    @Column(name = "version")
    private Long version;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private TournamentStatus status = TournamentStatus.SCHEDULED;

    @Min(value = 2, message = "Minimum participants must be at least 2")
    @Column(name = "minimum_participants", nullable = false)
    private Integer minimumParticipants = 2;

    @Max(value = 100, message = "Maximum participants cannot exceed 100")
    @Column(name = "maximum_participants", nullable = false)
    private Integer maximumParticipants = 100;

    public enum TournamentStatus {
        SCHEDULED, IN_PROGRESS, COMPLETED, CANCELLED
    }

    // Default constructor
    public Tournament() {
    }

    // Constructor with required fields
    public Tournament(LocalDate startDate, LocalDate endDate, String location,
                      Double entryFee, Double cashPrizeAmount) {
        this.startDate = startDate;
        this.endDate = endDate;
        this.location = location;
        this.entryFee = entryFee;
        this.cashPrizeAmount = cashPrizeAmount;
    }

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalDate getStartDate() {
        return startDate;
    }

    public void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
    }

    public LocalDate getEndDate() {
        return endDate;
    }

    public void setEndDate(LocalDate endDate) {
        this.endDate = endDate;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public Double getEntryFee() {
        return entryFee;
    }

    public void setEntryFee(Double entryFee) {
        this.entryFee = entryFee;
    }

    public Double getCashPrizeAmount() {
        return cashPrizeAmount;
    }

    public void setCashPrizeAmount(Double cashPrizeAmount) {
        this.cashPrizeAmount = cashPrizeAmount;
    }

    public Set<Member> getParticipatingMembers() {
        return participatingMembers;
    }

    public void setParticipatingMembers(Set<Member> participatingMembers) {
        this.participatingMembers = participatingMembers;
    }

    public Long getVersion() {
        return version;
    }

    public void setVersion(Long version) {
        this.version = version;
    }

    public TournamentStatus getStatus() {
        return status;
    }

    public void setStatus(TournamentStatus status) {
        this.status = status;
    }

    public Integer getMinimumParticipants() {
        return minimumParticipants;
    }

    public void setMinimumParticipants(Integer minimumParticipants) {
        this.minimumParticipants = minimumParticipants;
    }

    public Integer getMaximumParticipants() {
        return maximumParticipants;
    }

    public void setMaximumParticipants(Integer maximumParticipants) {
        this.maximumParticipants = maximumParticipants;
    }

    // Business methods
    public void addMember(Member member) {
        participatingMembers.add(member);
        member.getTournaments().add(this);
    }

    public void removeMember(Member member) {
        participatingMembers.remove(member);
        member.getTournaments().remove(this);
    }

    public boolean isRegistrationOpen() {
        return status == TournamentStatus.SCHEDULED &&
                participatingMembers.size() < maximumParticipants &&
                LocalDate.now().isBefore(startDate);
    }

    public boolean hasMinimumParticipants() {
        return participatingMembers.size() >= minimumParticipants;
    }

    public boolean isMemberRegistered(Member member) {
        return participatingMembers.contains(member);
    }

    public Double calculateTotalRevenue() {
        return entryFee * participatingMembers.size();
    }

    // Equals and HashCode
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Tournament)) return false;
        Tournament that = (Tournament) o;
        return getId() != null && getId().equals(that.getId());
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }

    @Override
    public String toString() {
        return "Tournament{" +
                "id=" + id +
                ", startDate=" + startDate +
                ", location='" + location + '\'' +
                ", status=" + status +
                ", participants=" + participatingMembers.size() +
                '}';
    }
}