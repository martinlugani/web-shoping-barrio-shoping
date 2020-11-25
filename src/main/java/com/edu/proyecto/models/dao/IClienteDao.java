package com.edu.proyecto.models.dao;

import java.util.Optional;

import org.springframework.data.repository.CrudRepository;

import com.edu.proyecto.models.entity.Cliente;

public interface IClienteDao extends CrudRepository<Cliente, Long> {
 public Cliente findByUsername(String username);
 public Optional<Cliente>  findById(Long id);
  
 

}
