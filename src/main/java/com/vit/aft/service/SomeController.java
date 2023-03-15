package com.vit.aft.service;

import io.vavr.control.Try;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.CannotCreateTransactionException;
import org.springframework.web.bind.annotation.*;
import com.vit.aft.service.model.Client;
import com.vit.aft.service.repository.ClientRepository;

import java.util.Objects;

import static io.vavr.API.*;
import static io.vavr.Predicates.instanceOf;

@Slf4j
@RestController
@RequestMapping("some")
public class SomeController {

    @Autowired
    private ClientRepository clientRepository;

    @GetMapping(value = "/msg/{name}")
    public ResponseEntity<String> getRequest(@PathVariable("name") String name) {
        StringBuilder result = new StringBuilder();
        if (name.equals("Investor") || name.trim().length() == 1) {
            result.append("Please provide a name!");
        } else {
            result.append("Hello ").append(name);
        }
        HttpHeaders headers = new HttpHeaders();
        return ResponseEntity.ok()
                .headers(headers)
                .body(result.toString());
    }

    @PostMapping(value = "/new")
    public ResponseEntity<String> createNewClient(@RequestBody Client client) {
        HttpHeaders headers = new HttpHeaders();
        if (Objects.isNull(client.getClientName())) {
            return ResponseEntity.badRequest()
                    .headers(headers)
                    .body("clientName не передан либо равен null !");
        }
        return Try.of(() -> ResponseEntity
                        .status(HttpStatus.CREATED)
                        .headers(headers)
                        .body(String.format("Создан клиент: стр. № %d", clientRepository.save(client).getId())))
                .recover(ex -> Match(ex).of(
                        Case($(instanceOf(DataIntegrityViolationException.class)), () ->
                                ResponseEntity.badRequest().body("Запись с таким clientName уже существует !")),
                        Case($(instanceOf(CannotCreateTransactionException.class)), () ->
                                ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE).body("Проблема с доступностью БД !")),
                        Case($(), () ->
                                ResponseEntity.internalServerError().body("Неизвестная ошибка сервера !"))))
                .get();
    }
}
