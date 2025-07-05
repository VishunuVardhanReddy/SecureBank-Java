package com.securebank.bankserver.config;

import org.hibernate.dialect.Dialect;
import org.hibernate.dialect.identity.IdentityColumnSupport;
import org.hibernate.dialect.identity.IdentityColumnSupportImpl;
import org.hibernate.type.descriptor.jdbc.*;
import org.hibernate.boot.model.TypeContributions;
import org.hibernate.service.ServiceRegistry;
import org.hibernate.type.descriptor.jdbc.spi.JdbcTypeRegistry;

import java.sql.Types;

public class SQLiteDialect extends Dialect {

    public SQLiteDialect() {
        super();
    }

    @Override
    public void contributeTypes(TypeContributions typeContributions, ServiceRegistry serviceRegistry) {
        JdbcTypeRegistry jdbcTypeRegistry = typeContributions.getTypeConfiguration().getJdbcTypeRegistry();

        jdbcTypeRegistry.addDescriptor(Types.INTEGER, new IntegerJdbcType());
        jdbcTypeRegistry.addDescriptor(Types.VARCHAR, new VarcharJdbcType());
        jdbcTypeRegistry.addDescriptor(Types.FLOAT, new FloatJdbcType());
        jdbcTypeRegistry.addDescriptor(Types.DOUBLE, new DoubleJdbcType());
        jdbcTypeRegistry.addDescriptor(Types.BOOLEAN, new BooleanJdbcType());
        jdbcTypeRegistry.addDescriptor(Types.BLOB, new BinaryJdbcType());

    }

    @Override
    public IdentityColumnSupport getIdentityColumnSupport() {
        return new IdentityColumnSupportImpl() {
            @Override
            public boolean supportsIdentityColumns() {
                return true;
            }

            @Override
            public String getIdentitySelectString(String table, String column, int type) {
                return "select last_insert_rowid()";
            }

            @Override
            public String getIdentityColumnString(int type) {
                return "integer";
            }
        };
    }

    @Override
    public boolean supportsIfExistsBeforeTableName() {
        return true;
    }

    @Override
    public boolean supportsTemporaryTables() {
        return true;
    }

    @Override
    public boolean supportsCascadeDelete() {
        return false;
    }

}