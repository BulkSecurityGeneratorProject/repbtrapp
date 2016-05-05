package com.btapp.web.rest;

import com.btapp.Application;
import com.btapp.domain.Comments;
import com.btapp.repository.CommentsRepository;
import com.btapp.repository.search.CommentsSearchRepository;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import static org.hamcrest.Matchers.hasItem;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.IntegrationTest;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


/**
 * Test class for the CommentsResource REST controller.
 *
 * @see CommentsResource
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
@WebAppConfiguration
@IntegrationTest
public class CommentsResourceIntTest {

    private static final String DEFAULT_COMMENT = "AAAAA";
    private static final String UPDATED_COMMENT = "BBBBB";

    @Inject
    private CommentsRepository commentsRepository;

    @Inject
    private CommentsSearchRepository commentsSearchRepository;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    private MockMvc restCommentsMockMvc;

    private Comments comments;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        CommentsResource commentsResource = new CommentsResource();
        ReflectionTestUtils.setField(commentsResource, "commentsSearchRepository", commentsSearchRepository);
        ReflectionTestUtils.setField(commentsResource, "commentsRepository", commentsRepository);
        this.restCommentsMockMvc = MockMvcBuilders.standaloneSetup(commentsResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    @Before
    public void initTest() {
        comments = new Comments();
        comments.setComment(DEFAULT_COMMENT);
    }

    @Test
    @Transactional
    public void createComments() throws Exception {
        int databaseSizeBeforeCreate = commentsRepository.findAll().size();

        // Create the Comments

        restCommentsMockMvc.perform(post("/api/commentss")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(comments)))
                .andExpect(status().isCreated());

        // Validate the Comments in the database
        List<Comments> commentss = commentsRepository.findAll();
        assertThat(commentss).hasSize(databaseSizeBeforeCreate + 1);
        Comments testComments = commentss.get(commentss.size() - 1);
        assertThat(testComments.getComment()).isEqualTo(DEFAULT_COMMENT);
    }

    @Test
    @Transactional
    public void checkCommentIsRequired() throws Exception {
        int databaseSizeBeforeTest = commentsRepository.findAll().size();
        // set the field null
        comments.setComment(null);

        // Create the Comments, which fails.

        restCommentsMockMvc.perform(post("/api/commentss")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(comments)))
                .andExpect(status().isBadRequest());

        List<Comments> commentss = commentsRepository.findAll();
        assertThat(commentss).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllCommentss() throws Exception {
        // Initialize the database
        commentsRepository.saveAndFlush(comments);

        // Get all the commentss
        restCommentsMockMvc.perform(get("/api/commentss?sort=id,desc"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.[*].id").value(hasItem(comments.getId().intValue())))
                .andExpect(jsonPath("$.[*].comment").value(hasItem(DEFAULT_COMMENT.toString())));
    }

    @Test
    @Transactional
    public void getComments() throws Exception {
        // Initialize the database
        commentsRepository.saveAndFlush(comments);

        // Get the comments
        restCommentsMockMvc.perform(get("/api/commentss/{id}", comments.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.id").value(comments.getId().intValue()))
            .andExpect(jsonPath("$.comment").value(DEFAULT_COMMENT.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingComments() throws Exception {
        // Get the comments
        restCommentsMockMvc.perform(get("/api/commentss/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateComments() throws Exception {
        // Initialize the database
        commentsRepository.saveAndFlush(comments);

		int databaseSizeBeforeUpdate = commentsRepository.findAll().size();

        // Update the comments
        comments.setComment(UPDATED_COMMENT);

        restCommentsMockMvc.perform(put("/api/commentss")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(comments)))
                .andExpect(status().isOk());

        // Validate the Comments in the database
        List<Comments> commentss = commentsRepository.findAll();
        assertThat(commentss).hasSize(databaseSizeBeforeUpdate);
        Comments testComments = commentss.get(commentss.size() - 1);
        assertThat(testComments.getComment()).isEqualTo(UPDATED_COMMENT);
    }

    @Test
    @Transactional
    public void deleteComments() throws Exception {
        // Initialize the database
        commentsRepository.saveAndFlush(comments);

		int databaseSizeBeforeDelete = commentsRepository.findAll().size();

        // Get the comments
        restCommentsMockMvc.perform(delete("/api/commentss/{id}", comments.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate the database is empty
        List<Comments> commentss = commentsRepository.findAll();
        assertThat(commentss).hasSize(databaseSizeBeforeDelete - 1);
    }
}
