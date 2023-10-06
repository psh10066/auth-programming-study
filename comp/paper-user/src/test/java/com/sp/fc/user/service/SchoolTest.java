package com.sp.fc.user.service;

import com.sp.fc.user.domain.School;
import com.sp.fc.user.repository.SchoolRepository;
import com.sp.fc.user.service.helper.SchoolTestHelper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@DataJpaTest
public class SchoolTest {

    @Autowired // DataJpaTest에서는 repository만 Bean 생성
    private SchoolRepository schoolRepository;

    private SchoolService schoolService;
    private SchoolTestHelper schoolTestHelper;
    School school;

    @BeforeEach
    void before() {
        this.schoolService = new SchoolService(schoolRepository);
        this.schoolTestHelper = new SchoolTestHelper(this.schoolService);
        school = this.schoolTestHelper.createSchool("테스트 학교", "서울");
    }

    @DisplayName("1. 학교를 생성한다.")
    @Test
    void test_1() {
        List<School> list = schoolRepository.findAll();
        assertEquals(1, list.size());
        SchoolTestHelper.assertSchool(list.get(0), "테스트 학교", "서울");
    }

    @DisplayName("2. 학교 이름을 수정한다.")
    @Test
    void test_2() {
        schoolService.updateName(school.getSchoolId(), "테스트2 학교");
        assertEquals("테스트2 학교", schoolRepository.findAll().get(0).getName());
    }
}
