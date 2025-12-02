package co.com.management.r2dbc.persistence;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name="clients")
public class ClientDao {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @Column(length = 20, nullable = false )
    private String documentNumber;

    @Column(length = 2, nullable = false)
    private String documentType;

    @Column(length = 50, nullable = false)
    private String firstName;

    @Column(length = 50, nullable = false)
    private String lastName;

    private Boolean state;

    @Column(length = 25, nullable = false)
    private String email;

    @Column(length = 80, nullable = false)
    private String phone;

    @Column(length = 150, nullable = false)
    private String address;

    @Column(name= "created_at")
    private LocalDateTime createdDate;

    @Column(name= "updated_at")
    private LocalDateTime updatedDate;

    @PrePersist
    public void prePersist() {
        createdDate = LocalDateTime.now();
    }

    @PreUpdate
    public void preUpdate() {
        updatedDate = LocalDateTime.now();
    }
}
