package com.btapp.web.rest;

import com.btapp.Application;
import com.btapp.domain.Btr;
import com.btapp.repository.BtrRepository;
import com.btapp.service.BtrService;
import com.btapp.repository.search.BtrSearchRepository;

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
 * Test class for the BtrResource REST controller.
 *
 * @see BtrResource
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
@WebAppConfiguration
@IntegrationTest
public class BtrResourceIntTest {

    private static final DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'").withZone(ZoneId.of("Z"));

    private static final String DEFAULT_STATUS = "AAAAA";
    private static final String UPDATED_STATUS = "BBBBB";

    private static final ZonedDateTime DEFAULT_START_DATE = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneId.systemDefault());
    private static final ZonedDateTime UPDATED_START_DATE = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);
    private static final String DEFAULT_START_DATE_STR = dateTimeFormatter.format(DEFAULT_START_DATE);

    private static final ZonedDateTime DEFAULT_END_DATE = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneId.systemDefault());
    private static final ZonedDateTime UPDATED_END_DATE = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);
    private static final String DEFAULT_END_DATE_STR = dateTimeFormatter.format(DEFAULT_END_DATE);
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

    private static final Double DEFAULT_SUMA_TOTALA = 1D;
    private static final Double UPDATED_SUMA_TOTALA = 2D;

    @Inject
    private BtrRepository btrRepository;

    @Inject
    private BtrService btrService;

    @Inject
    private BtrSearchRepository btrSearchRepository;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    private MockMvc restBtrMockMvc;

    private Btr btr;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        BtrResource btrResource = new BtrResource();
        ReflectionTestUtils.setField(btrResource, "btrService", btrService);
        this.restBtrMockMvc = MockMvcBuilders.standaloneSetup(btrResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    @Before
    public void initTest() {
        btrSearchRepository.deleteAll();
        btr = new Btr();
        btr.setStatus(DEFAULT_STATUS);
        btr.setStart_date(DEFAULT_START_DATE);
        btr.setEnd_date(DEFAULT_END_DATE);
        btr.setLocation(DEFAULT_LOCATION);
        btr.setCenter_cost(DEFAULT_CENTER_COST);
        btr.setRequest_date(DEFAULT_REQUEST_DATE);
        btr.setLast_modified_date(DEFAULT_LAST_MODIFIED_DATE);
        btr.setSuma_totala(DEFAULT_SUMA_TOTALA);
    }

    @Test
    @Transactional
    public void createBtr() throws Exception {
        int databaseSizeBeforeCreate = btrRepository.findAll().size();

        // Create the Btr

        restBtrMockMvc.perform(post("/api/btrs")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(btr)))
                .andExpect(status().isCreated());

        // Validate the Btr in the database
        List<Btr> btrs = btrRepository.findAll();
        assertThat(btrs).hasSize(databaseSizeBeforeCreate + 1);
        Btr testBtr = btrs.get(btrs.size() - 1);
        assertThat(testBtr.getStatus()).isEqualTo(DEFAULT_STATUS);
        assertThat(testBtr.getStart_date()).isEqualTo(DEFAULT_START_DATE);
        assertThat(testBtr.getEnd_date()).isEqualTo(DEFAULT_END_DATE);
        assertThat(testBtr.getLocation()).isEqualTo(DEFAULT_LOCATION);
        assertThat(testBtr.getCenter_cost()).isEqualTo(DEFAULT_CENTER_COST);
        assertThat(testBtr.getRequest_date()).isEqualTo(DEFAULT_REQUEST_DATE);
        assertThat(testBtr.getLast_modified_date()).isEqualTo(DEFAULT_LAST_MODIFIED_DATE);
        assertThat(testBtr.getSuma_totala()).isEqualTo(DEFAULT_SUMA_TOTALA);

        // Validate the Btr in ElasticSearch
        Btr btrEs = (Btr) btrSearchRepository.findOne(testBtr.getId());
        assertThat(btrEs).isEqualToComparingFieldByField(testBtr);
    }

    @Test
    @Transactional
    public void checkStatusIsRequired() throws Exception {
        int databaseSizeBeforeTest = btrRepository.findAll().size();
        // set the field null
        btr.setStatus(null);

        // Create the Btr, which fails.

        restBtrMockMvc.perform(post("/api/btrs")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(btr)))
                .andExpect(status().isBadRequest());

        List<Btr> btrs = btrRepository.findAll();
        assertThat(btrs).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkStart_dateIsRequired() throws Exception {
        int databaseSizeBeforeTest = btrRepository.findAll().size();
        // set the field null
        btr.setStart_date(null);

        // Create the Btr, which fails.

        restBtrMockMvc.perform(post("/api/btrs")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(btr)))
                .andExpect(status().isBadRequest());

        List<Btr> btrs = btrRepository.findAll();
        assertThat(btrs).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkEnd_dateIsRequired() throws Exception {
        int databaseSizeBeforeTest = btrRepository.findAll().size();
        // set the field null
        btr.setEnd_date(null);

        // Create the Btr, which fails.

        restBtrMockMvc.perform(post("/api/btrs")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(btr)))
                .andExpect(status().isBadRequest());

        List<Btr> btrs = btrRepository.findAll();
        assertThat(btrs).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkLocationIsRequired() throws Exception {
        int databaseSizeBeforeTest = btrRepository.findAll().size();
        // set the field null
        btr.setLocation(null);

        // Create the Btr, which fails.

        restBtrMockMvc.perform(post("/api/btrs")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(btr)))
                .andExpect(status().isBadRequest());

        List<Btr> btrs = btrRepository.findAll();
        assertThat(btrs).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkCenter_costIsRequired() throws Exception {
        int databaseSizeBeforeTest = btrRepository.findAll().size();
        // set the field null
        btr.setCenter_cost(null);

        // Create the Btr, which fails.

        restBtrMockMvc.perform(post("/api/btrs")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(btr)))
                .andExpect(status().isBadRequest());

        List<Btr> btrs = btrRepository.findAll();
        assertThat(btrs).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkRequest_dateIsRequired() throws Exception {
        int databaseSizeBeforeTest = btrRepository.findAll().size();
        // set the field null
        btr.setRequest_date(null);

        // Create the Btr, which fails.

        restBtrMockMvc.perform(post("/api/btrs")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(btr)))
                .andExpect(status().isBadRequest());

        List<Btr> btrs = btrRepository.findAll();
        assertThat(btrs).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkLast_modified_dateIsRequired() throws Exception {
        int databaseSizeBeforeTest = btrRepository.findAll().size();
        // set the field null
        btr.setLast_modified_date(null);

        // Create the Btr, which fails.

        restBtrMockMvc.perform(post("/api/btrs")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(btr)))
                .andExpect(status().isBadRequest());

        List<Btr> btrs = btrRepository.findAll();
        assertThat(btrs).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkSuma_totalaIsRequired() throws Exception {
        int databaseSizeBeforeTest = btrRepository.findAll().size();
        // set the field null
        btr.setSuma_totala(null);

        // Create the Btr, which fails.

        restBtrMockMvc.perform(post("/api/btrs")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(btr)))
                .andExpect(status().isBadRequest());

        List<Btr> btrs = btrRepository.findAll();
        assertThat(btrs).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllBtrs() throws Exception {
        // Initialize the database
        btrRepository.saveAndFlush(btr);

        // Get all the btrs
        restBtrMockMvc.perform(get("/api/btrs?sort=id,desc"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.[*].id").value(hasItem(btr.getId().intValue())))
                .andExpect(jsonPath("$.[*].status").value(hasItem(DEFAULT_STATUS.toString())))
                .andExpect(jsonPath("$.[*].start_date").value(hasItem(DEFAULT_START_DATE_STR)))
                .andExpect(jsonPath("$.[*].end_date").value(hasItem(DEFAULT_END_DATE_STR)))
                .andExpect(jsonPath("$.[*].location").value(hasItem(DEFAULT_LOCATION.toString())))
                .andExpect(jsonPath("$.[*].center_cost").value(hasItem(DEFAULT_CENTER_COST.toString())))
                .andExpect(jsonPath("$.[*].request_date").value(hasItem(DEFAULT_REQUEST_DATE_STR)))
                .andExpect(jsonPath("$.[*].last_modified_date").value(hasItem(DEFAULT_LAST_MODIFIED_DATE_STR)))
                .andExpect(jsonPath("$.[*].suma_totala").value(hasItem(DEFAULT_SUMA_TOTALA.doubleValue())));
    }

    @Test
    @Transactional
    public void getBtr() throws Exception {
        // Initialize the database
        btrRepository.saveAndFlush(btr);

        // Get the btr
        restBtrMockMvc.perform(get("/api/btrs/{id}", btr.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.id").value(btr.getId().intValue()))
            .andExpect(jsonPath("$.status").value(DEFAULT_STATUS.toString()))
            .andExpect(jsonPath("$.start_date").value(DEFAULT_START_DATE_STR))
            .andExpect(jsonPath("$.end_date").value(DEFAULT_END_DATE_STR))
            .andExpect(jsonPath("$.location").value(DEFAULT_LOCATION.toString()))
            .andExpect(jsonPath("$.center_cost").value(DEFAULT_CENTER_COST.toString()))
            .andExpect(jsonPath("$.request_date").value(DEFAULT_REQUEST_DATE_STR))
            .andExpect(jsonPath("$.last_modified_date").value(DEFAULT_LAST_MODIFIED_DATE_STR))
            .andExpect(jsonPath("$.suma_totala").value(DEFAULT_SUMA_TOTALA.doubleValue()));
    }

    @Test
    @Transactional
    public void getNonExistingBtr() throws Exception {
        // Get the btr
        restBtrMockMvc.perform(get("/api/btrs/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateBtr() throws Exception {
        // Initialize the database
        btrService.save(btr);

        int databaseSizeBeforeUpdate = btrRepository.findAll().size();

        // Update the btr
        Btr updatedBtr = new Btr();
        updatedBtr.setId(btr.getId());
        updatedBtr.setStatus(UPDATED_STATUS);
        updatedBtr.setStart_date(UPDATED_START_DATE);
        updatedBtr.setEnd_date(UPDATED_END_DATE);
        updatedBtr.setLocation(UPDATED_LOCATION);
        updatedBtr.setCenter_cost(UPDATED_CENTER_COST);
        updatedBtr.setRequest_date(UPDATED_REQUEST_DATE);
        updatedBtr.setLast_modified_date(UPDATED_LAST_MODIFIED_DATE);
        updatedBtr.setSuma_totala(UPDATED_SUMA_TOTALA);

        restBtrMockMvc.perform(put("/api/btrs")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(updatedBtr)))
                .andExpect(status().isOk());

        // Validate the Btr in the database
        List<Btr> btrs = btrRepository.findAll();
        assertThat(btrs).hasSize(databaseSizeBeforeUpdate);
        Btr testBtr = btrs.get(btrs.size() - 1);
        assertThat(testBtr.getStatus()).isEqualTo(UPDATED_STATUS);
        assertThat(testBtr.getStart_date()).isEqualTo(UPDATED_START_DATE);
        assertThat(testBtr.getEnd_date()).isEqualTo(UPDATED_END_DATE);
        assertThat(testBtr.getLocation()).isEqualTo(UPDATED_LOCATION);
        assertThat(testBtr.getCenter_cost()).isEqualTo(UPDATED_CENTER_COST);
        assertThat(testBtr.getRequest_date()).isEqualTo(UPDATED_REQUEST_DATE);
        assertThat(testBtr.getLast_modified_date()).isEqualTo(UPDATED_LAST_MODIFIED_DATE);
        assertThat(testBtr.getSuma_totala()).isEqualTo(UPDATED_SUMA_TOTALA);

        // Validate the Btr in ElasticSearch
        Btr btrEs = (Btr) btrSearchRepository.findOne(testBtr.getId());
        assertThat(btrEs).isEqualToComparingFieldByField(testBtr);
    }

    @Test
    @Transactional
    public void deleteBtr() throws Exception {
        // Initialize the database
        btrService.save(btr);

        int databaseSizeBeforeDelete = btrRepository.findAll().size();

        // Get the btr
        restBtrMockMvc.perform(delete("/api/btrs/{id}", btr.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate ElasticSearch is empty
        boolean btrExistsInEs = btrSearchRepository.exists(btr.getId());
        assertThat(btrExistsInEs).isFalse();

        // Validate the database is empty
        List<Btr> btrs = btrRepository.findAll();
        assertThat(btrs).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void searchBtr() throws Exception {
        // Initialize the database
        btrService.save(btr);

        // Search the btr
        restBtrMockMvc.perform(get("/api/_search/btrs?query=id:" + btr.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.[*].id").value(hasItem(btr.getId().intValue())))
            .andExpect(jsonPath("$.[*].status").value(hasItem(DEFAULT_STATUS.toString())))
            .andExpect(jsonPath("$.[*].start_date").value(hasItem(DEFAULT_START_DATE_STR)))
            .andExpect(jsonPath("$.[*].end_date").value(hasItem(DEFAULT_END_DATE_STR)))
            .andExpect(jsonPath("$.[*].location").value(hasItem(DEFAULT_LOCATION.toString())))
            .andExpect(jsonPath("$.[*].center_cost").value(hasItem(DEFAULT_CENTER_COST.toString())))
            .andExpect(jsonPath("$.[*].request_date").value(hasItem(DEFAULT_REQUEST_DATE_STR)))
            .andExpect(jsonPath("$.[*].last_modified_date").value(hasItem(DEFAULT_LAST_MODIFIED_DATE_STR)))
            .andExpect(jsonPath("$.[*].suma_totala").value(hasItem(DEFAULT_SUMA_TOTALA.doubleValue())));
    }
}
