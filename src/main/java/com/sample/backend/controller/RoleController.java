package com.sample.backend.controller;

import com.sample.backend.config.ApiStandardResponses;
import com.sample.backend.dto.RoleDTO;
import com.sample.backend.service.RoleService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/roles")
@Tag(name = "Role", description = "Role management APIs")
@Validated
public class RoleController {

  private final RoleService roleService;

  public RoleController(RoleService roleService) {
    this.roleService = roleService;
  }

  @Operation(summary = "Get all roles", description = "Retrieves a list of all available roles")
  @ApiResponse(responseCode = "200", description = "Successfully retrieved all roles")
  @ApiStandardResponses
  @GetMapping
  public ResponseEntity<List<RoleDTO>> getAllRoles() {
    return ResponseEntity.ok(roleService.getAllRoles());
  }

  @Operation(summary = "Get role by ID", description = "Retrieves a specific role by its ID")
  @ApiResponse(responseCode = "200", description = "Role found")
  @ApiStandardResponses
  @GetMapping("/{id}")
  public ResponseEntity<RoleDTO> getRoleById(
      @Parameter(description = "Role ID", required = true) @PathVariable Long id) {
    return ResponseEntity.ok(roleService.getRoleById(id));
  }

  @Operation(
      summary = "Get roles by movie",
      description = "Retrieves all roles for a specific movie")
  @ApiResponse(responseCode = "200", description = "Roles retrieved successfully")
  @ApiStandardResponses
  @GetMapping("/movie/{movieId}")
  public ResponseEntity<List<RoleDTO>> getRolesByMovie(
      @Parameter(description = "Movie ID", required = true) @PathVariable Long movieId) {
    return ResponseEntity.ok(roleService.getRolesByMovieId(movieId));
  }

  @Operation(
      summary = "Get roles by actor",
      description = "Retrieves all roles played by a specific actor")
  @ApiResponse(responseCode = "200", description = "Roles retrieved successfully")
  @ApiStandardResponses
  @GetMapping("/actor/{actorId}")
  public ResponseEntity<List<RoleDTO>> getRolesByActor(
      @Parameter(description = "Actor ID", required = true) @PathVariable Long actorId) {
    return ResponseEntity.ok(roleService.getRolesByActorId(actorId));
  }

  @Operation(summary = "Search roles", description = "Search roles by character name")
  @ApiResponse(responseCode = "200", description = "Search results retrieved successfully")
  @ApiStandardResponses
  @GetMapping("/search")
  public ResponseEntity<List<RoleDTO>> searchRoles(
      @Parameter(description = "Character name (partial match)") @RequestParam
          String characterName) {
    return ResponseEntity.ok(roleService.searchRolesByCharacterName(characterName));
  }

  @Operation(summary = "Create role", description = "Creates a new role entry")
  @ApiResponse(
      responseCode = "201",
      description = "Role created successfully",
      content = @Content(schema = @Schema(implementation = RoleDTO.class)))
  @ApiStandardResponses
  @PostMapping
  public ResponseEntity<RoleDTO> createRole(
      @Parameter(description = "Role data", required = true) @Valid @RequestBody RoleDTO roleDTO) {
    return new ResponseEntity<>(roleService.createRole(roleDTO), HttpStatus.CREATED);
  }

  @Operation(summary = "Update role", description = "Updates an existing role's information")
  @ApiResponse(responseCode = "200", description = "Role updated successfully")
  @ApiStandardResponses
  @PutMapping("/{id}")
  public ResponseEntity<RoleDTO> updateRole(
      @Parameter(description = "Role ID", required = true) @PathVariable Long id,
      @Parameter(description = "Updated role data", required = true) @Valid @RequestBody
          RoleDTO roleDTO) {
    return ResponseEntity.ok(roleService.updateRole(id, roleDTO));
  }

  @Operation(summary = "Delete role", description = "Removes a role from the database")
  @ApiResponse(responseCode = "204", description = "Role deleted successfully")
  @ApiStandardResponses
  @DeleteMapping("/{id}")
  public ResponseEntity<Void> deleteRole(
      @Parameter(description = "Role ID", required = true) @PathVariable Long id) {
    roleService.deleteRole(id);
    return ResponseEntity.noContent().build();
  }
}
