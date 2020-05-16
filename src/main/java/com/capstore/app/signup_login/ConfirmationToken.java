package main.java.com.capstore.app.signup_login;


import java.util.Date;
import java.util.UUID;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import main.java.com.capstore.app.models.CustomerDetails;
import main.java.com.capstore.app.models.MerchantDetails;
import main.java.com.capstore.app.models.User;

import lombok.Data;

@Entity
@Data
public class ConfirmationToken {
	@Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name="token_id")
    private long tokenid;

    @Column(name="confirmation_token")
    private String confirmationToken;

    @Temporal(TemporalType.TIMESTAMP)
    private Date createdDate;

    @OneToOne(targetEntity = CustomerDetails.class, fetch = FetchType.EAGER)
    @JoinColumn(nullable = false, name = "user_id", insertable=false,updatable=false)
    private CustomerDetails cd;
    
    @OneToOne(targetEntity = MerchantDetails.class, fetch = FetchType.EAGER)
    @JoinColumn(nullable = false, name = "user_id",insertable=false,updatable=false)
    private MerchantDetails md;
    
    public ConfirmationToken(CustomerDetails cd) {
        this.cd = cd;
        createdDate = new Date();
        confirmationToken = UUID.randomUUID().toString();
    }
    
    public ConfirmationToken(MerchantDetails md) {
        this.md = md;
        createdDate = new Date();
        confirmationToken = UUID.randomUUID().toString();
    }

}
