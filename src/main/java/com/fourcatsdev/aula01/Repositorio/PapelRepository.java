package com.fourcatsdev.aula01.Repositorio;

import org.springframework.data.jpa.repository.JpaRepository;

import com.fourcatsdev.aula01.Model.Papel;

public interface PapelRepository extends JpaRepository <Papel, Long> {
    Papel findByPapel (String papel);
}