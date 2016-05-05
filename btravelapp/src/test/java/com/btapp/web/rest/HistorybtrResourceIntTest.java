package com.btapp.web.rest;

import com.btapp.Application;
import com.btapp.domain.Historybtr;
import com.btapp.repository.HistorybtrRepository;
import com.btapp.repository.search.HistorybtrSearchRepository;

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
import java.time.Instant;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.ZoneId;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


/**
 * Test class for the HistorybtrResource REST controller.
 *
 * @see HistorybtrResource
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
@WebAppConfiguration
@IntegrationTest
public class HistorybtrResourceIntTest {

    private static final DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ISO_OFFSET_DATE_TIME.withZone(ZoneId.of("Z"));

    private static final String DEFAULT_BTRSTATUSBEFORE = "AAAAA";
    private static final String UPDATED_BTRSTATUSBEFORE = "BBBBB";
    private static final String DEFAULT_BTRSTATUSAFTER = "AAAAA";
    private static final String UPDATED_BTRSTATUSAFTER = "BBBBB";

    private static final ZonedDateTime DEFAULT_CHANGE_DATE = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneId.systemDefault());
    private static final ZonedDateTime UPDATED_CHANGE_DATE = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);
    private static final String DEFAULT_CHANGE_DATE_STR = dateTimeFormatter.format(DEFAULT_CHANGE_DATE);
    private static final String DEFAULT_CHANGED_BY = "AAAAA";
    private static final String UPDATED_CHANGED_BY = "BBBBB";

    private static final ZonedDateTime DEFAULT_START_DATE = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneId.systemDefault());
    private static final ZonedDateTime UPDATED_START_DATE = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);
    private static final String DEFAULT_START_DATE_STR = dateTimeFormatter.format(DEFAULT_START_DATE);

    private static final ZonedDateTime DEFAULT_END_DATE = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneId.systemDefault());
    private static final ZonedDateTime UPDATED_END_DATE = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);
    private static final String DEFAULT_END_DATE_STR = dateTimeFormatter.format(DEFAULT_END_DATE);
    private static final String DEFAULT_ASSIGNED_TO = "AAAAA";
    private static final String UPDATED_ASSIGNED_TO = "BBBBB";
    private static final String DEFAULT_ASSIGNED_FROM = "AAAAA";
    private static final String UPDATED_ASSIGNED_FROM = "BBBBB";
    private static final String DEFAULT_LOCATION = "AAAAA";
    private static final String UPDATED_LOCATION = "BBBBB";
    private static final String DEFAULT_CENTER_COST = "AAAAA";
    private static final String UPDATED_CENTER_COST = "BBBBB";

    private static final ZonedDateTime DEFAULT_REQUEST_DATE = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneId.systemDefault());
    private static final ZonedDateTime UPDATED_REQUEST_DATE = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);
    private static final String DEFAULT_REQUEST_DATE_STR = dateTimeFormatter.format(DEFAULT_REQUEST_DATE);

    private static final ZonedDateTime DEFAULT_LAST_MODIFIED_DATE = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneId.systemDefault());
    private static final ZonedDateTime UPDATED_LAST_MODIFIED_DATE = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);
    private static final String DEFAULT_LAST_MODIFIED_DATE_STR = dateTimeFormatter.format(DEFAULT_LAST_MODIFIED_DATE);

    @Inject
    private HistorybtrRepository historybtrRepository;

    @Inject
    private HistorybtrSearchRepository historybtrSearchRepository;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    private MockMvc restHistorybtrMockMvc;

    private Historybtr historybtr;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        HistorybtrResource historybtrResource = new HistorybtrResource();
        ReflectionTestUtils.setField(historybtrResource, "historybtrSearchRepository", historybtrSearchRepository);
        ReflectionTestUtils.setField(historybtrResource, "historybtrRepository", historybtrRepository);
        this.restHistorybtrMockMvc = MockMvcBuilders.standaloneSetup(historybtrResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    @Before
    public void initTest() {
        historybtr = new Historybtr();
        historybtr.setBtrstatusbefore(DEFAULT_BTRSTATUSBEFORE);
        historybtr.setBtrstatusafter(DEFAULT_BTRSTATUSAFTER);
        historybtr.setChange_date(DEFAULT_CHANGE_DATE);
        historybtr.setChanged_by(DEFAULT_CHANGED_BY);
        historybtr.setStart_date(DEFAULT_START_DATE);
        historybtr.setEnd_date(DEFAULT_END_DATE);
        historybtr.setAssigned_to(DEFAULT_ASSIGNED_TO);
        historybtr.setAssigned_from(DEFAULT_ASSIGNED_FROM);
        historybtr.setLocation(DEFAULT_LOCATION);
        historybtr.setCenter_cost(DEFAULT_CENTER_COST);
        historybtr.setRequest_date(DEFAULT_REQUEST_DATE);
        historybtr.setLast_modified_date(DEFAULT_LAST_MODIFIED_DATE);
    }

    @Test
    @Transactional
    public void createHistorybtr() throws Exception {
        int databaseSizeBeforeCreate = historybtrRepository.findAll().size();

        // Create the Historybtr

        restHistorybtrMockMvc.perform(post("/api/historybtrs")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(historybtr)))
                .andExpect(status().isCreated());

        // Validate the Historybtr in the database
        List<Historybtr> historybtrs = historybtrRepository.findAll();
        assertThat(historybtrs).hasSize(databaseSizeBeforeCreate + 1);
        Historybtr testHistorybtr = historybtrs.get(historybtrs.size() - 1);
        assertThat(testHistorybtr.getBtrstatusbefore()).isEqualTo(DEFAULT_BTRSTATUSBEFORE);
        assertThat(testHistorybtr.getBtrstatusafter()).isEqualTo(DEFAULT_BTRSTATUSAFTER);
        assertThat(testHistorybtr.getChange_date()).isEqualTo(DEFAULT_CHANGE_DATE);
        assertThat(testHistorybtr.getChanged_by()).isEqualTo(DEFAULT_CHANGED_BY);
        assertThat(testHistorybtr.getStart_date()).isEqualTo(DEFAULT_START_DATE);
        assertThat(testHistorybtr.getEnd_date()).isEqualTo(DEFAULT_END_DATE);
        assertThat(testHistorybtr.getAssigned_to()).isEqualTo(DEFAULT_ASSIGNED_TO);
        assertThat(testHistorybtr.getAssigned_from()).isEqualTo(DEFAULT_ASSIGNED_FROM);
        assertThat(testHistorybtr.getLocation()).isEqualTo(DEFAULT_LOCATION);
        assertThat(testHistorybtr.getCenter_cost()).isEqualTo(DEFAULT_CENTER_COST);
        assertThat(testHistorybtr.getRequest_date()).isEqualTo(DEFAULT_REQUEST_DATE);
        assertThat(testHistorybtr.getLast_modified_date()).isEqualTo(DEFAULT_LAST_MODIFIED_DATE);
    }

    @Test
    @Transactional
    public void checkBtrstatusbeforeIsRequired() throws Exception {
        int databaseSizeBeforeTest = historybtrRepository.findAll().size();
        // set the field null
        historybtr.setBtrstatusbefore(null);

        // Create the Historybtr, which fails.

        restHistorybtrMockMvc.perform(post("/api/historybtrs")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(historybtr)))
                .andExpect(status().isBadRequest());

        List<Historybtr> historybtrs = historybtrRepository.findAll();
        assertThat(historybtrs).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkBtrstatusafterIsRequired() throws Exception {
        int databaseSizeBeforeTest = historybtrRepository.findAll().size();
        // set the field null
        historybtr.setBtrstatusafter(null);

        // Create the Historybtr, which fails.

        restHistorybtrMockMvc.perform(post("/api/historybtrs")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(historybtr)))
                .andExpect(status().isBadRequest());

        List<Historybtr> historybtrs = historybtrRepository.findAll();
        assertThat(historybtrs).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkChange_dateIsRequired() throws Exception {
        int databaseSizeBeforeTest = historybtrRepository.findAll().size();
        // set the field null
        historybtr.setChange_date(null);

        // Create the Historybtr, which fails.

        restHistorybtrMockMvc.perform(post("/api/historybtrs")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(historybtr)))
                .andExpect(status().isBadRequest());

        List<Historybtr> historybtrs = historybtrRepository.findAll();
        assertThat(historybtrs).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkChanged_byIsRequired() throws Exception {
        int databaseSizeBeforeTest = historybtrRepository.findAll().size();
        // set the field null
        historybtr.setChanged_by(null);

        // Create the Historybtr, which fails.

        restHistorybtrMockMvc.perform(post("/api/historybtrs")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(historybtr)))
                .andExpect(status().isBadRequest());

        List<Historybtr> historybtrs = historybtrRepository.findAll();
        assertThat(historybtrs).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkStart_dateIsRequired() throws Exception {
        int databaseSizeBeforeTest = historybtrRepository.findAll().size();
        // set the field null
        historybtr.setStart_date(null);

        // Create the Historybtr, which fails.

        restHistorybtrMockMvc.perform(post("/api/historybtrs")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(historybtr)))
                .andExpect(status().isBadRequest());

        List<Historybtr> historybtrs = historybtrRepository.findAll();
        assertThat(historybtrs).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkEnd_dateIsRequired() throws Exception {
        int databaseSizeBeforeTest = historybtrRepository.findAll().size();
        // set the field null
        historybtr.setEnd_date(null);

        // Create the Historybtr, which fails.

        restHistorybtrMockMvc.perform(post("/api/historybtrs")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(historybtr)))
                .andExpect(status().isBadRequest());

        List<Historybtr> historybtrs = historybtrRepository.findAll();
        assertThat(historybtrs).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkAssigned_toIsRequired() throws Exception {
        int databaseSizeBeforeTest = historybtrRepository.findAll().size();
        // set the field null
        historybtr.setAssigned_to(null);

        // Create the Historybtr, which fails.

        restHistorybtrMockMvc.perform(post("/api/historybtrs")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(historybtr)))
                .andExpect(status().isBadRequest());

        List<Historybtr> historybtrs = historybtrRepository.findAll();
        assertThat(historybtrs).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkAssigned_fromIsRequired() throws Exception {
        int databaseSizeBeforeTest = historybtrRepository.findAll().size();
        // set the field null
        historybtr.setAssigned_from(null);

        // Create the Historybtr, which fails.

        restHistorybtrMockMvc.perform(post("/api/historybtrs")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(historybtr)))
                .andExpect(status().isBadRequest());

        List<Historybtr> historybtrs = historybtrRepository.findAll();
        assertThat(historybtrs).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkLocationIsRequired() throws Exception {
        int databaseSizeBeforeTest = historybtrRepository.findAll().size();
        // set the field null
        historybtr.setLocation(null);

        // Create the Historybtr, which fails.

        restHistorybtrMockMvc.perform(post("/api/historybtrs")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(historybtr)))
                .andExpect(status().isBadRequest());

        List<Historybtr> historybtrs = historybtrRepository.findAll();
        assertThat(historybtrs).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkCenter_costIsRequired() throws Exception {
        int databaseSizeBeforeTest = historybtrRepository.findAll().size();
        // set the field null
        historybtr.setCenter_cost(null);

        // Create the Historybtr, which fails.

        restHistorybtrMockMvc.perform(post("/api/historybtrs")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(historybtr)))
                .andExpect(status().isBadRequest());

        List<Historybtr> historybtrs = historybtrRepository.findAll();
        assertThat(historybtrs).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkRequest_dateIsRequired() throws Exception {
        int databaseSizeBeforeTest = historybtrRepository.findAll().size();
        // set the field null
        historybtr.setRequest_date(null);

        // Create the Historybtr, which fails.

        restHistorybtrMockMvc.perform(post("/api/historybtrs")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(historybtr)))
                .andExpect(status().isBadRequest());

        List<Historybtr> historybtrs = historybtrRepository.findAll();
        assertThat(historybtrs).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkLast_modified_dateIsRequired() throws Exception {
        int databaseSizeBeforeTest = historybtrRepository.findAll().size();
        // set the field null
        historybtr.setLast_modified_date(null);

        // Create the Historybtr, which fails.

        restHistorybtrMockMvc.perform(post("/api/historybtrs")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(historybtr)))
                .andExpect(status().isBadRequest());

        List<Historybtr> historybtrs = historybtrRepository.findAll();
        assertThat(historybtrs).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllHistorybtrs() throws Exception {
        // Initialize the database
        historybtrRepository.saveAndFlush(historybtr);

        // Get all the historybtrs
        restHistorybtrMockMvc.perform(get("/api/historybtrs?sort=id,desc"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.[*].id").value(hasItem(historybtr.getId().intValue())))
                .andExpect(jsonPath("$.[*].btrstatusbefore").value(hasItem(DEFAULT_BTRSTATUSBEFORE.toString())))
                .andExpect(jsonPath("$.[*].btrstatusafter").value(hasItem(DEFAULT_BTRSTATUSAFTER.toString())))
                .andExpect(jsonPath("$.[*].change_date").value(hasItem(DEFAULT_CHANGE_DATE_STR)))
                .andExpect(jsonPath("$.[*].changed_by").value(hasItem(DEFAULT_CHANGED_BY.toString())))
                .andExpect(jsonPath("$.[*].start_date").value(hasItem(DEFAULT_START_DATE_STR)))
                .andExpect(jsonPath("$.[*].end_date").value(hasItem(DEFAULT_END_DATE_STR)))
                .andExpect(jsonPath("$.[*].assigned_to").value(hasItem(DEFAULT_ASSIGNED_TO.toString())))
                .andExpect(jsonPath("$.[*].assigned_from").value(hasItem(DEFAULT_ASSIGNED_FROM.toString())))
                .andExpect(jsonPath("$.[*].location").value(hasItem(DEFAULT_LOCATION.toString())))
                .andExpect(jsonPath("$.[*].center_cost").value(hasItem(DEFAULT_CENTER_COST.toString())))
                .andExpect(jsonPath("$.[*].request_date").value(hasItem(DEFAULT_REQUEST_DATE_STR)))
                .andExpect(jsonPath("$.[*].last_modified_date").value(hasItem(DEFAULT_LAST_MODIFIED_DATE_STR)));
    }

    @Test
    @Transactional
    public void getHistorybtr() throws Exception {
        // Initialize the database
        historybtrRepository.saveAndFlush(historybtr);

        // Get the historybtr
        restHistorybtrMockMvc.perform(get("/api/historybtrs/{id}", historybtr.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.id").value(historybtr.getId().intValue()))
            .andExpect(jsonPath("$.btrstatusbefore").value(DEFAULT_BTRSTATUSBEFORE.toString()))
            .andExpect(jsonPath("$.btrstatusafter").value(DEFAULT_BTRSTATUSAFTER.toString()))
            .andExpect(jsonPath("$.change_date").value(DEFAULT_CHANGE_DATE_STR))
            .andExpect(jsonPath("$.changed_by").value(DEFAULT_CHANGED_BY.toString()))
            .andExpect(jsonPath("$.start_date").value(DEFAULT_START_DATE_STR))
            .andExpect(jsonPath("$.end_date").value(DEFAULT_END_DATE_STR))
            .andExpect(jsonPath("$.assigned_to").value(DEFAULT_ASSIGNED_TO.toString()))
            .andExpect(jsonPath("$.assigned_from").value(DEFAULT_ASSIGNED_FROM.toString()))
            .andExpect(jsonPath("$.location").value(DEFAULT_LOCATION.toString()))
            .andExpect(jsonPath("$.center_cost").value(DEFAULT_CENTER_COST.toString()))
            .andExpect(jsonPath("$.request_date").value(DEFAULT_REQUEST_DATE_STR))
            .andExpect(jsonPath("$.last_modified_date").value(DEFAULT_LAST_MODIFIED_DATE_STR));
    }

    @Test
    @Transactional
    public void getNonExistingHistorybtr() throws Exception {
        // Get the historybtr
        restHistorybtrMockMvc.perform(get("/api/historybtrs/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateHistorybtr() throws Exception {
        // Initialize the database
        historybtrRepository.saveAndFlush(historybtr);

		int databaseSizeBeforeUpdate = historybtrRepository.findAll().size();

        // Update the historybtr
        historybtr.setBtrstatusbefore(UPDATED_BTRSTATUSBEFORE);
        historybtr.setBtrstatusafter(UPDATED_BTRSTATUSAFTER);
        historybtr.setChange_date(UPDATED_CHANGE_DATE);
        historybtr.setChanged_by(UPDATED_CHANGED_BY);
        historybtr.setStart_date(UPDATED_START_DATE);
        historybtr.setEnd_date(UPDATED_END_DATE);
        historybtr.setAssigned_to(UPDATED_ASSIGNED_TO);
        historybtr.setAssigned_from(UPDATED_ASSIGNED_FROM);
        historybtr.setLocation(UPDATED_LOCATION);
        historybtr.setCenter_cost(UPDATED_CENTER_COST);
        historybtr.setRequest_date(UPDATED_REQUEST_DATE);
        historybtr.setLast_modified_date(UPDATED_LAST_MODIFIED_DATE);

        restHistorybtrMockMvc.perform(put("/api/historybtrs")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(historybtr)))
                .andExpect(status().isOk());

        // Validate the Historybtr in the database
        List<Historybtr> historybtrs = historybtrRepository.findAll();
        assertThat(historybtrs).hasSize(databaseSizeBeforeUpdate);
        Historybtr testHistorybtr = historybtrs.get(historybtrs.size() - 1);
        assertThat(testHistorybtr.getBtrstatusbefore()).isEqualTo(UPDATED_BTRSTATUSBEFORE);
        assertThat(testHistorybtr.getBtrstatusafter()).isEqualTo(UPDATED_BTRSTATUSAFTER);
        assertThat(testHistorybtr.getChange_date()).isEqualTo(UPDATED_CHANGE_DATE);
        assertThat(testHistorybtr.getChanged_by()).isEqualTo(UPDATED_CHANGED_BY);
        assertThat(testHistorybtr.getStart_date()).isEqualTo(UPDATED_START_DATE);
        assertThat(testHistorybtr.getEnd_date()).isEqualTo(UPDATED_END_DATE);
        assertThat(testHistorybtr.getAssigned_to()).isEqualTo(UPDATED_ASSIGNED_TO);
        assertThat(testHistorybtr.getAssigned_from()).isEqualTo(UPDATED_ASSIGNED_FROM);
        assertThat(testHistorybtr.getLocation()).isEqualTo(UPDATED_LOCATION);
        assertThat(testHistorybtr.getCenter_cost()).isEqualTo(UPDATED_CENTER_COST);
        assertThat(testHistorybtr.getRequest_date()).isEqualTo(UPDATED_REQUEST_DATE);
        assertThat(testHistorybtr.getLast_modified_date()).isEqualTo(UPDATED_LAST_MODIFIED_DATE);
    }

    @Test
    @Transactional
    public void deleteHistorybtr() throws Exception {
        // Initialize the database
        historybtrRepository.saveAndFlush(historybtr);

		int databaseSizeBeforeDelete = historybtrRepository.findAll().size();

        // Get the historybtr
        restHistorybtrMockMvc.perform(delete("/api/historybtrs/{id}", historybtr.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate the database is empty
        List<Historybtr> historybtrs = historybtrRepository.findAll();
        assertThat(historybtrs).hasSize(databaseSizeBeforeDelete - 1);
    }
}
