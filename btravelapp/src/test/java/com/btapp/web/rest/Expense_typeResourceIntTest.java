package com.btapp.web.rest;

import com.btapp.Application;
import com.btapp.domain.Expense_type;
import com.btapp.repository.Expense_typeRepository;
import com.btapp.service.Expense_typeService;

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
 * Test class for the Expense_typeResource REST controller.
 *
 * @see Expense_typeResource
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
@WebAppConfiguration
@IntegrationTest
public class Expense_typeResourceIntTest {

    private static final String DEFAULT_TYPE = "AAAAA";
    private static final String UPDATED_TYPE = "BBBBB";

    @Inject
    private Expense_typeRepository expense_typeRepository;

    @Inject
    private Expense_typeService expense_typeService;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    private MockMvc restExpense_typeMockMvc;

    private Expense_type expense_type;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        Expense_typeResource expense_typeResource = new Expense_typeResource();
        ReflectionTestUtils.setField(expense_typeResource, "expense_typeService", expense_typeService);
        this.restExpense_typeMockMvc = MockMvcBuilders.standaloneSetup(expense_typeResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    @Before
    public void initTest() {
        expense_type = new Expense_type();
        expense_type.setType(DEFAULT_TYPE);
    }

    @Test
    @Transactional
    public void createExpense_type() throws Exception {
        int databaseSizeBeforeCreate = expense_typeRepository.findAll().size();

        // Create the Expense_type

        restExpense_typeMockMvc.perform(post("/api/expense_types")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(expense_type)))
                .andExpect(status().isCreated());

        // Validate the Expense_type in the database
        List<Expense_type> expense_types = expense_typeRepository.findAll();
        assertThat(expense_types).hasSize(databaseSizeBeforeCreate + 1);
        Expense_type testExpense_type = expense_types.get(expense_types.size() - 1);
        assertThat(testExpense_type.getType()).isEqualTo(DEFAULT_TYPE);
    }

    @Test
    @Transactional
    public void checkTypeIsRequired() throws Exception {
        int databaseSizeBeforeTest = expense_typeRepository.findAll().size();
        // set the field null
        expense_type.setType(null);

        // Create the Expense_type, which fails.

        restExpense_typeMockMvc.perform(post("/api/expense_types")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(expense_type)))
                .andExpect(status().isBadRequest());

        List<Expense_type> expense_types = expense_typeRepository.findAll();
        assertThat(expense_types).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllExpense_types() throws Exception {
        // Initialize the database
        expense_typeRepository.saveAndFlush(expense_type);

        // Get all the expense_types
        restExpense_typeMockMvc.perform(get("/api/expense_types?sort=id,desc"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.[*].id").value(hasItem(expense_type.getId().intValue())))
                .andExpect(jsonPath("$.[*].type").value(hasItem(DEFAULT_TYPE.toString())));
    }

    @Test
    @Transactional
    public void getExpense_type() throws Exception {
        // Initialize the database
        expense_typeRepository.saveAndFlush(expense_type);

        // Get the expense_type
        restExpense_typeMockMvc.perform(get("/api/expense_types/{id}", expense_type.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.id").value(expense_type.getId().intValue()))
            .andExpect(jsonPath("$.type").value(DEFAULT_TYPE.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingExpense_type() throws Exception {
        // Get the expense_type
        restExpense_typeMockMvc.perform(get("/api/expense_types/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateExpense_type() throws Exception {
        // Initialize the database
        expense_typeRepository.saveAndFlush(expense_type);

		int databaseSizeBeforeUpdate = expense_typeRepository.findAll().size();

        // Update the expense_type
        expense_type.setType(UPDATED_TYPE);

        restExpense_typeMockMvc.perform(put("/api/expense_types")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(expense_type)))
                .andExpect(status().isOk());

        // Validate the Expense_type in the database
        List<Expense_type> expense_types = expense_typeRepository.findAll();
        assertThat(expense_types).hasSize(databaseSizeBeforeUpdate);
        Expense_type testExpense_type = expense_types.get(expense_types.size() - 1);
        assertThat(testExpense_type.getType()).isEqualTo(UPDATED_TYPE);
    }

    @Test
    @Transactional
    public void deleteExpense_type() throws Exception {
        // Initialize the database
        expense_typeRepository.saveAndFlush(expense_type);

		int databaseSizeBeforeDelete = expense_typeRepository.findAll().size();

        // Get the expense_type
        restExpense_typeMockMvc.perform(delete("/api/expense_types/{id}", expense_type.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate the database is empty
        List<Expense_type> expense_types = expense_typeRepository.findAll();
        assertThat(expense_types).hasSize(databaseSizeBeforeDelete - 1);
    }
}
