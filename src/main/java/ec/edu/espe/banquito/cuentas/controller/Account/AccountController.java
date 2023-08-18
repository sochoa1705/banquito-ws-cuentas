package ec.edu.espe.banquito.cuentas.controller.Account;

import ec.edu.espe.banquito.cuentas.controller.DTO.Account.AccountRQ;
import ec.edu.espe.banquito.cuentas.service.Account.AccountService;
import lombok.RequiredArgsConstructor;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/account")
@RequiredArgsConstructor
public class AccountController {

    private final AccountService accountService;

    @GetMapping("/verify/{accountHolderCode}")
    public ResponseEntity<?> verifyIfExistsAccount(@PathVariable String accountHolderCode) {
        try {
            return ResponseEntity.ok(accountService.verifyIfExistsAccount(accountHolderCode));
        } catch (RuntimeException rte) {
            return ResponseEntity.badRequest().body(rte.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @PostMapping
    public ResponseEntity<?> create(@RequestBody AccountRQ newAccount) {
        try {
            accountService.create(newAccount);
            return ResponseEntity.ok().body("Cuenta creada");
        } catch (RuntimeException rte) {
            return ResponseEntity.badRequest().body(rte.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}