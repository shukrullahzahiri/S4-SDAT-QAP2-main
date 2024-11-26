package com.golfclub.controller;

import com.golfclub.model.Member;
import com.golfclub.service.MemberService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import jakarta.validation.Valid;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/members")
public class MemberController {
    private final MemberService memberService;

    @Autowired
    public MemberController(MemberService memberService) {
        this.memberService = memberService;
    }

    @PostMapping
    public ResponseEntity<Member> createMember(@Valid @RequestBody Member member) {
        try {
            Member savedMember = memberService.saveMember(member);
            return new ResponseEntity<>(savedMember, HttpStatus.CREATED);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @GetMapping
    public List<Member> getAllMembers() {
        return memberService.getAllMembers();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Member> getMember(@PathVariable Long id) {
        return memberService.getMemberById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PutMapping("/{id}")
    public ResponseEntity<Member> updateMember(@PathVariable Long id, @Valid @RequestBody Member member) {
        try {
            Member updatedMember = memberService.updateMember(id, member);
            return ResponseEntity.ok(updatedMember);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteMember(@PathVariable Long id) {
        memberService.deleteMember(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/search/name/{name}")
    public List<Member> searchByName(@PathVariable String name) {
        return memberService.searchByName(name);
    }

    @GetMapping("/search/phone/{phone}")
    public List<Member> searchByPhone(@PathVariable String phone) {
        return memberService.searchByPhone(phone);
    }

    @GetMapping("/search/status/{status}")
    public List<Member> searchByStatus(@PathVariable Member.MembershipStatus status) {
        return memberService.findByStatus(status);
    }

    @GetMapping("/search/active")
    public List<Member> findActiveMembers() {
        return memberService.findActiveMembers();
    }

    @GetMapping("/search/tournaments")
    public List<Member> findByMinimumTournaments(@RequestParam Integer minCount) {
        return memberService.findByMinimumTournaments(minCount);
    }

    @PatchMapping("/{id}/status")
    public ResponseEntity<Void> updateStatus(
            @PathVariable Long id,
            @RequestBody Map<String, String> status) {
        try {
            Member.MembershipStatus newStatus = Member.MembershipStatus.valueOf(status.get("status"));
            memberService.updateMemberStatus(id, newStatus);
            return ResponseEntity.ok().build();
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @PatchMapping("/{id}/duration")
    public ResponseEntity<Member> extendMembership(
            @PathVariable Long id,
            @RequestBody Map<String, Integer> extension) {
        try {
            Member updated = memberService.updateMembershipDuration(id, extension.get("months"));
            return ResponseEntity.ok(updated);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @GetMapping("/search/tournament-date")
    public List<Member> findByTournamentDate(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
        return memberService.findByTournamentDate(date);
    }

    @GetMapping("/top-participants")
    public List<Member> getTopParticipants() {
        return memberService.findTopParticipants();
    }

    @PostMapping("/{id}/check-status")
    public ResponseEntity<Void> checkMembershipStatus(@PathVariable Long id) {
        memberService.checkMembershipStatus(id);
        return ResponseEntity.ok().build();
    }
}