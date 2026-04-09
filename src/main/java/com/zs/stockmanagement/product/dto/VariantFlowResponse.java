package com.zs.stockmanagement.product.dto;

import com.zs.stockmanagement.product.model.FlowEntry;

import java.util.List;
import java.util.Map;

public class VariantFlowResponse {

    private int variantId;
    private Map<String, String> attributes;
    private List<FlowEntry> flow;

    public VariantFlowResponse(Map<String, String> attributes, List<FlowEntry> flow, int variantId) {
        this.attributes = attributes;
        this.flow = flow;
        this.variantId = variantId;
    }

    public VariantFlowResponse() {

    }

    public Map<String, String> getAttributes() {
        return attributes;
    }

    public void setAttributes(Map<String, String> attributes) {
        this.attributes = attributes;
    }

    public List<FlowEntry> getFlow() {
        return flow;
    }

    public void setFlow(List<FlowEntry> flow) {
        this.flow = flow;
    }

    public int getVariantId() {
        return variantId;
    }

    public void setVariantId(int variantId) {
        this.variantId = variantId;
    }

    @Override
    public String toString() {
        return "VariantFlowResponse{" +
                "attributes=" + attributes +
                ", variantId=" + variantId +
                ", flow=" + flow +
                '}';
    }
}