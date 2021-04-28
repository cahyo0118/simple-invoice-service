package com.dicicip.starter.model;

import javax.persistence.*;
import java.sql.Timestamp;
import java.util.Date;

@Entity
@Table(name = "productions")
public class Production {

    public enum ProductionStatus {
        desain, konfirmasi, cetak, siap, kirim, sampai
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public Long id;
    public String status = ProductionStatus.desain.name();
    public Timestamp start_at = new Timestamp(new Date().getTime());
    public Timestamp produced_at;
    public Timestamp finish_at;
    public Integer payment_id;

}
