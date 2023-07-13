

package nl.anouk.bikerental.models;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@Entity
@Table(name="users")
public class User {

        @Id
        @Column(nullable = false, unique = true)
        private String username;

        @Column(nullable = false, length = 255)
        private String password;

        @Column(nullable = false)
        private boolean enabled = true;

        @Column
        private String apikey;

        @Column
        private String email;


        @OneToMany(
                targetEntity = Authority.class,
                mappedBy = "username",
                cascade = CascadeType.ALL,
                orphanRemoval = true,
                fetch = FetchType.EAGER)
        private Set<Authority> authorities = new HashSet<>();


        @OneToOne
        @JoinColumn(name = "customer_id")
        private Customer customer;



        public String getUsername() { return username; }
        public void setUsername(String username) {
            this.username = username;
        }
        public String getPassword() {
            return password;
        }
        public void setPassword(String password) {
            this.password = password;
        }

        public String getApikey() { return apikey; }
        public void setApikey(String apikey) { this.apikey = apikey; }
        public String getEmail() { return email; }
        public void setEmail(String email) { this.email = email;}

        public Set<Authority> getAuthorities() { return authorities; }
        public void addAuthority(Authority authority) {
            this.authorities.add(authority);
        }
        public void removeAuthority(Authority authority) {
            this.authorities.remove(authority);
        }

    }


