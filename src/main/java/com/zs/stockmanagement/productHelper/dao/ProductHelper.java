package com.zs.stockmanagement.productHelper.dao;

import com.zs.stockmanagement.exceptions.DataBaseException;
import com.zs.stockmanagement.utils.DBController;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

public class ProductHelper {

    public Map<Integer, String> getAttributes() {

        String query = """
                select attribute_id,attribute_key from attribute
                """;

        try (Connection connection = DBController.getConnection(); PreparedStatement queryPs = connection.prepareStatement(query); ResultSet attributeRs = queryPs.executeQuery()) {

            Map<Integer, String> map = new HashMap<>();
            while (attributeRs.next()) {
                map.put(attributeRs.getInt("attribute_id"), attributeRs.getString("attribute_key"));
            }
            return map;
        } catch (SQLException e) {
            throw new DataBaseException(e.getMessage());
        }
    }

    public Map<String, String> getAttributesAndValues(int variantId) {

        String query = """
                select a.attribute_key,va.attribute_value from variant_attribute va join attribute a on va.attribute_id = a.attribute_id where variant_id = ?;
                """;

        try (Connection connection = DBController.getConnection(); PreparedStatement queryPs = connection.prepareStatement(query);) {

            queryPs.setInt(1,variantId);
            try (ResultSet attributeRs = queryPs.executeQuery()) {
                Map<String, String> map = new HashMap<>();
                while (attributeRs.next()) {
                    map.put(attributeRs.getString("attribute_key"), attributeRs.getString("attribute_value"));
                }
                return map;
            }
        } catch (SQLException e) {
            throw new DataBaseException(e.getMessage());
        }
    }


}