package com.mycompany.myapp.web.rest;

import com.mycompany.myapp.Application;
import com.mycompany.myapp.domain.Message;
import com.mycompany.myapp.repository.MessageRepository;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import static org.hamcrest.Matchers.hasItem;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.IntegrationTest;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.http.MediaType;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import org.joda.time.LocalDate;
import java.math.BigDecimal;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Test class for the MessageResource REST controller.
 *
 * @see MessageResource
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
@WebAppConfiguration
@IntegrationTest
public class MessageResourceTest {

    private static final String DEFAULT_CURRENCY_FROM = "BRL";
    private static final String UPDATED_CURRENCY_FROM = "USD";
    private static final String DEFAULT_CURRENCY_TO = "EUR";
    private static final String UPDATED_CURRENCY_TO = "BRL";

    private static final BigDecimal DEFAULT_AMOUNT_SELL = new BigDecimal(0);
    private static final BigDecimal UPDATED_AMOUNT_SELL = new BigDecimal(1);

    private static final BigDecimal DEFAULT_AMOUNT_BUY = new BigDecimal(0);
    private static final BigDecimal UPDATED_AMOUNT_BUY = new BigDecimal(1);

    private static final BigDecimal DEFAULT_RATE = new BigDecimal(0);
    private static final BigDecimal UPDATED_RATE = new BigDecimal(1);

    private static final LocalDate DEFAULT_TIME_PLACED = new LocalDate(0L);
    private static final LocalDate UPDATED_TIME_PLACED = new LocalDate();
    private static final String DEFAULT_ORIGINATING_COUNTRY = "BR";
    private static final String UPDATED_ORIGINATING_COUNTRY = "FR";

    @Inject
    private MessageRepository messageRepository;

    @Inject
    private SimpMessageSendingOperations messagingTemplate;

    private MockMvc restMessageMockMvc;

    private Message message;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        MessageResource messageResource = new MessageResource();
        ReflectionTestUtils.setField(messageResource, "messageRepository", messageRepository);
        ReflectionTestUtils.setField(messageResource, "messagingTemplate", messagingTemplate);
        this.restMessageMockMvc = MockMvcBuilders.standaloneSetup(messageResource).build();
    }

    @Before
    public void initTest() {
        message = new Message();
        message.setCurrencyFrom(DEFAULT_CURRENCY_FROM);
        message.setCurrencyTo(DEFAULT_CURRENCY_TO);
        message.setAmountSell(DEFAULT_AMOUNT_SELL);
        message.setAmountBuy(DEFAULT_AMOUNT_BUY);
        message.setRate(DEFAULT_RATE);
        message.setTimePlaced(DEFAULT_TIME_PLACED);
        message.setOriginatingCountry(DEFAULT_ORIGINATING_COUNTRY);
    }

    @Test
    @Transactional
    public void createMessage() throws Exception {
        int databaseSizeBeforeCreate = messageRepository.findAll().size();

        // Create the Message
        restMessageMockMvc.perform(post("/api/messages")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(message)))
                .andExpect(status().isCreated());

        // Validate the Message in the database
        List<Message> messages = messageRepository.findAll();
        assertThat(messages).hasSize(databaseSizeBeforeCreate + 1);
        Message testMessage = messages.get(messages.size() - 1);
        assertThat(testMessage.getCurrencyFrom()).isEqualTo(DEFAULT_CURRENCY_FROM);
        assertThat(testMessage.getCurrencyTo()).isEqualTo(DEFAULT_CURRENCY_TO);
        assertThat(testMessage.getAmountSell()).isEqualTo(DEFAULT_AMOUNT_SELL);
        assertThat(testMessage.getAmountBuy()).isEqualTo(DEFAULT_AMOUNT_BUY);
        assertThat(testMessage.getRate()).isEqualTo(DEFAULT_RATE);
        assertThat(testMessage.getTimePlaced()).isEqualTo(DEFAULT_TIME_PLACED);
        assertThat(testMessage.getOriginatingCountry()).isEqualTo(DEFAULT_ORIGINATING_COUNTRY);
    }

    @Test
    @Transactional
    public void checkCurrencyFromIsRequired() throws Exception {
        // Validate the database is empty
        assertThat(messageRepository.findAll()).hasSize(0);
        // set the field null
        message.setCurrencyFrom(null);

        // Create the Message, which fails.
        restMessageMockMvc.perform(post("/api/messages")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(message)))
                .andExpect(status().isBadRequest());

        // Validate the database is still empty
        List<Message> messages = messageRepository.findAll();
        assertThat(messages).hasSize(0);
    }

    @Test
    @Transactional
    public void checkCurrencyToIsRequired() throws Exception {
        // Validate the database is empty
        assertThat(messageRepository.findAll()).hasSize(0);
        // set the field null
        message.setCurrencyTo(null);

        // Create the Message, which fails.
        restMessageMockMvc.perform(post("/api/messages")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(message)))
                .andExpect(status().isBadRequest());

        // Validate the database is still empty
        List<Message> messages = messageRepository.findAll();
        assertThat(messages).hasSize(0);
    }

    @Test
    @Transactional
    public void checkAmountSellIsRequired() throws Exception {
        // Validate the database is empty
        assertThat(messageRepository.findAll()).hasSize(0);
        // set the field null
        message.setAmountSell(null);

        // Create the Message, which fails.
        restMessageMockMvc.perform(post("/api/messages")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(message)))
                .andExpect(status().isBadRequest());

        // Validate the database is still empty
        List<Message> messages = messageRepository.findAll();
        assertThat(messages).hasSize(0);
    }

    @Test
    @Transactional
    public void checkAmountBuyIsRequired() throws Exception {
        // Validate the database is empty
        assertThat(messageRepository.findAll()).hasSize(0);
        // set the field null
        message.setAmountBuy(null);

        // Create the Message, which fails.
        restMessageMockMvc.perform(post("/api/messages")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(message)))
                .andExpect(status().isBadRequest());

        // Validate the database is still empty
        List<Message> messages = messageRepository.findAll();
        assertThat(messages).hasSize(0);
    }

    @Test
    @Transactional
    public void checkRateIsRequired() throws Exception {
        // Validate the database is empty
        assertThat(messageRepository.findAll()).hasSize(0);
        // set the field null
        message.setRate(null);

        // Create the Message, which fails.
        restMessageMockMvc.perform(post("/api/messages")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(message)))
                .andExpect(status().isBadRequest());

        // Validate the database is still empty
        List<Message> messages = messageRepository.findAll();
        assertThat(messages).hasSize(0);
    }

    @Test
    @Transactional
    public void checkTimePlacedIsRequired() throws Exception {
        // Validate the database is empty
        assertThat(messageRepository.findAll()).hasSize(0);
        // set the field null
        message.setTimePlaced(null);

        // Create the Message, which fails.
        restMessageMockMvc.perform(post("/api/messages")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(message)))
                .andExpect(status().isBadRequest());

        // Validate the database is still empty
        List<Message> messages = messageRepository.findAll();
        assertThat(messages).hasSize(0);
    }

    @Test
    @Transactional
    public void checkOriginatingCountryIsRequired() throws Exception {
        // Validate the database is empty
        assertThat(messageRepository.findAll()).hasSize(0);
        // set the field null
        message.setOriginatingCountry(null);

        // Create the Message, which fails.
        restMessageMockMvc.perform(post("/api/messages")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(message)))
                .andExpect(status().isBadRequest());

        // Validate the database is still empty
        List<Message> messages = messageRepository.findAll();
        assertThat(messages).hasSize(0);
    }

    @Test
    @Transactional
    public void getAllMessages() throws Exception {
        // Initialize the database
        messageRepository.saveAndFlush(message);

        // Get all the messages
        restMessageMockMvc.perform(get("/api/messages"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.[*].id").value(hasItem(message.getId().intValue())))
                .andExpect(jsonPath("$.[*].currencyFrom").value(hasItem(DEFAULT_CURRENCY_FROM.toString())))
                .andExpect(jsonPath("$.[*].currencyTo").value(hasItem(DEFAULT_CURRENCY_TO.toString())))
                .andExpect(jsonPath("$.[*].amountSell").value(hasItem(DEFAULT_AMOUNT_SELL.intValue())))
                .andExpect(jsonPath("$.[*].amountBuy").value(hasItem(DEFAULT_AMOUNT_BUY.intValue())))
                .andExpect(jsonPath("$.[*].rate").value(hasItem(DEFAULT_RATE.intValue())))
                .andExpect(jsonPath("$.[*].timePlaced").value(hasItem(DEFAULT_TIME_PLACED.toString())))
                .andExpect(jsonPath("$.[*].originatingCountry").value(hasItem(DEFAULT_ORIGINATING_COUNTRY.toString())));
    }

    @Test
    @Transactional
    public void getMessage() throws Exception {
        // Initialize the database
        messageRepository.saveAndFlush(message);

        // Get the message
        restMessageMockMvc.perform(get("/api/messages/{id}", message.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.id").value(message.getId().intValue()))
            .andExpect(jsonPath("$.currencyFrom").value(DEFAULT_CURRENCY_FROM.toString()))
            .andExpect(jsonPath("$.currencyTo").value(DEFAULT_CURRENCY_TO.toString()))
            .andExpect(jsonPath("$.amountSell").value(DEFAULT_AMOUNT_SELL.intValue()))
            .andExpect(jsonPath("$.amountBuy").value(DEFAULT_AMOUNT_BUY.intValue()))
            .andExpect(jsonPath("$.rate").value(DEFAULT_RATE.intValue()))
            .andExpect(jsonPath("$.timePlaced").value(DEFAULT_TIME_PLACED.toString()))
            .andExpect(jsonPath("$.originatingCountry").value(DEFAULT_ORIGINATING_COUNTRY.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingMessage() throws Exception {
        // Get the message
        restMessageMockMvc.perform(get("/api/messages/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateMessage() throws Exception {
        // Initialize the database
        messageRepository.saveAndFlush(message);

		int databaseSizeBeforeUpdate = messageRepository.findAll().size();

        // Update the message
        message.setCurrencyFrom(UPDATED_CURRENCY_FROM);
        message.setCurrencyTo(UPDATED_CURRENCY_TO);
        message.setAmountSell(UPDATED_AMOUNT_SELL);
        message.setAmountBuy(UPDATED_AMOUNT_BUY);
        message.setRate(UPDATED_RATE);
        message.setTimePlaced(UPDATED_TIME_PLACED);
        message.setOriginatingCountry(UPDATED_ORIGINATING_COUNTRY);
        restMessageMockMvc.perform(put("/api/messages")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(message)))
                .andExpect(status().isOk());

        // Validate the Message in the database
        List<Message> messages = messageRepository.findAll();
        assertThat(messages).hasSize(databaseSizeBeforeUpdate);
        Message testMessage = messages.get(messages.size() - 1);
        assertThat(testMessage.getCurrencyFrom()).isEqualTo(UPDATED_CURRENCY_FROM);
        assertThat(testMessage.getCurrencyTo()).isEqualTo(UPDATED_CURRENCY_TO);
        assertThat(testMessage.getAmountSell()).isEqualTo(UPDATED_AMOUNT_SELL);
        assertThat(testMessage.getAmountBuy()).isEqualTo(UPDATED_AMOUNT_BUY);
        assertThat(testMessage.getRate()).isEqualTo(UPDATED_RATE);
        assertThat(testMessage.getTimePlaced()).isEqualTo(UPDATED_TIME_PLACED);
        assertThat(testMessage.getOriginatingCountry()).isEqualTo(UPDATED_ORIGINATING_COUNTRY);
    }

    @Test
    @Transactional
    public void deleteMessage() throws Exception {
        // Initialize the database
        messageRepository.saveAndFlush(message);

		int databaseSizeBeforeDelete = messageRepository.findAll().size();

        // Get the message
        restMessageMockMvc.perform(delete("/api/messages/{id}", message.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate the database is empty
        List<Message> messages = messageRepository.findAll();
        assertThat(messages).hasSize(databaseSizeBeforeDelete - 1);
    }
}
