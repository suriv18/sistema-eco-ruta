package pe.edu.unmsm.ciudadsana.shared.testing.container;

import org.testcontainers.containers.PostgreSQLContainer;

public final class PostgresTestContainer {

    private static final String IMAGE = "postgis/postgis:17-3.5";

    @SuppressWarnings("resource")
    private static final PostgreSQLContainer<?> INSTANCE =
            new PostgreSQLContainer<>(IMAGE)
                    .withDatabaseName("ciudad_sana_test")
                    .withUsername("test")
                    .withPassword("test")
                    .withReuse(true);

    static {
        INSTANCE.start();
    }

    private PostgresTestContainer() {}

    public static PostgreSQLContainer<?> getInstance() {
        return INSTANCE;
    }

    public static String getJdbcUrl() {
        return INSTANCE.getJdbcUrl();
    }

    public static String getUsername() {
        return INSTANCE.getUsername();
    }

    public static String getPassword() {
        return INSTANCE.getPassword();
    }
}
