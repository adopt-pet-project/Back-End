package com.adoptpet.server.community.domain;


import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "CATEGORY")
public class Category {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "category_no")
    private Integer categoryNo;

    @Column(name = "category_name", nullable = false)
    private String categoryName;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "parent_category_id")
    private Category parentCategory;

    @OneToMany(mappedBy = "parentCategory", cascade = CascadeType.ALL)
    private List<Category> subCategory = new ArrayList<>();

    @Column(name = "reg_date", nullable = false)
    private LocalDateTime regDate;

    @Column(name = "reg_id", nullable = false)
    private String regId;

    @Column(name = "mod_date", nullable = false)
    private LocalDateTime modDate;

    @Column(name = "mod_id", nullable = false)
    private String modId;
}
