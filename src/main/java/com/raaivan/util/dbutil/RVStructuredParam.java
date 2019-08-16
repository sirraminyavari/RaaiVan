package com.raaivan.util.dbutil;

import com.microsoft.sqlserver.jdbc.SQLServerDataTable;
import com.microsoft.sqlserver.jdbc.SQLServerPreparedStatement;
import org.joda.time.DateTime;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.sql.Date;
import java.sql.Timestamp;
import java.sql.Types;
import java.util.UUID;

@Component
@Scope(scopeName = BeanDefinition.SCOPE_PROTOTYPE)
public class RVStructuredParam {
    private SQLServerDataTable dataTable;
    private String name;

    public RVStructuredParam(String name){
        try {
            this.name = name;
            this.dataTable = new SQLServerDataTable();
        }catch (Exception ex){
        }
    }

    public RVStructuredParam addColumnMetaData(String columnName, Class dataType) {
        try {
            if (dataType == boolean.class || dataType == Boolean.class)
                dataTable.addColumnMetadata(columnName, Types.BOOLEAN);
            else if (dataType == double.class || dataType == Double.class)
                dataTable.addColumnMetadata(columnName, Types.BIGINT);
            else if (dataType == int.class || dataType == Integer.class)
                dataTable.addColumnMetadata(columnName, Types.INTEGER);
            else if (dataType == float.class || dataType == Float.class)
                dataTable.addColumnMetadata(columnName, Types.FLOAT);
            else if (dataType == String.class)
                dataTable.addColumnMetadata(columnName, Types.NVARCHAR);
            else if (dataType == char.class || dataType == Character.class)
                dataTable.addColumnMetadata(columnName, Types.CHAR);
            else if (dataType == DateTime.class || dataType == Date.class)
                dataTable.addColumnMetadata(columnName, Types.DATE);
            else if (dataType == UUID.class)
                dataTable.addColumnMetadata(columnName, Types.VARCHAR);
        } catch (Exception ex) {
        }

        return this;
    }

    public RVStructuredParam addRow(Object... values) {
        try {
            Object[] items = new Object[values.length];

            for(int i = 0; i < values.length; ++i)
                items[i] = values[i] instanceof Timestamp ? new DateTime((Timestamp) values[i]) : values[i];

            dataTable.addRow(items);
        } catch (Exception ex) {
        }

        return this;
    }

    public boolean setParameter(int index, SQLServerPreparedStatement statement) {
        try {
            statement.setStructured(index, "[dbo].[" + this.name + "]", this.dataTable);
            return true;
        } catch (Exception ex) {
            return false;
        }
    }
}
