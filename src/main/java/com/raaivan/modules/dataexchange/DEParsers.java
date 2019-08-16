package com.raaivan.modules.dataexchange;

import com.raaivan.util.dbutil.RVResultSet;
import com.raaivan.util.PublicMethods;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.context.annotation.ApplicationScope;

import java.util.List;
import java.util.UUID;

@Component
@ApplicationScope
public class DEParsers {
    private PublicMethods publicMethods;

    @Autowired
    public void _setDependencies(PublicMethods publicMethods) {
        if (this.publicMethods == null) this.publicMethods = publicMethods;
    }

    public boolean updateNodesResults(RVResultSet resultSet, List<UUID> nodeIds)
    {
        try {
            Object firstResult = resultSet.getValue(0, 0);

            if (firstResult == null) return false;
            else if (firstResult instanceof Boolean) return (boolean) firstResult;

            Long lng = publicMethods.parseLong(firstResult.toString());

            boolean result = lng != null && lng > 0;

            if (resultSet.getTablesCount() > 1) {
                for (int i = 0, lnt = resultSet.getRowsCount(1); i < lnt; ++i) {
                    try {
                        UUID id = publicMethods.parseUUID((String) resultSet.getValue(1, i, "ID"));
                        if (id != null) nodeIds.add(id);
                    } catch (Exception e) {
                    }
                }
            }

            return result;
        }catch (Exception ex){
            return false;
        }
    }
}
