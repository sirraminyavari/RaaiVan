package com.raaivan.modules.rv.beans;

import com.raaivan.modules.rv.enums.DashboardType;
import com.raaivan.util.PublicMethods;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

@Component
@Scope(scopeName = BeanDefinition.SCOPE_PROTOTYPE)
public class DashboardCount {
    private DashboardType Type;
    private Integer Count;
    private Integer NotSeen;

    public DashboardCount()
    {
        Type = DashboardType.NotSet;
    }

    public DashboardType getType() {
        return Type;
    }

    public void setType(DashboardType type) {
        Type = type;
    }

    public Integer getCount() {
        return Count;
    }

    public void setCount(Integer count) {
        Count = count;
    }

    public Integer getNotSeen() {
        return NotSeen;
    }

    public void setNotSeen(Integer notSeen) {
        NotSeen = notSeen;
    }
}
