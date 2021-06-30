package com.cassandra;

import com.datastax.oss.driver.api.core.CqlSession;
import com.datastax.oss.driver.api.core.cql.*;
import com.datastax.oss.driver.api.core.type.DataTypes;

import com.datastax.oss.driver.api.querybuilder.insert.RegularInsert;
import com.datastax.oss.driver.api.querybuilder.schema.CreateTable;
import com.schema.Observation;

import java.util.ArrayList;
import java.util.List;

import static com.datastax.oss.driver.api.querybuilder.SchemaBuilder.*;
import static com.datastax.oss.driver.api.querybuilder.QueryBuilder.*;

public class ObservationTable {

    private String tableName = "observation";
    private String keyspace;
    private CqlSession session;

    public ObservationTable(String keyspace, CqlSession session){
        this.keyspace = keyspace;
        this.session = session;
    }

    public void create() {

        CreateTable create = createTable(keyspace, tableName)
                .ifNotExists()
                .withPartitionKey("id", DataTypes.INT)
                .withPartitionKey("plane_name", DataTypes.TEXT)
                .withPartitionKey("number_of_flight", DataTypes.INT)
                .withColumn("height", DataTypes.FLOAT)
                .withColumn("x_position", DataTypes.FLOAT)
                .withColumn("y_position", DataTypes.FLOAT)
                .withColumn("plane_id", DataTypes.TEXT)
                .withColumn("destination_airport_id", DataTypes.INT)
                .withColumn("time", DataTypes.BIGINT);

        SimpleStatement statement = create.build();
        session.execute(statement);

    }

    public void insert(int id, float height, float x_position, float y_position,
                       int number_of_flight, String plane_name, String plane_id,
                       long time){
        RegularInsert insert = insertInto(keyspace, tableName)
                .value("id", literal(id))
                .value("plane_name", literal(plane_name))
                .value("number_of_flight", literal(number_of_flight))
                .value("height", literal(height))
                .value("x_position", literal(x_position))
                .value("y_position", literal(y_position))
                .value("plane_id", literal(plane_id))
                .value("time", literal(time));

        SimpleStatement statement = insert.build();
        session.execute(statement);
    }

    public List<Observation> select_plane(String plane_name){
        ResultSet rs;
        List<Observation> observations = new ArrayList<>();

        SimpleStatement statement = selectFrom(keyspace, tableName)
                .all()
                .whereColumn("plane_name").isEqualTo(literal(plane_name))
                .allowFiltering()
                .build();

        rs = session.execute(statement);

        rs.forEach(row -> {
            observations.add(new Observation(row.getInt("id"), row.getFloat("height"),
                            row.getFloat("x_position"), row.getFloat("y_position"),
                            row.getInt("number_of_flight"), plane_name, row.getString("plane_id"),
                            row.getInt("destination_airport_id"), row.getLong("time")));
        });

        return observations;

    }

    public List<Observation> select_flight(int id){

        ResultSet rs;
        List<Observation> observations = new ArrayList<>();

        SimpleStatement statement = selectFrom(keyspace, tableName)
                .all()
                .whereColumn("number_of_flight").isEqualTo(literal(id))
                .allowFiltering()
                .build();

        rs = session.execute(statement);

        rs.forEach(row -> {
            observations.add(new Observation(row.getInt("id"), row.getFloat("height"),
                    row.getFloat("x_position"), row.getFloat("y_position"),
                    row.getInt("number_of_flight"), row.getString("plane_name"), row.getString("plane_id"),
                    id, row.getLong("time")));
        });

        return observations;

    }


}
