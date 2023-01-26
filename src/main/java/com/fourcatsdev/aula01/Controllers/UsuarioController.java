package com.fourcatsdev.aula01.Controllers;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
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

    @GetMapping("/novo")
    public String adicionaUsuario(Model model) {
        model.addAttribute("usuario", new Usuario());
        return "/publica-cria-usuario";
    }

    @PostMapping("/salvar")
    public String salvarUsuario(@Valid Usuario usuario, BindingResult result, RedirectAttributes attributes) {
        if (result.hasErrors()) {
            return "/publica-cria-usuario";
        }
        usuarioRepositorio.save(usuario);
        attributes.addFlashAttribute("mensagem", "Usuário salvo com sucesso");
        return "redirect:/usuario/novo";
    }

    @RequestMapping("/admin/listar")
    public String listarUser(Model model) {
        model.addAttribute("usuarios", usuarioRepositorio.findAll());
        return "auth/adm/admin-listar-usuario";
    }

    @GetMapping("/admin/apagar/{id}")
    public String deleteUser(@PathVariable("id") long id, Model model) {
        Usuario user = usuarioRepositorio.findById(id).orElseThrow(() -> new IllegalArgumentException("ID inválido"));
        usuarioRepositorio.delete(user);
        return "redirect:/usuario/admin/listar";
    }

    @GetMapping("/admin/editar/{id}")
    public String editarUser(@PathVariable("id") long id, Model model) {
        Optional<Usuario> usuarioVelho = usuarioRepositorio.findById(id);
        if (!usuarioVelho.isPresent())
            throw new IllegalArgumentException("Usuário inválido: " + id);

        Usuario usuario = usuarioVelho.get();
        model.addAttribute("usuario", usuario);
        return "auth/user/user-alterar-usuario";
    }

    @PostMapping ("/admin/editar/{id}")
    public String editarUser(@PathVariable("id") long id, @Valid Usuario user, BindingResult result) {
        if (result.hasErrors()) {
            user.setId(id);
            return "/auth/user/user-alterar-usuario";
        }
        usuarioRepositorio.save(user);
        return "redirect:/usuario/admin/listar";
    }
}