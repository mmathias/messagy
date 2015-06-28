package com.mycompany.myapp.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.mycompany.myapp.domain.Message;
import com.mycompany.myapp.repository.MessageRepository;
import com.mycompany.myapp.web.rest.dto.Header;
import com.mycompany.myapp.web.rest.dto.MessageDTO;
import org.joda.time.LocalDate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.web.bind.annotation.*;

import javax.inject.Inject;
import javax.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing Message.
 */
@RestController
@RequestMapping("/api")
public class MessageResource {

    private final Logger log = LoggerFactory.getLogger(MessageResource.class);

    @Inject
    private MessageRepository messageRepository;

    @Inject
    private SimpMessageSendingOperations messagingTemplate;

    private static final String WEBSOCKET_TOPIC = "/topic/tracker";

    /**
     * POST  /messages -> Create a new message.
     */
    @RequestMapping(value = "/messages",
            method = RequestMethod.POST,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> create(@Valid @RequestBody Message message) throws URISyntaxException {
        log.debug("REST request to save Message : {}", message);
        if (message.getId() != null) {
            return ResponseEntity.badRequest().header("Failure", "A new message cannot already have an ID").build();
        }
        messageRepository.save(message);
        messagingTemplate.convertAndSend(WEBSOCKET_TOPIC, processMessages());

        return ResponseEntity.created(new URI("/api/messages/" + message.getId())).build();
    }

    /**
     * PUT  /messages -> Updates an existing message.
     */
    @RequestMapping(value = "/messages",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> update(@Valid @RequestBody Message message) throws URISyntaxException {
        log.debug("REST request to update Message : {}", message);
        if (message.getId() == null) {
            return create(message);
        }
        messageRepository.save(message);
        messagingTemplate.convertAndSend(WEBSOCKET_TOPIC, processMessages());

        return ResponseEntity.ok().build();
    }

    /**
     * GET  /messages -> get all the messages.
     */
    @RequestMapping(value = "/messages",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public List<Message> getAll() {
        log.debug("REST request to get all Messages");
        return messageRepository.findAll();
    }

    /**
     * GET  /messages/:id -> get the "id" message.
     */
    @RequestMapping(value = "/messages/{id}",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Message> get(@PathVariable Long id) {
        log.debug("REST request to get Message : {}", id);
        return Optional.ofNullable(messageRepository.findOne(id))
            .map(message -> new ResponseEntity<>(
                message,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /messages/:id -> delete the "id" message.
     */
    @RequestMapping(value = "/messages/{id}",
            method = RequestMethod.DELETE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public void delete(@PathVariable Long id) {
        log.debug("REST request to delete Message : {}", id);
        messageRepository.delete(id);

        messagingTemplate.convertAndSend(WEBSOCKET_TOPIC, processMessages());
    }

    private MessageDTO processMessages() {

        MessageDTO messageDTO = new MessageDTO();

        generateHeaders(messageDTO);

        generateData(messageDTO);

        return messageDTO;
    }

    private void generateData(MessageDTO messageDTO) {
        List<LocalDate> distinctDates = messageRepository.findDistinctTimePlaced();

        List<List<Object>> rows = new ArrayList<>();

        for (LocalDate distinctDate : distinctDates) {
            List<Object> row = new ArrayList<>();
            for (Header header : messageDTO.getHeaders()) {
                if (header.getId().equals("day")) {
                    row.add(distinctDate.toString());
                } else {
                    List<Object[]> rs = messageRepository.findOneGroupedByOriginatingCountryAndTimePlaced(header.getId(), distinctDate);

                    if (rs.size() == 0) {
                        row.add(0);
                    } else {
                        row.add(rs.get(0)[2]);
                    }
                }
            }

            rows.add(row);
        }

        messageDTO.setData(rows);
    }

    private void generateHeaders(MessageDTO messageDTO) {
        List<Header> headers = new ArrayList<>();

        Header header = new Header();
        header.setId("day");
        header.setLabel("Day");
        header.setType("string");
        headers.add(header);

        List<String> countries = messageRepository.findDistinctOriginCountries();

        for (String country : countries) {
            header = new Header();
            header.setId(country);
            header.setLabel(country);
            header.setType("number");

            headers.add(header);
        }

        messageDTO.setHeaders(headers);
    }
}
