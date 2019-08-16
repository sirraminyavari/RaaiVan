package com.raaivan.modules.corenetwork.beans;

import com.raaivan.util.RVBeanFactory;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

@Component
@Scope(scopeName = BeanDefinition.SCOPE_PROTOTYPE)
public class Relation {
    private Node Source;
    private Node Destination;
    private Boolean Bidirectional;

    public Relation()
    {
        Source = RVBeanFactory.getBean(Node.class);
        Destination = RVBeanFactory.getBean(Node.class);
    }

    public Node getSource() {
        return Source;
    }

    public void setSource(Node source) {
        Source = source;
    }

    public Node getDestination() {
        return Destination;
    }

    public void setDestination(Node destination) {
        Destination = destination;
    }

    public Boolean getBidirectional() {
        return Bidirectional;
    }

    public void setBidirectional(Boolean bidirectional) {
        Bidirectional = bidirectional;
    }
}
