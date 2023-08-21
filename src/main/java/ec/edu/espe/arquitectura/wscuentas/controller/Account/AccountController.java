package ec.edu.espe.arquitectura.wscuentas.controller.Account;

import ec.edu.espe.arquitectura.wscuentas.controller.DTO.Account.AccountRQ;
import ec.edu.espe.arquitectura.wscuentas.controller.DTO.Account.AccountUpdateRQ;
import ec.edu.espe.arquitectura.wscuentas.controller.DTO.Account.AccountUpdateStateRQ;
import ec.edu.espe.arquitectura.wscuentas.service.Account.AccountService;
import lombok.RequiredArgsConstructor;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@CrossOrigin
@RestController
@RequestMapping("/api/v1/account")
@RequiredArgsConstructor
public class AccountController {

    private final AccountService accountService;

    @GetMapping("/information/{codeInternalAccount}")
    public ResponseEntity<?> getAccountInformation(@PathVariable String codeInternalAccount) {
        try {
            return ResponseEntity.ok(accountService.getAccountInformation(codeInternalAccount));
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

    @PutMapping
    public ResponseEntity<?> update(@RequestBody AccountUpdateRQ accountUpdateRQ) {
        try {
            return ResponseEntity.ok(accountService.update(accountUpdateRQ));
        } catch (RuntimeException rte) {
            return ResponseEntity.badRequest().body(rte.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @PostMapping("/state")
    public ResponseEntity<?> updateStateDependsClient(@RequestBody AccountUpdateStateRQ accountUpdateStateRQ) {
        try {
            accountService.updateStateDependsClient(accountUpdateStateRQ);
            return ResponseEntity.ok().body("Cuenta actualizada");
        } catch (RuntimeException rte) {
            return ResponseEntity.badRequest().body(rte.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

}
