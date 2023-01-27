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
        Papel papel = papelRepository.findByPapel("USER");
        List<Papel> papeis = new ArrayList<Papel>();
        papeis.add(papel);
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

    @GetMapping("/admin/editar/{id}")
    public String editarUser(@PathVariable("id") long id, Model model) {
        Optional<Usuario> usuarioVelho = usuarioRepositorio.findById(id);
        if (!usuarioVelho.isPresent())
            throw new IllegalArgumentException("Usuário inválido: " + id);

        Usuario usuario = usuarioVelho.get();
        model.addAttribute("usuario", usuario);
        return "auth/user/user-alterar-usuario";
    }

    @PostMapping("/admin/editar/{id}")
    public String editarUser(@PathVariable("id") long id, @Valid Usuario user, BindingResult result) {
        if (result.hasErrors()) {
            user.setId(id);
            return "/auth/user/user-alterar-usuario";
        }
        usuarioRepositorio.save(user);
        return "redirect:/usuario/admin/listar";
    }

    public boolean verificaLogin(Usuario user) {
        Usuario usr = usuarioRepositorio.findByLogin(user.getLogin());
        return usr != null ? true : false;
    }

    @GetMapping("admin/editar/papeis/{id}")
    public String editaPapel(@PathVariable("id") long id, Model model) {
        Optional<Usuario> userVelho = usuarioRepositorio.findById(id);
        if (!userVelho.isPresent())
            throw new IllegalArgumentException("Usuário inválido");
        Usuario user1 = userVelho.get();
        model.addAttribute("usuario", user1);
        model.addAttribute("listaPapel", papelRepository.findAll());
        return "auth/adm/atribuir-papeis";
    }

    @PostMapping("admin/editar/papeis/{id}")
    public String editaPapel (@PathVariable ("id") long id, Usuario user, @RequestParam (value = "pps", required = false)
     int[] pps, RedirectAttributes attributes) {
        if (pps == null) {
            user.setId (id);
            attributes.addFlashAttribute("mensagem", "Pelo menos um papel deve estar selecionado");
            return "redirect:/usuario/admin/editar/papeis/" + id;
        } else {
            List <Papel> papeis = new ArrayList <>();
          
            for (int i = 0; i < pps.length; i++) {
                long idPapel = (long) pps[i];
                Optional <Papel> papelOptional = papelRepository.findById(idPapel);

                if (papelOptional.isPresent()) {
                    Papel papel = papelOptional.get();
                    papeis.add(papel);
                }
            }
            Optional <Usuario> userOptional = usuarioRepositorio.findById(id);
            if (userOptional.isPresent()) {
                Usuario usr = userOptional.get();
                usr.setPapeis (papeis);
                usuarioRepositorio.save(usr);
            }
        }
        return "redirect:/usuario/admin/listar";
    }
}