package com.raaivan.modules.corenetwork.beans;

import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
@Scope(scopeName = BeanDefinition.SCOPE_PROTOTYPE)
public class NodesCount {
    private Integer Order;
    private Integer ReverseOrder;
    private UUID NodeTypeID;
    private String NodeTypeAdditionalID;
    private String TypeName;
    private Integer Count;

    public Integer getOrder() {
        return Order;
    }

    public void setOrder(Integer order) {
        Order = order;
    }

    public Integer getReverseOrder() {
        return ReverseOrder;
    }

    public void setReverseOrder(Integer reverseOrder) {
        ReverseOrder = reverseOrder;
    }

    public UUID getNodeTypeID() {
        return NodeTypeID;
    }

    public void setNodeTypeID(UUID nodeTypeID) {
        NodeTypeID = nodeTypeID;
    }

    public String getNodeTypeAdditionalID() {
        return NodeTypeAdditionalID;
    }

    public void setNodeTypeAdditionalID(String nodeTypeAdditionalID) {
        NodeTypeAdditionalID = nodeTypeAdditionalID;
    }

    public String getTypeName() {
        return TypeName;
    }

    public void setTypeName(String typeName) {
        TypeName = typeName;
    }

    public Integer getCount() {
        return Count;
    }

    public void setCount(Integer count) {
        Count = count;
    }
}
