package ec.edu.espe.banquito.cuentas.controller.Account;

import ec.edu.espe.banquito.cuentas.controller.DTO.Account.AccountTransactionRQ;
import ec.edu.espe.banquito.cuentas.service.Account.AccountTransactionService;
import lombok.RequiredArgsConstructor;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/account-transaction")
@RequiredArgsConstructor
public class AccountTransactionController {

    private final AccountTransactionService accountTransactionService;

    @PostMapping
    public ResponseEntity<?> create(@RequestBody AccountTransactionRQ accountTransactionRQ) {
        try {
            accountTransactionService.create(accountTransactionRQ);
            return ResponseEntity.ok().body("Transaccion realizada");
        } catch (RuntimeException rte) {
            return ResponseEntity.badRequest().body(rte.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

}
