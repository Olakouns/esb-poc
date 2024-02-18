package sn.esmt.hlr_api.models;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.JdbcTypeCode;

import java.sql.Types;
import java.util.Date;
import java.util.List;
import java.util.UUID;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class SubscriberUser {
    @Id
    @GeneratedValue(generator = "uuid2")
    @GenericGenerator(name = "uuid2", strategy = "uuid2")
    @JdbcTypeCode(Types.VARCHAR)
    private UUID id;
    private String name;
    private String phoneNumber;
    private String imsi;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss.SSS")
    private Date createdAt;
    @Enumerated(value = EnumType.STRING)
    private SubscriberType subscriberType;
    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private List<TLService> tlServices;
}
