package com.zs.stockmanagement.product.dao;

import com.zs.stockmanagement.exceptions.BadRequestException;
import com.zs.stockmanagement.exceptions.ConflictException;
import com.zs.stockmanagement.exceptions.DataBaseException;
import com.zs.stockmanagement.exceptions.ResourceNotFoundException;
import com.zs.stockmanagement.product.model.Product;
import com.zs.stockmanagement.product.model.Variant;
import com.zs.stockmanagement.utils.DBController;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ProductDAO {

    public List<Product> getProducts(int shopId, int branchId) {

        String productQuery = """
                select p.product_id,p.product_name,c.category_name,b2.brand_name,p.model 
                From products p  
                Left join branches b on b.branch_id = p.branch_id
                left join categories c on p.category_id = c.category_id 
                left join brand b2 on p.brand_id = b2.brand_id 
                WHERE b.shop_id = ? AND p.branch_id = ? AND is_deleted = FALSE;
                """;

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
                } else {
                    throw new ResourceNotFoundException("product not found");
                }
                return product;
            }
        } catch (SQLException e) {
            throw new DataBaseException(e.getMessage());
        }
    }


    public List<Variant> getVariants(int shopId, int branchId, int productId) {
        String variantQuery = """
                select v.variant_id, v.price, v.mrp
                            from variants v
                            join products p on  v.product_id = p.product_id
                            join branches b on p.branch_id = b.branch_id
                            where b.shop_id=? AND b.branch_id = ? AND p.product_id=? AND v.is_deleted = false;
                """;

        String attributeQuery = """
                select a.attribute_key,va.attribute_value
                from variant_attribute va
                left join attribute a on va.attribute_id = a.attribute_id
                where variant_id = ? ;
                """;

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
                } else {
                    throw new ResourceNotFoundException("variant not found");
                }
                return variant;
            }
        } catch (SQLException e) {
            throw new DataBaseException(e.getMessage());
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
                } else {
                    throw new ResourceNotFoundException("category not found");
                }
            }
        } catch (SQLException e) {
            throw new DataBaseException(e.getMessage());
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
                } else {
                   return -1;
                }
            }
        } catch (SQLException e) {
            throw new DataBaseException(e.getMessage());
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
        } catch (SQLIntegrityConstraintViolationException e) {
            if (e.getErrorCode() == 1062) {
                throw new ConflictException("resource already exist");
            }
            throw new ConflictException(e.getMessage());
        } catch (SQLException e) {
            throw new DataBaseException(e.getMessage());
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
            throw new DataBaseException(e.getMessage());
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
                } else {
                    throw new DataBaseException("Unexpected error while creating brand");
                }
            }
        } catch (SQLIntegrityConstraintViolationException e) {
            if (e.getErrorCode() == 1062) {
                throw new ConflictException("resource already exist");
            }
            throw new ConflictException(e.getMessage());
        } catch (SQLException e) {

            throw new DataBaseException(e.getMessage());
        }
    }


    public Product addProduct(int shopId, int branchId, Product product, List<Variant> variants) {

        String checkShopQuery = "select shop_id from branches where branch_id=?;";
        String productQuery = """
                insert into products(branch_id,product_name,category_id,brand_id,model) values(?,?,?,?,?);
                """;
        String variantInsert = "insert into variants(product_id, price, mrp) values(?,?,?)";
        String variantAttributeInsert = "insert into variant_attribute(variant_id, attribute_id, attribute_value) VALUES (?,?,?);";

        int categoryId = getCategoryId(product.getCategoryName());
        int existingBrandId = getBrandId(product.getBrandName());
        int brandId = existingBrandId != -1 ? existingBrandId : addBrand(product.getBrandName());

        Integer productId = null;
        Connection connection = null;
        try {
            connection = DBController.getConnection();
            try (PreparedStatement checkShopPs = connection.prepareStatement(checkShopQuery)) {
                connection.setAutoCommit(false);
                checkShopPs.setInt(1, branchId);
                try (ResultSet checkShopRs = checkShopPs.executeQuery()) {
                    if (!checkShopRs.next() || checkShopRs.getInt("shop_id") != shopId) {
                        connection.rollback();
                        throw new ResourceNotFoundException("product not found for these inputs");
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
                            throw new DataBaseException("Unable to create product");
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
            }
        } catch (SQLIntegrityConstraintViolationException e) {
            if (connection != null) {
                try {
                    connection.rollback();
                } catch (SQLException ignored) {
                }
            }
            switch (e.getErrorCode()) {
                case 1062:
                    throw new ConflictException("Resource already exists");
                case 1452:
                    throw new BadRequestException("Invalid reference value");
                case 1451:
                    throw new ConflictException("Cannot delete. Resource in use");
                default:
                    throw new DataBaseException("Constraint violation");
            }
        } catch (SQLException e) {
            try {
                connection.rollback();
                throw new DataBaseException(e.getMessage());
            } catch (Exception ee) {
                throw new DataBaseException(ee.getMessage());
            }
        } finally {
            if (connection != null) {
                try {
                    connection.setAutoCommit(true);
                    connection.close();
                } catch (SQLException ignored) {
                }
            }
        }
    }

    //TODO addProducts
    public List<Product> addProducts() {
        return null;
    }

    public boolean deleteProduct(int shopId, int branchId, int productId) {

        String productQuery = """
                        UPDATE products p
                        JOIN branches b ON p.branch_id = b.branch_id
                        SET p.is_deleted = TRUE
                        WHERE p.product_id = ? AND p.branch_id = ? AND b.shop_id = ? AND p.is_deleted = FALSE;
                        """;

        try (Connection connection = DBController.getConnection();
             PreparedStatement productPs = connection.prepareStatement(productQuery)) {
            productPs.setInt(1, productId);
            productPs.setInt(2, branchId);
            productPs.setInt(3, shopId);
            int rowsAffected = productPs.executeUpdate();
            if (rowsAffected == 0) {
                throw new ResourceNotFoundException("Product not found for given inputs");
            }
            return true;
        } catch (SQLIntegrityConstraintViolationException e) {
            if (e.getErrorCode() == 1451) {
                throw new ConflictException("Cannot delete. Product is in use");
            }
            throw new DataBaseException("Constraint violation while deleting product");
        } catch (SQLException e) {
            throw new DataBaseException("Unable to delete product");
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

        String variantQuery =
                "INSERT INTO variants(product_id, price, mrp) VALUES (?,?,?)";

        String variantAttributeQuery =
                "INSERT INTO variant_attribute(variant_id, attribute_id, attribute_value) VALUES (?,?,?)";

        Connection connection = null;

        try {
            connection = DBController.getConnection();
            connection.setAutoCommit(false);

            try (PreparedStatement checkPs = connection.prepareStatement(checkQuery)) {
                checkPs.setInt(1, productId);
                checkPs.setInt(2, branchId);
                checkPs.setInt(3, shopId);

                try (ResultSet rs = checkPs.executeQuery()) {
                    if (!rs.next()) {
                        throw new ResourceNotFoundException("Product not found for given inputs");
                    }
                }
            }

            try (PreparedStatement variantPs =
                         connection.prepareStatement(variantQuery, PreparedStatement.RETURN_GENERATED_KEYS)) {

                variantPs.setInt(1, productId);
                variantPs.setDouble(2, variant.getPrice());
                variantPs.setDouble(3, variant.getMrp());

                int rows = variantPs.executeUpdate();
                if (rows == 0) {
                    throw new DataBaseException("Unable to create variant");
                }

                try (ResultSet variantRs = variantPs.getGeneratedKeys()) {
                    if (variantRs.next()) {
                        variant.setVariantId(variantRs.getInt(1));
                    } else {
                        throw new DataBaseException("Variant ID generation failed");
                    }
                }
            }
            try (PreparedStatement variantAttributePs =
                         connection.prepareStatement(variantAttributeQuery)) {

                variantAttributePs.setInt(1, variant.getVariantId());

                for (String attributeKey : variant.getAttributes().keySet()) {

                    int existingAttributeId = getAttributeId(attributeKey);
                    int attributeId = existingAttributeId != -1
                            ? existingAttributeId
                            : addAttribute(attributeKey);

                    variantAttributePs.setInt(2, attributeId);
                    variantAttributePs.setString(3,
                            variant.getAttributes().get(attributeKey));

                    variantAttributePs.executeUpdate();
                }
            }

            connection.commit();
            return variant;
        } catch (SQLIntegrityConstraintViolationException e) {
            if (connection != null) {
                try { connection.rollback(); } catch (SQLException ignored) {}
            }
            switch (e.getErrorCode()) {
                case 1062:
                    throw new ConflictException("Variant already exists");
                case 1452:
                    throw new BadRequestException("Invalid reference value");
                default:
                    throw new DataBaseException("Constraint violation while creating variant");
            }
        } catch (SQLException e) {
            if (connection != null) {
                try { connection.rollback(); } catch (SQLException ignored) {}
            }
            throw new DataBaseException("Unable to create variant");
        } finally {
            if (connection != null) {
                try {
                    connection.setAutoCommit(true);
                    connection.close();
                } catch (SQLException ignored) {}
            }
        }
    }

    //TODO addVariants
    public List<Variant> addVariant() {
        return null;
    }
    public boolean deleteVariant(int shopId, int branchId, int productId, int variantId) {

        String query =
                "UPDATE variants v " +
                        "JOIN products p ON p.product_id = v.product_id " +
                        "JOIN branches b ON p.branch_id = b.branch_id " +
                        "SET v.is_deleted = TRUE " +
                        "WHERE v.variant_id = ? " +
                        "AND v.product_id = ? " +
                        "AND p.branch_id = ? " +
                        "AND b.shop_id = ? " +
                        "AND v.is_deleted = FALSE";

        try (Connection connection = DBController.getConnection();
             PreparedStatement ps = connection.prepareStatement(query)) {

            ps.setInt(1, variantId);
            ps.setInt(2, productId);
            ps.setInt(3, branchId);
            ps.setInt(4, shopId);

            int rowsAffected = ps.executeUpdate();

            if (rowsAffected == 0) {
                throw new ResourceNotFoundException("Variant not found for given inputs");
            }

            return true;
        } catch (SQLIntegrityConstraintViolationException e) {
            if (e.getErrorCode() == 1451) {
                throw new ConflictException("Cannot delete. Variant is in use");
            }
            throw new DataBaseException("Constraint violation while deleting variant");
        } catch (SQLException e) {
            throw new DataBaseException("Unable to delete variant");
        }
    }


    public Product updateProduct(int shopId, int branchId, int productId, Product product) {

        String checkQuery =
                "SELECT p.product_id FROM products p " +
                        "JOIN branches b ON p.branch_id = b.branch_id " +
                        "WHERE p.product_id=? AND p.branch_id=? AND b.shop_id=? AND p.is_deleted=false;";

        Connection connection = null;

        try {
            connection = DBController.getConnection();
            connection.setAutoCommit(false);

            try (PreparedStatement checkPs = connection.prepareStatement(checkQuery)) {

                checkPs.setInt(1, productId);
                checkPs.setInt(2, branchId);
                checkPs.setInt(3, shopId);

                try (ResultSet checkRs = checkPs.executeQuery()) {
                    if (!checkRs.next()) {
                        throw new ResourceNotFoundException("Product not found for given inputs");
                    }
                }
            }

            StringBuilder updateQuery = new StringBuilder("UPDATE products SET ");
            List<Object> params = new ArrayList<>();

            if (product.getProductName() != null) {
                updateQuery.append("product_name=?,");
                params.add(product.getProductName());
            }

            if (product.getCategoryName() != null) {
                int categoryId = getCategoryId(product.getCategoryName());
                if (categoryId == -1) {
                    throw new BadRequestException("Invalid category name");
                }
                updateQuery.append("category_id=?,");
                params.add(categoryId);
            }

            if (product.getBrandName() != null) {
                int brandId = getBrandId(product.getBrandName());
                if (brandId == -1) {
                    throw new BadRequestException("Invalid brand name");
                }
                updateQuery.append("brand_id=?,");
                params.add(brandId);
            }

            if (product.getModel() != null) {
                updateQuery.append("model=?,");
                params.add(product.getModel());
            }

            if (params.isEmpty()) {
                throw new BadRequestException("No fields provided for update");
            }

            updateQuery.deleteCharAt(updateQuery.length() - 1);
            updateQuery.append(" WHERE product_id=? AND branch_id=? AND is_deleted=false;");

            params.add(productId);
            params.add(branchId);

            try (PreparedStatement updatePs = connection.prepareStatement(updateQuery.toString())) {
                for (int i = 0; i < params.size(); i++) {
                    updatePs.setObject(i + 1, params.get(i));
                }

                int rowsAffected = updatePs.executeUpdate();
                if (rowsAffected == 0) {
                    throw new ResourceNotFoundException("Product not found or already deleted");
                }
            }

            connection.commit();
            return getProducts(shopId, branchId, productId);

        } catch (SQLIntegrityConstraintViolationException e) {

            switch (e.getErrorCode()) {

                case 1062:
                    throw new ConflictException("Duplicate value exists");

                case 1452:
                    throw new BadRequestException("Invalid reference value");

                default:
                    throw new DataBaseException("Constraint violation");
            }

        } catch (SQLException e) {

            try {
                if (connection != null) connection.rollback();
            } catch (SQLException ignored) {}

            throw new DataBaseException("Unable to update product");

        } finally {
            try {
                if (connection != null) {
                    connection.setAutoCommit(true);
                    connection.close();
                }
            } catch (SQLException ignored) {}
        }
    }

    public Variant updateVariant(int shopId,
                                 int branchId,
                                 int productId,
                                 int variantId,
                                 Variant variant) {

        String checkQuery =
                "SELECT v.variant_id FROM variants v " +
                        "JOIN products p ON v.product_id = p.product_id " +
                        "JOIN branches b ON p.branch_id = b.branch_id " +
                        "WHERE v.variant_id=? AND v.product_id=? " +
                        "AND p.branch_id=? AND b.shop_id=? AND v.is_deleted=false;";

        Connection connection = null;

        try {
            connection = DBController.getConnection();
            connection.setAutoCommit(false);

            try (PreparedStatement checkPs = connection.prepareStatement(checkQuery)) {

                checkPs.setInt(1, variantId);
                checkPs.setInt(2, productId);
                checkPs.setInt(3, branchId);
                checkPs.setInt(4, shopId);

                try (ResultSet checkRs = checkPs.executeQuery()) {
                    if (!checkRs.next()) {
                        throw new ResourceNotFoundException("Variant not found for given inputs");
                    }
                }
            }

            StringBuilder updateQuery = new StringBuilder("UPDATE variants SET ");
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
                throw new BadRequestException("No fields provided for update");
            }

            updateQuery.deleteCharAt(updateQuery.length() - 1);
            updateQuery.append(" WHERE variant_id=? AND product_id=? AND is_deleted=false;");

            params.add(variantId);
            params.add(productId);

            try (PreparedStatement updatePs = connection.prepareStatement(updateQuery.toString())) {
                for (int i = 0; i < params.size(); i++) {
                    updatePs.setObject(i + 1, params.get(i));
                }

                int rowsAffected = updatePs.executeUpdate();
                if (rowsAffected == 0) {
                    throw new ResourceNotFoundException("Variant not found or already deleted");
                }
            }

            connection.commit();
            return getVariants(shopId, branchId, productId, variantId);

        } catch (SQLIntegrityConstraintViolationException e) {

            switch (e.getErrorCode()) {

                case 1062:
                    throw new ConflictException("Duplicate value exists");

                case 1452:
                    throw new BadRequestException("Invalid reference value");

                default:
                    throw new DataBaseException("Constraint violation");
            }

        } catch (SQLException e) {

            try {
                if (connection != null) connection.rollback();
            } catch (SQLException ignored) {}

            throw new DataBaseException("Unable to update variant");

        } finally {

            try {
                if (connection != null) {
                    connection.setAutoCommit(true);
                    connection.close();
                }
            } catch (SQLException ignored) {}
        }
    }
}