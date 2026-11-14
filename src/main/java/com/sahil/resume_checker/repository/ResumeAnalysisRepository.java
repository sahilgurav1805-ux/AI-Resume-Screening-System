package com.sahil.resume_checker.repository;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import tools.jackson.databind.json.JsonMapper;

import java.util.List;
import java.util.Map;

@Repository
public class ResumeAnalysisRepository {

    private final JdbcTemplate jdbcTemplate ;
    private final JsonMapper jsonMapper = JsonMapper.builder().build();

    public ResumeAnalysisRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate ;

    }

    public Long saveAnalysis (
            String resumeText ,
            String jobDescription,
            String analysisResult,
            Long userId
    )



    {

        String sql = """
                INSERT INTO resume_analysis
                (resume_text , job_description , analysis_result , user_id)
                VALUES(?, ? , ? ,?)
                """;

        jdbcTemplate.update(
                sql,
                resumeText,
                jobDescription,
                analysisResult,
                userId
        );

        return jdbcTemplate.queryForObject(
                "SELECT LAST_INSERT_ID()",
                Long.class
        );
    }

    public List<Map<String , Object>> getAnalysesByUserId(Long userId) {

        String sql = """
                SELECT id, job_description , analysis_result , created_at
                FROM resume_analysis
                WHERE user_id = ?
                ORDER BY id DESC
                """;

        return jdbcTemplate.queryForList(sql , userId) ;
    }

}
