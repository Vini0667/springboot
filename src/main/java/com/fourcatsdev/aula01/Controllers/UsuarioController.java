package com.fourcatsdev.aula01.Controllers;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.fourcatsdev.aula01.Model.Papel;
import com.fourcatsdev.aula01.Model.Usuario;
import com.fourcatsdev.aula01.Repositorio.PapelRepository;
import com.fourcatsdev.aula01.Repositorio.UsuarioRepositorio;

import jakarta.validation.Valid;

@Controller
@RequestMapping("/usuario")
public class UsuarioController {

    @Autowired
    private UsuarioRepositorio usuarioRepositorio;
    @Autowired
    private PapelRepository papelRepository;

    @GetMapping("/novo")
    public String adicionaUsuario(Model model) {
        model.addAttribute("usuario", new Usuario());
        return "/publica-cria-usuario";
    }

    @PostMapping("/salvar")
    public String salvarUsuario(@Valid Usuario usuario, BindingResult result, RedirectAttributes attributes,
            Model model) {
        if (result.hasErrors()) {
            return "/publica-cria-usuario";
        }
        if (verificaLogin(usuario)) {
            model.addAttribute("loginExistente", "O login já exite digite outro");
            return "/publica-cria-usuario";
        }
        List<Papel> papeis = new ArrayList<Papel>();
        papeis.add(papelRepository.findByPapel("USER"));
        usuario.setPapeis(papeis);

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

    @GetMapping("/editar/{id}")
    public String editarUser(@PathVariable("id") long id, Model model) {
        Optional<Usuario> usuarioVelho = usuarioRepositorio.findById(id);
        if (!usuarioVelho.isPresent())
            throw new IllegalArgumentException("Usuário inválido: " + id);

        Usuario usuario = usuarioVelho.get();
        model.addAttribute("usuario", usuario);
        return "auth/user/user-alterar-usuario";
    }

    @PostMapping("/editar/{id}")
    public String editarUser(@PathVariable("id") long id, @Valid Usuario user, BindingResult result) {
        if (result.hasErrors()) {
            user.setId(id);
            return "/auth/user/user-alterar-usuario";
        }
        usuarioRepositorio.save(user);
        return "redirect:/usuario/admin/listar";
    }

    @GetMapping("admin/editar/papeis/{id}")
    public String editaPapel(@PathVariable("id") long id, Model model) {
        Usuario user = (Usuario) atribuiUserById(id);
        model.addAttribute("usuario", user);
        model.addAttribute("listaPapel", papelRepository.findAll());
        return "auth/adm/atribuir-papeis";
    }

    @PostMapping("admin/editar/papeis/{id}")
    public String editaPapelEAtivação (@PathVariable ("id") long id, Usuario user, @RequestParam (value = "pps", required = false)
     int[] pps, RedirectAttributes attributes) {
        if (pps == null) {
            user.setId (id);
            attributes.addFlashAttribute("mensagem", "Pelo menos um papel deve estar selecionado");
            return "redirect:/usuario/admin/editar/papeis/" + id;
        } else {
            Usuario usuario = (Usuario) atribuiUserById(id);
            usuario.setPapeis(listaPapeis(pps));
            usuario.setAtivo(user.isAtivo());

            usuarioRepositorio.save(usuario);
        }
        return "redirect:/usuario/admin/listar";
    
    }

    public boolean verificaPapelPorId (long idPapel) {
        return papelRepository.findById(idPapel).isPresent() ? true : false;
    }

    public List <Papel> listaPapeis (int[] pps) {
        List <Papel> papeis = new ArrayList <> ();
        for (int i = 0; i < pps.length; i++) {
            long idPapel = pps[i];

            if (verificaPapelPorId(idPapel)) {
                papeis.add(papelRepository.findById(idPapel).get());
            }
        }
        return papeis;
    }

    public Object atribuiUserById (long idUser) {
        Optional <Usuario> user = usuarioRepositorio.findById(idUser);
        return user.isPresent() ? user.get() : new IllegalArgumentException("Usuário inválido");
    }

    public boolean verificaLogin(Usuario user) {
        Usuario usr = usuarioRepositorio.findByLogin(user.getLogin());
        return usr != null ? true : false;
    }
} 