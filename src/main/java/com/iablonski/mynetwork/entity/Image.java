package com.iablonski.mynetwork.entity;

import jakarta.persistence.*;

import lombok.Data;
import lombok.NoArgsConstructor;

import net.minidev.json.annotate.JsonIgnore;
import org.hibernate.annotations.JdbcTypeCode;

import java.sql.Types;

@Data
@Entity
@NoArgsConstructor
@Table(name = "images")
public class Image {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false)
    private String name;
    @Lob
    @Column(columnDefinition = "bytea")
    @JdbcTypeCode(Types.LONGVARBINARY)
    private byte[] image;
    @JsonIgnore
    private Long userId;
    @JsonIgnore
    private Long postId;
}