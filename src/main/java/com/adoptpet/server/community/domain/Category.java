package com.adoptpet.server.community.domain;


import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "CATEGORY")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Category {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "category_no")
    private Integer categoryNo;

    @Column(name = "category_name")
    private String categoryName;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "parent_no")
    private Category parent;

    @OneToMany(mappedBy = "parent", cascade = CascadeType.ALL)
    private List<Category> subCategory = new ArrayList<>();

    @Column(name = "reg_date")
    private LocalDateTime regDate;

    @Column(name = "reg_id")
    private String regId;

    @Column(name = "mod_date")
    private LocalDateTime modDate;

    @Column(name = "mod_id")
    private String modId;


    //== 연관관계 메서드 ==//

    //== 생성 메서드 ==//

    //== 조회 메서드 ==//

    //== 수정 메서드 ==//

    //== 비즈니스 로직 ==//

}
