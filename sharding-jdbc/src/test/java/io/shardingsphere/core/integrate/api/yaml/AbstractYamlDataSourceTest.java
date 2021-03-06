/*
 * Copyright 1999-2015 dangdang.com.
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * </p>
 */

package io.shardingsphere.core.integrate.api.yaml;

import org.apache.commons.dbcp2.BasicDataSource;
import org.h2.tools.RunScript;
import org.junit.BeforeClass;

import javax.sql.DataSource;
import java.io.File;
import java.io.InputStreamReader;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.List;

public abstract class AbstractYamlDataSourceTest {
    
    @BeforeClass
    public static void createSchema() throws SQLException {
        for (String each : getSchemaFiles()) {
            RunScript.execute(createDataSource(getFileName(each)).getConnection(), new InputStreamReader(AbstractYamlDataSourceTest.class.getClassLoader().getResourceAsStream(each)));
        }
    }
    
    protected static DataSource createDataSource(final String dsName) {
        BasicDataSource result = new BasicDataSource();
        result.setDriverClassName(org.h2.Driver.class.getName());
        result.setUrl(String.format("jdbc:h2:mem:%s;DB_CLOSE_DELAY=-1;DATABASE_TO_UPPER=false;MODE=MYSQL", dsName));
        result.setUsername("sa");
        result.setMaxTotal(100);
        return result;
    }
    
    private static String getFileName(final String dataSetFile) {
        String fileName = new File(dataSetFile).getName();
        if (-1 == fileName.lastIndexOf(".")) {
            return fileName;
        }
        return fileName.substring(0, fileName.lastIndexOf("."));
    }
    
    private static List<String> getSchemaFiles() {
        return Arrays.asList("integrate/api/schema/sharding/db0.sql", "integrate/api/schema/sharding/db1.sql", "integrate/api/schema/ms/db_master.sql", 
                "integrate/api/schema/ms/db_slave_0.sql", "integrate/api/schema/ms/db_slave_1.sql", "integrate/api/schema/sharding_ms/db0_master.sql", 
                "integrate/api/schema/sharding_ms/db1_master.sql", "integrate/api/schema/sharding_ms/db0_slave.sql", "integrate/api/schema/sharding_ms/db1_slave.sql");
    }
}
