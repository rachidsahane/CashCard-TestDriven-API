package com.example.cashcard;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.security.Principal;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/cashcards")
public class CashCardController {
    private final CashCardRepository cashCardRepository;

    private CashCardController(CashCardRepository cashCardRepository) {
        this.cashCardRepository = cashCardRepository;
    }

    @GetMapping("/{requestedId}")
    private ResponseEntity<CashCard> findById(@PathVariable Long requestedId, Principal principal) {
        // Optional<CashCard> cashCardOptional =
        // Optional.ofNullable(cashCardRepository.findByIdAndOwner(requestedId,
        // principal.getName()));
        // Optional<CashCard> cashCard = cashCardRepository.findById(requestedId);
        Optional<CashCard> optionalCashCard = cashCardRepository.findByIdAndOwner(requestedId, principal.getName());
        return optionalCashCard.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping
    private ResponseEntity<Void> createCashCard(@RequestBody CashCard newCashCard, UriComponentsBuilder ucb,
            Principal principal) {
        CashCard cashCardWithOwner = new CashCard(
                null,
                newCashCard.amount(),
                principal.getName());
        CashCard savedCashCard = cashCardRepository.save(cashCardWithOwner);
        URI locationOfNewCashCard = ucb
                .path("cashcards/{id}")
                .buildAndExpand(savedCashCard.id())
                .toUri();
        return ResponseEntity.created(locationOfNewCashCard).build();
    }

    @GetMapping
    private ResponseEntity<List<CashCard>> findAll(Pageable pageable, Principal principal) {
        Page<CashCard> page = cashCardRepository.findByOwner(principal.getName(),
                PageRequest.of(
                        pageable.getPageNumber(),
                        pageable.getPageSize(),
                        pageable.getSortOr(
                                Sort.by(Sort.Direction.ASC, "amount"))));

        return ResponseEntity.ok(page.getContent());
    }

    @PutMapping("/{requestId}")
    private ResponseEntity<Void> updateCashCard(@PathVariable Long requestId, @RequestBody CashCard cashCardUpdate,
            Principal principal) {
        Optional<CashCard> cashCard = cashCardRepository.findByIdAndOwner(requestId, principal.getName());
        if (cashCard.isPresent()) {
            CashCard updateCashCard = new CashCard(cashCard.get().id(), cashCardUpdate.amount(), principal.getName());
            cashCardRepository.save(updateCashCard);
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.notFound().build();
    }

    @DeleteMapping("/{requestId}")
    private ResponseEntity<Void> deleteCashCard(
            @PathVariable long requestId,
            Principal principal) {
        if (!cashCardRepository.existsByIdAndOwner(requestId, principal.getName())) {
            return ResponseEntity.notFound().build();
        }
        cashCardRepository.deleteById(requestId);
        return ResponseEntity.noContent().build();
    }

}
