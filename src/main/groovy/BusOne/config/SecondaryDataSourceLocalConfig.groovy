package BusOne.config

import BusOne.common.constant.SpringProfile
import org.springframework.boot.autoconfigure.jdbc.DataSourceBuilder
import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.context.annotation.Profile
import org.springframework.context.annotation.PropertySource

import javax.sql.DataSource
import java.nio.file.Files
import java.nio.file.Paths

/**
 * Configures secondary data source using externalized database properties when running with "local" Spring profile.
 *
 * The external properties file contains the following info:-
 *
 * secondary.datasource.poolName=secondary
 * secondary.datasource.jdbcUrl=jdbc:h2:mem:test;MODE=MSSQLServer;DATABASE_TO_UPPER=false
 * secondary.datasource.username=
 * secondary.datasource.password=
 * secondary.datasource.connectionTestQuery=select 1
 */
// TODO Additional data source - Enable "local" profile
// @Configuration
@Profile(SpringProfile.LOCAL)
@PropertySource('file:${SPRING_BOOT_PROPERTIES_DIR}/IntercitySmartBus.properties')
class SecondaryDataSourceLocalConfig extends SecondaryDataSourceAbstractConfig {

    // ensure file path defined under @PropertySource is valid
    SecondaryDataSourceLocalConfig() {
        final String dir = System.getenv('SPRING_BOOT_PROPERTIES_DIR')

        assert dir?.trim(),
                'SPRING_BOOT_PROPERTIES_DIR environment variable containing directory path (without trailing slash) must exist'

        assert Files.exists(Paths.get("${dir}/IntercitySmartBus.properties")),
                "${dir}/IntercitySmartBus.properties containing externalized datasource properties must exist"
    }

    @Override
    @ConfigurationProperties(prefix = 'secondary.datasource')
    DataSource dataSource() {
        return DataSourceBuilder.create().build()
    }
}
