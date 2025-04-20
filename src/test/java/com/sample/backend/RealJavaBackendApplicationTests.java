package com.sample.backend;

import static org.junit.jupiter.api.Assertions.assertNotNull;

import com.sample.backend.controller.ActorController;
import com.sample.backend.controller.DirectorController;
import com.sample.backend.controller.MovieController;
import com.sample.backend.controller.RoleController;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class SampleJavaBackendApplicationTests {

  @Autowired private ActorController actorController;
  @Autowired private DirectorController directorController;
  @Autowired private MovieController movieController;
  @Autowired private RoleController roleController;

  @Test
  void contextLoads() {
    assertNotNull(actorController);
    assertNotNull(directorController);
    assertNotNull(movieController);
    assertNotNull(roleController);
  }
}
