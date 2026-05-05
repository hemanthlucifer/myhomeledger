package com.myhomeledger.app.costcenter.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.UuidGenerator;

import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "project",indexes = {
        @Index(name = "project_user_index",columnList = "projectId,userId")
})
public class Project {

    @Id
    @UuidGenerator
    private UUID projectId;

    @Column(name = "project_name", nullable = false)
    private String projectName;

    @Column(name = "user_id", nullable = false)
    private UUID userId;

}
