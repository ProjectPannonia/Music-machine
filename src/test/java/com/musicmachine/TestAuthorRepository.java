package com.musicmachine;

import com.musicmachine.repository.MusicRepository;
import com.musicmachine.repository.entities.Author;
import org.junit.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import javax.persistence.EntityManager;
import javax.sql.DataSource;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(SpringExtension.class)
@DataJpaTest
public class TestAuthorRepository {

    @Autowired
    private DataSource dataSource;
    @Autowired
    private JdbcTemplate jdbcTemplate;
    @Autowired
    private EntityManager entityManager;
    @Autowired
    private MusicRepository musicRepository;

//    @Test
//    public void injectedCompponentsAreNotNull() {
//        //assertThat(dataSource).isNotNull();
//        //assertThat(jdbcTemplate).isNotNull();
//        //assertThat(entityManager).isNotNull();
//        assertThat(musicRepository).isNotNull();
//    }
//    @Test
//    public void whenSaved_thenFindsByName() {
//        musicRepository.saveMyOwn(new Author("First"));
//        //assertThat(musicRepository.findByName("Bab")).isNotNull();
//    }
}
