package ec.edu.espe.arquitectura.wscuentas.controller.Account;

import ec.edu.espe.arquitectura.wscuentas.controller.DTO.Account.AccountTransactionRQ;
import ec.edu.espe.arquitectura.wscuentas.service.Account.AccountTransactionService;
import lombok.RequiredArgsConstructor;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@CrossOrigin
@RestController
@RequestMapping("/api/v1/account-transaction")
@RequiredArgsConstructor
public class AccountTransactionController {

    private final AccountTransactionService accountTransactionService;

    @PostMapping
    public ResponseEntity<?> create(@RequestBody AccountTransactionRQ accountTransactionRQ) {
        try {
            return ResponseEntity.ok(accountTransactionService.create(accountTransactionRQ));
        } catch (RuntimeException rte) {
            return ResponseEntity.badRequest().body(rte.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/history/{codeInternalAccount}")
    public ResponseEntity<?> getTransactionsOfAccount(@PathVariable String codeInternalAccount) {
        try {
            return ResponseEntity.ok(accountTransactionService.getTransactionsOfAccount(codeInternalAccount));
        } catch (RuntimeException rte) {
            return ResponseEntity.badRequest().body(rte.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

}
