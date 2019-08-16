package com.raaivan.util.dbutil;

import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
@Scope(scopeName = BeanDefinition.SCOPE_PROTOTYPE)
public class RVResultSet {
    private List<List<Map<String, Object>>> values = new ArrayList<>();
    private List<List<String>> columnNames = new ArrayList<>();
    private int ActiveSet = -1;
    private int ActiveRow = -1;

    public void addTable(List<String> columnNames){
        this.values.add(new ArrayList<>());
        this.columnNames.add(columnNames);
        ++ActiveSet;
        ActiveRow = -1;
    }

    public void addTable(){
        addTable(new ArrayList<>());
    }

    public boolean addRow(){
        if(ActiveSet < 0) return false;
        values.get(ActiveSet).add(new HashMap<>());
        ++ActiveRow;
        return true;
    }

    public boolean addValue(String name, Object value){
        if(ActiveSet < 0 || ActiveRow < 0 || values.get(ActiveSet).get(ActiveRow).containsKey(name)) return false;
        values.get(ActiveSet).get(ActiveRow).put(name, value instanceof Timestamp ? new DateTime((Timestamp) value).toDateTime(DateTimeZone.UTC) : value);
        return  true;
    }

    public int getTablesCount(){
        return values.size();
    }

    public int getRowsCount(int tableIndex){
        if(tableIndex < 0 || values.size() < (tableIndex + 1)) return 0;
        else return values.get(tableIndex).size();
    }

    public int getRowsCount(){
        return getRowsCount(0);
    }

    public int getColumnsCount(int tableIndex){
        if(tableIndex < 0 || Math.min(columnNames.size(), values.size()) < (tableIndex + 1)) return 0;
        else return columnNames.get(tableIndex).size();
    }

    public int getColumnsCount(){ return getColumnsCount(0); }

    public Object getValue(int tableIndex, int rowIndex, String columnName, Object defaultValue) {
        if (tableIndex < 0 || rowIndex < 0 || values.size() < (tableIndex + 1) ||
                values.get(tableIndex).size() < (rowIndex + 1) ||
                !values.get(tableIndex).get(rowIndex).containsKey(columnName)) return defaultValue;

        Object ret = values.get(tableIndex).get(rowIndex).get(columnName);

        return ret == null ? defaultValue : ret;
    }

    public Object getValue(int tableIndex, int rowIndex, String columnName) {
        return getValue(tableIndex, rowIndex, columnName, null);
    }

    public Object getValue(int rowIndex, String columnName, Object defaultValue){
        return getValue(0, rowIndex, columnName, defaultValue);
    }

    public Object getValue(int rowIndex, String columnName){
        return getValue(0, rowIndex, columnName, null);
    }

    public Object getValue(int tableIndex, int rowIndex, int columnIndex, Object defaultValue){
        if(tableIndex < 0 || values.size() < (tableIndex + 1) ||
                columnNames.get(tableIndex).size() < (columnIndex + 1)) return defaultValue;
        return getValue(tableIndex, rowIndex, columnNames.get(tableIndex).get(columnIndex), defaultValue);
    }

    public Object getValue(int tableIndex, int rowIndex, int columnIndex){
        return getValue(tableIndex, rowIndex, columnIndex, null);
    }

    public Object getValue(int rowIndex, int columnIndex, Object defaultValue){
        return getValue(0, rowIndex, columnIndex, defaultValue);
    }

    public Object getValue(int rowIndex, int columnIndex){
        return getValue(0, rowIndex, columnIndex, null);
    }
}
