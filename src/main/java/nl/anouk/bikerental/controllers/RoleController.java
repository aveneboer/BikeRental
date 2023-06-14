/*
package nl.anouk.bikerental.controllers;

import nl.anouk.bikerental.dtos.RoleDto;
import nl.anouk.bikerental.models.Role;
import nl.anouk.bikerental.repositories.RoleRepository;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RestController
public class RoleController {
    private final RoleRepository repos;

    public  RoleController(RoleRepository repos) { this.repos = repos;}

    @GetMapping("/roles")
    public List<RoleDto> getRoles() {
        List<RoleDto> roleDtos = new ArrayList<>();
        for (Role r : repos.findAll()) {
            RoleDto rdto = new RoleDto();
            rdto.rolename = r.getRolename();
            roleDtos.add(rdto);
        }
        return roleDtos;
    }
}
*/
