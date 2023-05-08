package example.pipe.cicd.config;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.JpaVendorAdapter;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.sql.DataSource;

@Configuration
@EnableTransactionManagement // 트랜잭션 관리 기능을 활성화하는 애너테이션
//@EnableJpaRepositories(basePackages = {"example.pipe.cicd.repository"}) // JPA 리포지토리를 활성화하는 애너테이션이다.
public class JpaConfiguration {


    @Bean
    public LocalContainerEntityManagerFactoryBean entityManagerFactory(
            // 이름이 dataSource인 Bean을 주입 받는다.
            @Qualifier("dataSource") DataSource dataSource) {

        LocalContainerEntityManagerFactoryBean entityManagerFactory
                = new LocalContainerEntityManagerFactoryBean();

        // DataSource를 주입받은 dataSource로 설정한다.
        entityManagerFactory.setDataSource(dataSource);
        // JPA 엔티티 클래스가 포함된 패키지를 설정한다.
        entityManagerFactory.setPackagesToScan("example.pipe.cicd");
        // JPA 벤더 어뎁터를 설정한다.
        entityManagerFactory.setJpaVendorAdapter(jpaVendorAdapter());
        // 영속성 유닛의 이름을 entityManager로 설정한다.
        entityManagerFactory.setPersistenceUnitName("entityManager");

        return entityManagerFactory;

    }

    private JpaVendorAdapter jpaVendorAdapter() {
        HibernateJpaVendorAdapter hibernateJpaVendorAdapter = new HibernateJpaVendorAdapter();
        // DDL 생성 기능을 비활성화
        hibernateJpaVendorAdapter.setGenerateDdl(false);
        // SQL 쿼리를 로깅하지 않도록 설정
        hibernateJpaVendorAdapter.setShowSql(false);
        // SQL 방언을 MySQL 5 Inno DB 방언으로 설정
        hibernateJpaVendorAdapter.setDatabasePlatform("org.hibernate.dialect.MySQL5InnoDBDialect");
        return hibernateJpaVendorAdapter;
    }

    @Bean
    public PlatformTransactionManager transactionManager (
            // 이름이 entityManager인 Bean을 주입받는다.
            @Qualifier("entityManagerFactory") LocalContainerEntityManagerFactoryBean entityManagerFactory) {
        JpaTransactionManager jpaTransactionManager = new JpaTransactionManager();
        // 주입받은 entityManagerFactory의 객체를 설정한다 -> 트랜잭션 매니저가 올바른 엔티티 매니저 팩토리를 사용하여 트랜잭션을 관리할 수 있다.
        jpaTransactionManager.setEntityManagerFactory(entityManagerFactory.getObject());
        return jpaTransactionManager;
    }
}
