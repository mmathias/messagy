package com.mycompany.myapp.repository;

import com.mycompany.myapp.domain.Message;
import org.joda.time.LocalDate;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;

import java.util.List;

/**
 * Spring Data JPA repository for the Message entity.
 */
public interface MessageRepository extends JpaRepository<Message,Long> {

    @Query("select message from Message message where message.user.login = ?#{principal.username}")
    List<Message> findAllForCurrentUser();

    @Query("select message.originatingCountry, message.timePlaced, sum(message.amountSell) from Message message where message.timePlaced = :timePlaced and message.originatingCountry = :originatingCountry  group by message.timePlaced, message.originatingCountry")
    List<Object[]> findOneGroupedByOriginatingCountryAndTimePlaced(@Param("originatingCountry") String originatingCountry, @Param("timePlaced") LocalDate timePlaced);


    @Query("select distinct message.originatingCountry from Message message")
    List<String> findDistinctOriginCountries();

    @Query("select distinct message.timePlaced from Message message order by message.timePlaced")
    List<LocalDate> findDistinctTimePlaced();

}
