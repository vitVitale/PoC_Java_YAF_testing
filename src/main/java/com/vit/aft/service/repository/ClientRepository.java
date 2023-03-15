package com.vit.aft.service.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.vit.aft.service.model.Client;

public interface ClientRepository extends JpaRepository<Client, Long> {}
