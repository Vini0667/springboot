package com.fourcatsdev.aula01.Controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.fourcatsdev.aula01.Model.Usuario;
import com.fourcatsdev.aula01.Repositorio.UsuarioRepositorio;

import jakarta.validation.Valid;

@Controller
@RequestMapping("/usuario")
public class UsuarioController {

    @Autowired
    private UsuarioRepositorio usuarioRepositorio; 

    @GetMapping ("/novo")
    public String adicionaUsuario (Model model) {
        model.addAttribute ("usuario", new Usuario());
        return "/publica-cria-usuario";
    }

    @PostMapping ("/salvar")
    public String salvarUsuario (@Valid Usuario usuario, BindingResult result, RedirectAttributes attributes) {
        if (result.hasErrors()) {
            return "/publica-cria-usuario";
        }
        usuarioRepositorio.save(usuario);
        attributes.addFlashAttribute("mensagem", "Usuário salvo com sucesso");
        return "redirect:/usuario/novo";
    }
}