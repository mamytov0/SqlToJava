package com.example.tsum_services.DAO;

import com.zaxxer.hikari.HikariDataSource;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.SqlOutParameter;
import org.springframework.jdbc.core.SqlParameter;
import org.springframework.jdbc.core.simple.SimpleJdbcCall;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.io.PrintWriter;
import java.sql.SQLException;
import java.util.Collections;
import java.util.List;
import java.util.Map;

@Component
public class SqlDao {
    @Value("${wp.caller.driver}")
    private String PROPERTY_NAME_WP_DATABASE_DRIVER;
    @Value("${wp.caller.url}")
    private String PROPERTY_NAME_WP_DATABASE_URL;
    @Value("${wp.caller.username}")
    private String PROPERTY_NAME_WP_DATABASE_USERNAME;
    @Value("${wp.caller.password}")
    private String PROPERTY_NAME_WP_DATABASE_PASSWORD;
    @Value("${wp.caller.minPoolSize}")
    private String PROPERTY_NAME_WP_MINPOOLSIZE;
    @Value("${wp.caller.maxPoolSize}")
    private String PROPERTY_NAME_WP_MAXPOOLSIZE;
    @Value("${db.autoCommitOnClose}")
    private String PROPERTY_NAME_AUTO_COMMIT_ON_CLOSE;
    public JdbcTemplate jdbcTemplate;
    @PostConstruct
    public void init() {

        HikariDataSource dataSource = new HikariDataSource();

        try {
            dataSource.setLogWriter(new PrintWriter(System.out));
            dataSource.setDriverClassName(PROPERTY_NAME_WP_DATABASE_DRIVER);
            dataSource.setJdbcUrl(PROPERTY_NAME_WP_DATABASE_URL);
            dataSource.setUsername(PROPERTY_NAME_WP_DATABASE_USERNAME);
            dataSource.setPassword(PROPERTY_NAME_WP_DATABASE_PASSWORD);
            dataSource.setAutoCommit(Boolean.parseBoolean(PROPERTY_NAME_AUTO_COMMIT_ON_CLOSE));
            dataSource.setMaximumPoolSize(Integer.parseInt(PROPERTY_NAME_WP_MAXPOOLSIZE));
            dataSource.setMinimumIdle(Integer.parseInt(PROPERTY_NAME_WP_MINPOOLSIZE));
            dataSource.setPoolName("TsumService");
            dataSource.addDataSourceProperty("cachePrepStmts", "true");
            dataSource.addDataSourceProperty("prepStmtCacheSize", "250");
            dataSource.addDataSourceProperty("prepStmtCacheSqlLimit", "2048");

            this.jdbcTemplate = new JdbcTemplate(dataSource);
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    public JdbcTemplate getJdbcTemplate() {
        return jdbcTemplate;
    }
    public SimpleJdbcCall getCall(String schemaName,String procedureName) {
        SimpleJdbcCall call = new SimpleJdbcCall(jdbcTemplate)
                .withProcedureName(procedureName)
                .withSchemaName(schemaName);
        return call;
    }
    public Map<String, Object> mapExecute(String schemaName,
            String procedureName,
            Map<String, Object> mapIns,
            Map<String, Integer> mapInTypes,
            Map<String, Integer> mapOutTypes)
    {

        Map<String, Object> result;
        try {
            SimpleJdbcCall call = getCall(schemaName,procedureName).withoutProcedureColumnMetaDataAccess();

//            call.declareParameters(new SqlOutParameter("#result-set-1", java.sql.Types.VARCHAR));

            // declare in parameter sql types
            if (!mapIns.isEmpty() && !mapInTypes.isEmpty()) {
                for (Map.Entry<String, Integer> entry : mapInTypes.entrySet()) {
                    call.declareParameters(new SqlParameter(entry.getKey(), entry.getValue()));
                }
            }

            // declare out parameter sql types
            if (!mapOutTypes.isEmpty()) {
                for (Map.Entry<String, Integer> entry : mapOutTypes.entrySet()) {
                    call.declareParameters(new SqlOutParameter(entry.getKey(), entry.getValue()));
                }
            }

            result = call.execute(mapIns);

        } finally {
        }

        return result;
    }
    public List<Map<String, Object>> listExecute(String query, List<String> listParams) {

        List<Map<String,Object>> listMap = jdbcTemplate.queryForList(query);

        return listMap;
    }
    public Map<String, Object> mapExecute(String query, List<String> listParams) {

        Map<String,Object> map = jdbcTemplate.queryForMap(query);

        return map;
    }
    //template
//    String queryTemplate = "insert into CBS.CAMPUS_CUSTOMER_INFO(TX_NO, CUSTOMER_NO, CUSTOMER_INFO, CLASS_NAME, CREATED_BY) values (%s, %s, null, null, null)";
//        cbsDAO.execute(String.format(queryTemplate, txNo, customerNumber), Collections.emptyList());
}
