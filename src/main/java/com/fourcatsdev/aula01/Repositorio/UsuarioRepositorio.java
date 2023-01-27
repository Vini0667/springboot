package com.fourcatsdev.aula01.Repositorio;

import org.springframework.data.jpa.repository.JpaRepository;
import com.fourcatsdev.aula01.Model.Usuario;

public interface UsuarioRepositorio extends JpaRepository <Usuario, Long> {
    Usuario findByLogin (String login);
}