package com.example.EnergyService.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.springframework.data.annotation.CreatedDate;

import java.util.Date;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@ToString
@Table(name = "projects")
public class ProjectsEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(name = "project_name", nullable = false, unique = true)
    private String projectName;

    @Column(name = "access_id", nullable = false, unique = true)
    private String accessId;

    @Column(name = "access_key", nullable = false, unique = true)
    private String accessKey;

    @Column(name = "created")
    @CreatedDate
    @CreationTimestamp
    private Date created;

    public ProjectsEntity(String projectName, String accessId, String accessKey) {
        this.projectName = projectName;
        this.accessId = accessId;
        this.accessKey = accessKey;
    }
}
