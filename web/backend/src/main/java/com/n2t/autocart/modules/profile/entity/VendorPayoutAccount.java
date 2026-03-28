package com.n2t.autocart.modules.profile.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "vendor_payout_accounts")
public class VendorPayoutAccount {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "payout_account_id")
    private Integer payoutAccountId;

    @OneToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "vendor_id", nullable = false, unique = true)
    @JsonIgnore
    private Vendor vendor;

    @Column(name = "bank_name", nullable = false, length = 255)
    private String bankName;

    @Column(name = "account_number", nullable = false, length = 100)
    private String accountNumber;

    @Column(name = "account_holder_name", nullable = false, length = 255)
    private String accountHolderName;

    @Column(name = "branch_name", length = 255)
    private String branchName;
}
