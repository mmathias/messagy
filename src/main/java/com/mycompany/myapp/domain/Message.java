package com.mycompany.myapp.domain;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.mycompany.myapp.domain.util.CustomLocalDateSerializer;
import com.mycompany.myapp.domain.util.ISO8601LocalDateDeserializer;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.Type;
import org.joda.time.LocalDate;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;

/**
 * A Message.
 */
@Entity
@Table(name = "MESSAGE")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class Message implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @NotNull
    @Size(min = 3, max = 3)
    @Column(name = "currency_from", length = 3, nullable = false)
    private String currencyFrom;

    @NotNull
    @Size(min = 3, max = 3)
    @Column(name = "currency_to", length = 3, nullable = false)
    private String currencyTo;

    @NotNull
    @Column(name = "amount_sell", precision=10, scale=2, nullable = false)
    private BigDecimal amountSell;

    @NotNull
    @Column(name = "amount_buy", precision=10, scale=2, nullable = false)
    private BigDecimal amountBuy;

    @NotNull
    @Column(name = "rate", precision=10, scale=2, nullable = false)
    private BigDecimal rate;

    @NotNull
    @Type(type = "org.jadira.usertype.dateandtime.joda.PersistentLocalDate")
    @JsonSerialize(using = CustomLocalDateSerializer.class)
    @JsonDeserialize(using = ISO8601LocalDateDeserializer.class)
    @Column(name = "time_placed", nullable = false)
    private LocalDate timePlaced;

    @NotNull
    @Size(min = 2, max = 2)
    @Column(name = "originating_country", length = 2, nullable = false)
    private String originatingCountry;

    @ManyToOne
    private User user;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCurrencyFrom() {
        return currencyFrom;
    }

    public void setCurrencyFrom(String currencyFrom) {
        this.currencyFrom = currencyFrom;
    }

    public String getCurrencyTo() {
        return currencyTo;
    }

    public void setCurrencyTo(String currencyTo) {
        this.currencyTo = currencyTo;
    }

    public BigDecimal getAmountSell() {
        return amountSell;
    }

    public void setAmountSell(BigDecimal amountSell) {
        this.amountSell = amountSell;
    }

    public BigDecimal getAmountBuy() {
        return amountBuy;
    }

    public void setAmountBuy(BigDecimal amountBuy) {
        this.amountBuy = amountBuy;
    }

    public BigDecimal getRate() {
        return rate;
    }

    public void setRate(BigDecimal rate) {
        this.rate = rate;
    }

    public LocalDate getTimePlaced() {
        return timePlaced;
    }

    public void setTimePlaced(LocalDate timePlaced) {
        this.timePlaced = timePlaced;
    }

    public String getOriginatingCountry() {
        return originatingCountry;
    }

    public void setOriginatingCountry(String originatingCountry) {
        this.originatingCountry = originatingCountry;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        Message message = (Message) o;

        if ( ! Objects.equals(id, message.id)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "Message{" +
                "id=" + id +
                ", currencyFrom='" + currencyFrom + "'" +
                ", currencyTo='" + currencyTo + "'" +
                ", amountSell='" + amountSell + "'" +
                ", amountBuy='" + amountBuy + "'" +
                ", rate='" + rate + "'" +
                ", timePlaced='" + timePlaced + "'" +
                ", originatingCountry='" + originatingCountry + "'" +
                '}';
    }
}
