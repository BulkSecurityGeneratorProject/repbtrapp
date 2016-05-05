package com.btapp.web.rest;

import com.btapp.Application;
import com.btapp.domain.Expense;
import com.btapp.repository.ExpenseRepository;
import com.btapp.service.ExpenseService;
import com.btapp.web.rest.TestUtil;
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
 * Test class for the ExpenseResource REST controller.
 *
 * @see ExpenseResource
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
@WebAppConfiguration
@IntegrationTest
public class ExpenseResourceIntTest {


    private static final Double DEFAULT_EXPENSE_COST = 1D;
    private static final Double UPDATED_EXPENSE_COST = 2D;

    @Inject
    private ExpenseRepository expenseRepository;

    @Inject
    private ExpenseService expenseService;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    private MockMvc restExpenseMockMvc;

    private Expense expense;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        ExpenseResource expenseResource = new ExpenseResource();
        ReflectionTestUtils.setField(expenseResource, "expenseService", expenseService);
        this.restExpenseMockMvc = MockMvcBuilders.standaloneSetup(expenseResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    @Before
    public void initTest() {
        expense = new Expense();
        expense.setExpense_cost(DEFAULT_EXPENSE_COST);
    }

    @Test
    @Transactional
    public void createExpense() throws Exception {
        int databaseSizeBeforeCreate = expenseRepository.findAll().size();

        // Create the Expense

        restExpenseMockMvc.perform(post("/api/expenses")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(expense)))
                .andExpect(status().isCreated());

        // Validate the Expense in the database
        List<Expense> expenses = expenseRepository.findAll();
        assertThat(expenses).hasSize(databaseSizeBeforeCreate + 1);
        Expense testExpense = expenses.get(expenses.size() - 1);
        assertThat(testExpense.getExpense_cost()).isEqualTo(DEFAULT_EXPENSE_COST);
    }

    @Test
    @Transactional
    public void checkExpense_costIsRequired() throws Exception {
        int databaseSizeBeforeTest = expenseRepository.findAll().size();
        // set the field null
        expense.setExpense_cost(null);

        // Create the Expense, which fails.

        restExpenseMockMvc.perform(post("/api/expenses")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(expense)))
                .andExpect(status().isBadRequest());

        List<Expense> expenses = expenseRepository.findAll();
        assertThat(expenses).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllExpenses() throws Exception {
        // Initialize the database
        expenseRepository.saveAndFlush(expense);

        // Get all the expenses
        restExpenseMockMvc.perform(get("/api/expenses?sort=id,desc"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.[*].id").value(hasItem(expense.getId().intValue())))
                .andExpect(jsonPath("$.[*].expense_cost").value(hasItem(DEFAULT_EXPENSE_COST.doubleValue())));
    }

    @Test
    @Transactional
    public void getExpense() throws Exception {
        // Initialize the database
        expenseRepository.saveAndFlush(expense);

        // Get the expense
        restExpenseMockMvc.perform(get("/api/expenses/{id}", expense.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.id").value(expense.getId().intValue()))
            .andExpect(jsonPath("$.expense_cost").value(DEFAULT_EXPENSE_COST.doubleValue()));
    }

    @Test
    @Transactional
    public void getNonExistingExpense() throws Exception {
        // Get the expense
        restExpenseMockMvc.perform(get("/api/expenses/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateExpense() throws Exception {
        // Initialize the database
        expenseRepository.saveAndFlush(expense);

		int databaseSizeBeforeUpdate = expenseRepository.findAll().size();

        // Update the expense
        expense.setExpense_cost(UPDATED_EXPENSE_COST);

        restExpenseMockMvc.perform(put("/api/expenses")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(expense)))
                .andExpect(status().isOk());

        // Validate the Expense in the database
        List<Expense> expenses = expenseRepository.findAll();
        assertThat(expenses).hasSize(databaseSizeBeforeUpdate);
        Expense testExpense = expenses.get(expenses.size() - 1);
        assertThat(testExpense.getExpense_cost()).isEqualTo(UPDATED_EXPENSE_COST);
    }

    @Test
    @Transactional
    public void deleteExpense() throws Exception {
        // Initialize the database
        expenseRepository.saveAndFlush(expense);

		int databaseSizeBeforeDelete = expenseRepository.findAll().size();

        // Get the expense
        restExpenseMockMvc.perform(delete("/api/expenses/{id}", expense.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate the database is empty
        List<Expense> expenses = expenseRepository.findAll();
        assertThat(expenses).hasSize(databaseSizeBeforeDelete - 1);
    }
}
