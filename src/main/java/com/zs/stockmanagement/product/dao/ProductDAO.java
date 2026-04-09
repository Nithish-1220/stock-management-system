package com.zs.stockmanagement.product.dao;

import com.zs.stockmanagement.product.model.Product;
import com.zs.stockmanagement.product.model.Variant;
import com.zs.stockmanagement.utils.DBController;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ProductDAO {

    public List<Product> getProducts(int shopId, int branchId) {
        String productQuery = "select p.product_id,p.product_name,c.category_name,b2.brand_name,p.model " +
                "From products p " +
                "Left join branches b on b.branch_id = p.branch_id " +
                "left join categories c on p.category_id = c.category_id " +
                "left join brand b2 on p.brand_id = b2.brand_id " +
                "WHERE b.shop_id = ? AND p.branch_id = ? AND is_deleted = FALSE;";

        try (Connection connection = DBController.getConnection();
             PreparedStatement productPs = connection.prepareStatement(productQuery)) {

            productPs.setInt(1, shopId);
            productPs.setInt(2, branchId);

            try (ResultSet productRs = productPs.executeQuery()) {
                List<Product> products = new ArrayList<>();
                while (productRs.next()) {
                    Product product = new Product();
                    product.setProductId(productRs.getInt("product_id"));
                    product.setProductName(productRs.getString("product_name"));
                    product.setCategoryName(productRs.getString("category_name"));
                    product.setBrandName(productRs.getString("brand_name"));
                    product.setModel(productRs.getString("model"));
                    products.add(product);
                }
                return products;
            }
        } catch (SQLException e) {
            System.err.println(e.getMessage());
            return null;
        }
    }

    public Product getProducts(int shopId, int branchId, int productId) {
        String productQuery = "select p.product_id,p.product_name,c.category_name,b2.brand_name,p.model " +
                "From products p " +
                "Left join branches b on b.branch_id = p.branch_id " +
                "left join categories c on p.category_id = c.category_id " +
                "left join brand b2 on p.brand_id = b2.brand_id " +
                "WHERE b.shop_id = ? AND p.branch_id = ? AND product_id = ? AND is_deleted = FALSE;";

        try (Connection connection = DBController.getConnection();
             PreparedStatement productPs = connection.prepareStatement(productQuery)) {

            productPs.setInt(1, shopId);
            productPs.setInt(2, branchId);
            productPs.setInt(3, productId);

            try (ResultSet productRs = productPs.executeQuery()) {
                Product product = new Product();
                if (productRs.next()) {
                    product.setProductId(productRs.getInt("product_id"));
                    product.setProductName(productRs.getString("product_name"));
                    product.setCategoryName(productRs.getString("category_name"));
                    product.setBrandName(productRs.getString("brand_name"));
                    product.setModel(productRs.getString("model"));
                }
                return product;
            }
        } catch (SQLException e) {
            System.err.println(e.getMessage());
            return null;
        }
    }


    public List<Variant> getVariants(int shopId, int branchId, int productId) {
        String variantQuery = "select v.variant_id, v.price, v.mrp " +
                "from variants v " +
                "join products p on  v.product_id = p.product_id " +
                "join branches b on p.branch_id = b.branch_id " +
                "where b.shop_id=? AND b.branch_id = ? AND p.product_id=? AND v.is_deleted = false;";

        String attributeQuery = "select a.attribute_key,va.attribute_value " +
                "from variant_attribute va " +
                "left join attribute a on va.attribute_id = a.attribute_id " +
                "where variant_id = ? ;";

        try (Connection connection = DBController.getConnection();
             PreparedStatement variantPs = connection.prepareStatement(variantQuery);
             PreparedStatement attributePs = connection.prepareStatement(attributeQuery)) {

            variantPs.setInt(1, shopId);
            variantPs.setInt(2, branchId);
            variantPs.setInt(3, productId);

            try (ResultSet variantRs = variantPs.executeQuery()) {
                List<Variant> variants = new ArrayList<>();
                while (variantRs.next()) {
                    Variant variant = new Variant();
                    variant.setVariantId(variantRs.getInt("variant_id"));
                    variant.setPrice(variantRs.getDouble("price"));
                    variant.setMrp(variantRs.getDouble("mrp"));

                    attributePs.setInt(1, variant.getVariantId());
                    try (ResultSet attributeRs = attributePs.executeQuery()) {
                        while (attributeRs.next()) {
                            variant.getAttributes().put(attributeRs.getString("attribute_key"), attributeRs.getString("attribute_value"));
                        }
                    }
                    variants.add(variant);
                }
                return variants;
            }
        } catch (SQLException e) {
            System.err.println(e.getMessage());
            return null;
        }
    }


    public Variant getVariants(int shopId, int branchId, int productId, int variantId) {
        String variantQuery = "select v.variant_id, v.price, v.mrp " +
                "from variants v " +
                "join products p on  v.product_id = p.product_id " +
                "join branches b on p.branch_id = b.branch_id " +
                "where b.shop_id=? AND b.branch_id = ? AND p.product_id=? AND v.variant_id = ? AND v.is_deleted = false;";

        String attributeQuery = "select a.attribute_key,va.attribute_value " +
                "from variant_attribute va " +
                "left join attribute a on va.attribute_id = a.attribute_id " +
                "where variant_id = ?;";

        try (Connection connection = DBController.getConnection();
             PreparedStatement variantPs = connection.prepareStatement(variantQuery);
             PreparedStatement attributePs = connection.prepareStatement(attributeQuery)) {

            variantPs.setInt(1, shopId);
            variantPs.setInt(2, branchId);
            variantPs.setInt(3, productId);
            variantPs.setInt(4, variantId);

            try (ResultSet variantRs = variantPs.executeQuery()) {
                Variant variant = new Variant();
                if (variantRs.next()) {
                    variant.setVariantId(variantRs.getInt("variant_id"));
                    variant.setPrice(variantRs.getDouble("price"));
                    variant.setMrp(variantRs.getDouble("mrp"));

                    attributePs.setInt(1, variant.getVariantId());
                    try (ResultSet attributeRs = attributePs.executeQuery()) {
                        while (attributeRs.next()) {
                            variant.getAttributes().put(attributeRs.getString("attribute_key"), attributeRs.getString("attribute_value"));
                        }
                    }
                }
                return variant;
            }
        } catch (SQLException e) {
            System.err.println(e.getMessage());
            return null;
        }
    }

    public int getCategoryId(String categoryName) {
        String categoryQuery = "select category_id from categories where category_name = ?";
        try (Connection connection = DBController.getConnection();
             PreparedStatement categoryPs = connection.prepareStatement(categoryQuery)) {

            categoryPs.setString(1, categoryName);
            try (ResultSet categoryRs = categoryPs.executeQuery()) {
                if (categoryRs.next()) {
                    return categoryRs.getInt("category_id");
                }
                return -1;
            }
        } catch (SQLException e) {
            System.err.println(e.getMessage());
            return -1;
        }
    }

    public int getBrandId(String brandName) {
        String brandQuery = "select brand_id from brand where brand_name = ?";
        try (Connection connection = DBController.getConnection();
             PreparedStatement brandPs = connection.prepareStatement(brandQuery)) {

            brandPs.setString(1, brandName);
            try (ResultSet brandRs = brandPs.executeQuery()) {
                if (brandRs.next()) {
                    return brandRs.getInt("brand_id");
                }
                return -1;
            }
        } catch (SQLException e) {
            System.err.println(e.getMessage());
            return -1;
        }
    }

    public int addBrand(String brandName) {
        String brandQuery = "insert into brand(brand_name) values(?);";
        try (Connection connection = DBController.getConnection();
             PreparedStatement brandPs = connection.prepareStatement(brandQuery, PreparedStatement.RETURN_GENERATED_KEYS)) {

            brandPs.setString(1, brandName);
            brandPs.executeUpdate();
            try (ResultSet brandRs = brandPs.getGeneratedKeys()) {
                if (brandRs.next()) {
                    return brandRs.getInt(1);
                }
                return -1;
            }
        } catch (SQLException e) {
            System.err.println(e.getMessage());
            return -1;
        }
    }

    public int getAttributeId(String attributeName) {
        String attributeQuery = "select attribute_id from attribute where attribute_key =?;";
        try (Connection connection = DBController.getConnection();
             PreparedStatement attributePs = connection.prepareStatement(attributeQuery)) {

            attributePs.setString(1, attributeName);
            try (ResultSet attributeRs = attributePs.executeQuery()) {
                if (attributeRs.next()) {
                    return attributeRs.getInt("attribute_id");
                }
                return -1;
            }
        } catch (SQLException e) {
            System.err.println(e.getMessage());
            return -1;
        }
    }

    public int addAttribute(String attributeName) {
        String attributeQuery = "insert into attribute(attribute_key) values (?);";
        try (Connection connection = DBController.getConnection();
             PreparedStatement attributePs = connection.prepareStatement(attributeQuery, PreparedStatement.RETURN_GENERATED_KEYS)) {

            attributePs.setString(1, attributeName);
            attributePs.executeUpdate();
            try (ResultSet attributeRs = attributePs.getGeneratedKeys()) {
                if (attributeRs.next()) {
                    return attributeRs.getInt(1);
                }
                return -1;
            }
        } catch (SQLException e) {
            System.err.println(e.getMessage());
            return -1;
        }
    }


    public Product addProduct(int shopId, int branchId, Product product, List<Variant> variants) {

        String checkShopQuery = "select shop_id from branches where branch_id=?;";
        String productQuery = "insert into products(branch_id,product_name,category_id,brand_id,model) values(?,?,?,?,?);";
        String variantInsert = "insert into variants(product_id, price, mrp) values(?,?,?)";
        String variantAttributeInsert = "insert into variant_attribute(variant_id, attribute_id, attribute_value) VALUES (?,?,?);";

        int categoryId = getCategoryId(product.getCategoryName());
        if (categoryId == -1) return null;
        int existingBrandId = getBrandId(product.getBrandName());
        int brandId = existingBrandId != -1 ? existingBrandId : addBrand(product.getBrandName());

        Integer productId = null;

        try (Connection connection = DBController.getConnection()) {
            connection.setAutoCommit(false); // Start transaction

            try {
                try (PreparedStatement checkShopPs = connection.prepareStatement(checkShopQuery)) {
                    checkShopPs.setInt(1, branchId);
                    try (ResultSet checkShopRs = checkShopPs.executeQuery()) {
                        if (!checkShopRs.next() || checkShopRs.getInt("shop_id") != shopId) {
                            connection.rollback();
                            return null;
                        }
                    }
                }

                try (PreparedStatement productPs = connection.prepareStatement(productQuery, PreparedStatement.RETURN_GENERATED_KEYS)) {
                    productPs.setInt(1, branchId);
                    productPs.setString(2, product.getProductName());
                    productPs.setInt(3, categoryId);
                    productPs.setInt(4, brandId);
                    productPs.setString(5, product.getModel());
                    productPs.executeUpdate();

                    try (ResultSet productRs = productPs.getGeneratedKeys()) {
                        if (productRs.next()) {
                            productId = productRs.getInt(1);
                        } else {
                            throw new SQLException("Failed to generate Product ID");
                        }
                    }
                }

                try (PreparedStatement variantPs = connection.prepareStatement(variantInsert, PreparedStatement.RETURN_GENERATED_KEYS);
                     PreparedStatement variantAttributePs = connection.prepareStatement(variantAttributeInsert)) {

                    for (Variant variant : variants) {
                        variantPs.setInt(1, productId);
                        variantPs.setDouble(2, variant.getPrice());
                        variantPs.setDouble(3, variant.getMrp());
                        variantPs.executeUpdate();

                        Integer variantId = null;
                        try (ResultSet variantRs = variantPs.getGeneratedKeys()) {
                            if (variantRs.next()) variantId = variantRs.getInt(1);
                        }

                        if (variantId != null) {
                            variantAttributePs.setInt(1, variantId);
                            for (String attributeKey : variant.getAttributes().keySet()) {
                                int existingAttributeId = getAttributeId(attributeKey);
                                int attributeId = existingAttributeId != -1 ? existingAttributeId : addAttribute(attributeKey);

                                variantAttributePs.setInt(2, attributeId);
                                variantAttributePs.setString(3, variant.getAttributes().get(attributeKey));
                                variantAttributePs.executeUpdate();
                            }
                        }
                    }
                }
                connection.commit();
                product.setProductId(productId);
                return product;

            } catch (SQLException e) {
                connection.rollback();
                System.err.println(e.getMessage());
                return null;
            } finally {
                connection.setAutoCommit(true);
            }

        } catch (SQLException e) {
            System.err.println(e.getMessage());
            return null;
        }
    }

    //TODO addProducts
    public List<Product> addProducts() {
        return null;
    }

    public boolean deleteProduct(int shopId, int branchId, int productId) {
        String productQuery = "UPDATE products p join branches b on p.branch_id = b.branch_id " +
                "SET is_deleted = TRUE " +
                "WHERE p.product_id = ? AND p.branch_id = ? AND b.shop_id = ?;";

        try (Connection connection = DBController.getConnection();
             PreparedStatement productPs = connection.prepareStatement(productQuery)) {

            productPs.setInt(1, productId);
            productPs.setInt(2, branchId);
            productPs.setInt(3, shopId);
            productPs.executeUpdate();
            return true;
        } catch (SQLException e) {
            System.err.println(e.getMessage());
            return false;
        }
    }

    public Variant addVariant(int shopId, int branchId, int productId, Variant variant) {
        String checkQuery =
                "SELECT p.product_id " +
                        "FROM products p " +
                        "JOIN branches b ON p.branch_id = b.branch_id " +
                        "WHERE p.product_id = ? " +
                        "AND p.branch_id = ? " +
                        "AND b.shop_id = ? " +
                        "AND p.is_deleted = FALSE";
        String variantQuery = "insert into variants(product_id, price, mrp) values (?,?,?);";
        String variantAttributeQuery = "insert into variant_attribute(variant_id, attribute_id, attribute_value) values (?,?,?);";

        try (Connection connection = DBController.getConnection()) {
            connection.setAutoCommit(false);

            try {
                try (PreparedStatement checkPs = connection.prepareStatement(checkQuery)) {
                    checkPs.setInt(1, productId);
                    checkPs.setInt(2, branchId);
                    checkPs.setInt(3, shopId);
                    try (ResultSet rs = checkPs.executeQuery()) {
                        if (!rs.next()) {
                            connection.rollback();
                            return null;
                        }
                    }
                }

                try (PreparedStatement variantPs = connection.prepareStatement(variantQuery, PreparedStatement.RETURN_GENERATED_KEYS)) {
                    variantPs.setInt(1, productId);
                    variantPs.setDouble(2, variant.getPrice());
                    variantPs.setDouble(3, variant.getMrp());
                    variantPs.executeUpdate();

                    try (ResultSet variantRs = variantPs.getGeneratedKeys()) {
                        if (variantRs.next()) variant.setVariantId(variantRs.getInt(1));
                    }
                }

                try (PreparedStatement variantAttributePs = connection.prepareStatement(variantAttributeQuery)) {
                    variantAttributePs.setInt(1, variant.getVariantId());

                    for (String attributeKey : variant.getAttributes().keySet()) {
                        int existingAttributeId = getAttributeId(attributeKey);
                        int attributeId = existingAttributeId != -1 ? existingAttributeId : addAttribute(attributeKey);

                        variantAttributePs.setInt(2, attributeId);
                        variantAttributePs.setString(3, variant.getAttributes().get(attributeKey));
                        variantAttributePs.executeUpdate();
                    }
                }

                connection.commit();
                return variant;

            } catch (SQLException e) {
                connection.rollback();
                System.err.println(e.getMessage());
                return null;
            } finally {
                connection.setAutoCommit(true);
            }

        } catch (SQLException e) {
            System.err.println(e.getMessage());
            return null;
        }
    }

    //TODO addVariants
    public List<Variant> addVariant() {
        return null;
    }

    public boolean deleteVariant(int shopId, int branchId, int productId, int variantId) {
        String productQuery = "update variants v " +
                "join products p on p.product_id = v.product_id " +
                "join branches b on p.branch_id = b.branch_id " +
                "Set v.is_deleted = true " +
                "where v.variant_id = ? AND v.product_id =? AND p.branch_id = ? AND b.shop_id =? ;";

        try (Connection connection = DBController.getConnection();
             PreparedStatement variantPs = connection.prepareStatement(productQuery)) {

            variantPs.setInt(1, variantId);
            variantPs.setInt(2, productId);
            variantPs.setInt(3, branchId);
            variantPs.setInt(4, shopId);
            variantPs.executeUpdate();
            return true;
        } catch (SQLException e) {
            System.err.println(e.getMessage());
            return false;
        }
    }


    public Product updateProduct(int shopId, int branchId, int productId, Product product) {

        String checkQuery =
                "select p.product_id from products p " +
                        "join branches b on p.branch_id = b.branch_id " +
                        "where p.product_id=? AND p.branch_id=? AND b.shop_id=? AND p.is_deleted=false;";

        try (Connection connection = DBController.getConnection()) {
            connection.setAutoCommit(false);

            try {
                try (PreparedStatement checkPs = connection.prepareStatement(checkQuery)) {
                    checkPs.setInt(1, productId);
                    checkPs.setInt(2, branchId);
                    checkPs.setInt(3, shopId);

                    try (ResultSet checkRs = checkPs.executeQuery()) {
                        if (!checkRs.next()) {
                            connection.rollback();
                            return null;
                        }
                    }
                }

                StringBuilder updateQuery = new StringBuilder("update products set ");
                List<Object> params = new ArrayList<>();

                if (product.getProductName() != null) {
                    updateQuery.append("product_name=?,");
                    params.add(product.getProductName());
                }

                if (product.getCategoryName() != null) {
                    int categoryId = getCategoryId(product.getCategoryName());
                    if (categoryId == -1) {
                        connection.rollback();
                        return null;
                    }
                    updateQuery.append("category_id=?,");
                    params.add(categoryId);
                }

                if (product.getBrandName() != null) {
                    int brandId = getBrandId(product.getBrandName());
                    if (brandId == -1) {
                        connection.rollback();
                        return null;
                    }
                    updateQuery.append("brand_id=?,");
                    params.add(brandId);
                }

                if (product.getModel() != null) {
                    updateQuery.append("model=?,");
                    params.add(product.getModel());
                }

                if (params.isEmpty()) {
                    connection.rollback();
                    return null;
                }

                updateQuery.deleteCharAt(updateQuery.length() - 1);
                updateQuery.append(" where product_id=? AND branch_id=? AND is_deleted=false;");

                params.add(productId);
                params.add(branchId);

                try (PreparedStatement updatePs = connection.prepareStatement(updateQuery.toString())) {
                    for (int i = 0; i < params.size(); i++) {
                        updatePs.setObject(i + 1, params.get(i));
                    }

                    int rowsAffected = updatePs.executeUpdate();
                    if (rowsAffected == 0) {
                        connection.rollback();
                        return null;
                    }
                }

                connection.commit();
                return getProducts(shopId, branchId, productId);

            } catch (SQLException e) {
                connection.rollback();
                System.err.println(e.getMessage());
                return null;
            } finally {
                connection.setAutoCommit(true);
            }

        } catch (SQLException e) {
            System.err.println(e.getMessage());
            return null;
        }
    }

    public Variant updateVariant(int shopId, int branchId, int productId, int variantId, Variant variant) {

        String checkQuery =
                "select v.variant_id from variants v " +
                        "join products p on v.product_id = p.product_id " +
                        "join branches b on p.branch_id = b.branch_id " +
                        "where v.variant_id=? AND v.product_id=? AND p.branch_id=? AND b.shop_id=? AND v.is_deleted=false;";

        try (Connection connection = DBController.getConnection()) {
            connection.setAutoCommit(false);

            try {
                try (PreparedStatement checkPs = connection.prepareStatement(checkQuery)) {
                    checkPs.setInt(1, variantId);
                    checkPs.setInt(2, productId);
                    checkPs.setInt(3, branchId);
                    checkPs.setInt(4, shopId);

                    try (ResultSet checkRs = checkPs.executeQuery()) {
                        if (!checkRs.next()) {
                            connection.rollback();
                            return null;
                        }
                    }
                }

                StringBuilder updateQuery = new StringBuilder("update variants set ");
                List<Object> params = new ArrayList<>();

                if (variant.getPrice() != 0) {
                    updateQuery.append("price=?,");
                    params.add(variant.getPrice());
                }

                if (variant.getMrp() != 0) {
                    updateQuery.append("mrp=?,");
                    params.add(variant.getMrp());
                }

                if (params.isEmpty()) {
                    connection.rollback();
                    return null;
                }

                updateQuery.deleteCharAt(updateQuery.length() - 1);
                updateQuery.append(" where variant_id=? AND product_id=? AND is_deleted=false;");

                params.add(variantId);
                params.add(productId);

                try (PreparedStatement updatePs = connection.prepareStatement(updateQuery.toString())) {
                    for (int i = 0; i < params.size(); i++) {
                        updatePs.setObject(i + 1, params.get(i));
                    }

                    int rowsAffected = updatePs.executeUpdate();
                    if (rowsAffected == 0) {
                        connection.rollback();
                        return null;
                    }
                }

                connection.commit();
                return getVariants(shopId, branchId, productId, variantId);

            } catch (SQLException e) {
                connection.rollback();
                System.err.println(e.getMessage());
                return null;
            } finally {
                connection.setAutoCommit(true);
            }

        } catch (SQLException e) {
            System.err.println(e.getMessage());
            return null;
        }
    }
}