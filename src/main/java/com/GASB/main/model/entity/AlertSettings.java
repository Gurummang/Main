package com.GASB.main.model.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AlertSettings {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "admin_id", referencedColumnName = "id", nullable = false)
    private AdminUsers adminUsers;

    @Column(columnDefinition = "TEXT")
    private String title;

    @Column(columnDefinition = "TEXT")
    private String content;

    private boolean suspicious;

    private boolean dlp;

    private boolean vt;

    @OneToMany(mappedBy = "alertSettings", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<AlertEmails> alertEmails;

}
