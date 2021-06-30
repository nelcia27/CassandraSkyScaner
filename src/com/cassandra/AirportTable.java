package com.cassandra;

import com.datastax.oss.driver.api.core.CqlSession;
import com.datastax.oss.driver.api.core.cql.*;
import com.datastax.oss.driver.api.core.type.DataTypes;

import com.datastax.oss.driver.api.querybuilder.insert.RegularInsert;
import com.datastax.oss.driver.api.querybuilder.schema.CreateTable;
import com.schema.Airport;

import static com.datastax.oss.driver.api.querybuilder.SchemaBuilder.*;
import static com.datastax.oss.driver.api.querybuilder.QueryBuilder.*;

public class AirportTable {

    private String tableName = "airport";
    private String keyspace;
    private CqlSession session;

    public AirportTable(String keyspace, CqlSession session) {
        this.keyspace = keyspace;
        this.session = session;
    }

    public void create() {
        CreateTable create = createTable(keyspace, tableName)
                .ifNotExists()
                .withPartitionKey("id", DataTypes.INT)
                .withColumn("x_position", DataTypes.FLOAT)
                .withColumn("y_position", DataTypes.FLOAT)
                .withColumn("name", DataTypes.TEXT)
                .withColumn("country", DataTypes.TEXT)
                .withColumn("city", DataTypes.TEXT);

        SimpleStatement statement = create.build();
        session.execute(statement);

    }

    public void insert(int id, float x_position, float y_position,
                       String name, String country, String city) {
        RegularInsert insert = insertInto(keyspace, tableName)
                .value("id", literal(id))
                .value("x_position", literal(x_position))
                .value("y_position", literal(y_position))
                .value("name", literal(name))
                .value("country", literal(country))
                .value("city", literal(city));

        SimpleStatement statement = insert.build();
        session.execute(statement);
    }

    public Airport select_airport(int id) {
        ResultSet rs;

        SimpleStatement statement = selectFrom(keyspace, tableName)
                .all()
                .whereColumn("id").isEqualTo(literal(id))
                .build();

        rs = session.execute(statement);

        Row res = rs.one();

        return new Airport(id, res.getFloat("x_position"), res.getFloat("y_position"),
                res.getString("name"), res.getString("country"), res.getString("city"));

    }
}


