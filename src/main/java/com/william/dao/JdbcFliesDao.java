package com.william.dao;

import com.william.model.Flies;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.jdbc.SQLWarningException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;


import javax.sql.DataSource;
import java.util.ArrayList;
import java.util.List;

public class JdbcFliesDao implements FliesDao{

    private JdbcTemplate jdbcTemplate;

    public JdbcFliesDao(DataSource dataSource){
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }

    @Override
    public List<Flies> getFliesBySeason(String season) {
        List<Flies> flies = new ArrayList<>();
        String sql = "Select * from " +
                "flies where season = ?";
        SqlRowSet results = jdbcTemplate.queryForRowSet(sql, season);
        while(results.next()){
            Flies fly = mapRowToFlies(results);
            flies.add(fly);
        }
        return flies;
    }

    @Override
    public Flies getRecipe(int flyId) {
        Flies flies = null;
        String sql = "select recipe from flies " +
                " where fly_id = ?";
        SqlRowSet results = jdbcTemplate.queryForRowSet(sql, flyId);
        if(results.next()){
            flies = mapRowToRecipe(results);
        }
        return flies;
    }

    private Flies mapRowToRecipe(SqlRowSet results) {
        Flies flies = new Flies();
        flies.setWebsite(results.getString("recipe"));
        return flies;
    }

    @Override
    public List<Flies> getAllFlies() {
        List<Flies> flies = new ArrayList<Flies>();
        String sql = "select * from flies";
        SqlRowSet results = jdbcTemplate.queryForRowSet(sql);
        while(results.next()){
            Flies fly = mapRowToFlies(results);
            flies.add(fly);
        }
        return flies;
    }

    @Override
    public Flies getFly(int flyId) {
        Flies flies = null;
        String sql = "Select name, creator, season, target " +
                "from flies " +
                "where flies.fly_id = ?";
        SqlRowSet results = jdbcTemplate.queryForRowSet(sql, flyId);
        if(results.next()){
            flies = mapRowToOneFly(results);
        }
        return flies;
    }

    private Flies mapRowToOneFly(SqlRowSet results) {
        Flies flies = new Flies();
        flies.setName(results.getString("name"));
        flies.setCreator(results.getString("creator"));
        flies.setSeason(results.getString("season"));
        flies.setTarget(results.getString("target"));
        return flies;

    }


    @Override
    public Flies createFly(Flies fly)  {
        String sql = "insert into flies(name, creator, season, target, recipe) " +
                "values( ?, ?, ?, ?, ?) returning fly_id";
        Integer newId = jdbcTemplate.queryForObject(sql, Integer.class, fly.getName(), fly.getCreator(),
                fly.getSeason(), fly.getTarget(), fly.getWebsite());
        return getFly(newId);
    }


    @Override
    public void updateFly(Flies fly) {
        String sql = "update flies " +
                "set name = ?, creator = ?, season = ?, target = ?, recipe = ? " +
                "where fly_id = ?";
        jdbcTemplate.update(sql, fly.getName(), fly.getCreator(), fly.getSeason(), fly.getTarget(), fly.getWebsite(), fly.getFlyId());
    }

    @Override
    public void deleteFly(int flyId) {
        String sql = "delete from flies where fly_id = ?;";
        jdbcTemplate.update(sql, flyId);

    }

    private Flies mapRowToFlies(SqlRowSet results) {
        Flies flies = new Flies();
        flies.setFlyId(results.getInt("fly_id"));
        flies.setName(results.getString("name"));
        flies.setCreator(results.getString("creator"));
        flies.setSeason(results.getString("season"));
        flies.setWebsite(results.getString("recipe"));
        return flies;
    }
}
