function loadReportsPage() {
    const content = document.querySelector(".content");

    content.innerHTML = `
        <h2>Reports</h2>
        <div class="report-selection-card">
            <p>Purchase</p>
                <span class="separator">|</span>
            <p>Sales</p>
                <span class="separator">|</span>
            <p>Stock</p>
                <span class="separator">|</span>
            <p>Profit/Loss</p>
        </div>

        <div class="report-result-container">
        </div>
    `;

    document.querySelectorAll(".report-selection-card p").forEach(report => {
        report.addEventListener("click", () => {
            const selectedReport = report.textContent.trim();
            loadReportDetails(selectedReport);
        });
    });
}

async function loadReportDetails(reportType) {

    const content = document.querySelector(".report-result-container");

     const shopId = AppConfig.shopId;
    const branchId = AppConfig.branchId;

    if (!shopId || !branchId) {
        alert("Enter Shop ID and Branch ID");
        return;
    }

    if (reportType === "Purchase") {
        const response = await fetch(`${BASE_URL}/shops/${shopId}/branches/${branchId}/reports/purchase-summary`);
        const reportData = await response.json();

        const totalBillings = reportData.totalOrders;
        const totalInvestments = reportData.totalInvestment;
        const topVendor = reportData.topVendor;
        const productDetails = reportData.productDetails;

        let rows = "";

        for (const product of productDetails) {

            for (const variant of product.variants) {

                const variantRes = await fetch(
                    `${BASE_URL}/shops/${shopId}/branches/${branchId}/products/${product.productId}/variants/${variant.variantId}`
                );

                const variantData = await variantRes.json();

                const attributesString = Object.entries(variantData.attributes)
                    .map(([key, value]) => `${key}: ${value}`)
                    .join(", ");

                rows += `
            <tr>
                <td>${product.productName}</td>
                <td>${attributesString}</td>
                <td>${variant.totalPurchase}</td>
                <td>${variant.totalAmount}</td>
            </tr>
        `;
            }
        }

        content.innerHTML = `
                    <div class="report-cards">

                        <div class="total-billings">
                            <p class="report-card-heading">Total Billings</p>
                            <div class="report-card-content">
                                <i class="total-billing-value-icon" data-lucide="shopping-bag"></i>
                                <p class="total-billing-value">${totalBillings}</p>
                            </div>
                        </div>

                        <div class="total-Investments">
                            <p class="report-card-heading">Total Investment</p>
                            <div class="report-card-content"> 
                                <i class="total-investment-value-icon" data-lucide="indian-rupee"></i>
                                <p class="total-investment-value">${totalInvestments}</p>
                            </div>
                        </div>

                        <div class="top-vendor">
                            <p class="report-card-heading">Top vendor</p>
                            <div class="report-card-content">
                                <i class="top-vendor-value-icon" data-lucide="user-round"></i>
                                <p class="top-vendor-value"> ${topVendor}</p>
                            </div>
                        </div>

                    </div>

                    <div class="report-table">
                        <table>
                            <thead>
                                <tr>
                                    <th>Product Name</th>
                                    <th>Attributes</th>
                                    <th>Total Purchases</th>
                                    <th>Total Amount</th>
                                </tr>
                            </thead>
                            <tbody>
                                ${rows}
                            </tbody>
                        </table>
                    </div>
                `;
    }

    else if (reportType === "Sales") {

        const response = await fetch(`${BASE_URL}/shops/${shopId}/branches/${branchId}/reports/sales-summary`);
        const reportData = await response.json();

        const totalOrders = reportData.totalOrders;
        const totalRevenue = reportData.totalRevenue;
        const topSellingProduct = reportData.topSellingProduct;
        const productDetails = reportData.productDetails;

        let rows = "";

        for (const product of productDetails) {

            for (const variant of product.variants) {

                const variantRes = await fetch(
                    `${BASE_URL}/shops/${shopId}/branches/${branchId}/products/${product.productId}/variants/${variant.variantId}`
                );

                const variantData = await variantRes.json();

                const attributesString = Object.entries(variantData.attributes)
                    .map(([key, value]) => `${key}: ${value}`)
                    .join(", ");

                rows += `
            <tr>
                <td>${product.productName}</td>
                <td>${attributesString}</td>
                <td>${variant.totalSales}</td>
                <td>${variant.totalAmount}</td>
            </tr>
        `;
            }
        }

        content.innerHTML = `
                    <div class="report-cards">

                        <div class="total-orders">
                            <p class="report-card-heading">Total Orders</p>
                            <div class="report-card-content">
                                <i class="total-orders-value-icon" data-lucide="shopping-bag"></i>
                                <p class="total-orders-value">${totalOrders}</p>
                            </div>
                        </div>

                        <div class="total-revenue">
                            <p class="report-card-heading">Total Revenue</p>
                            <div class="report-card-content"> 
                                <i class="total-revenue-value-icon" data-lucide="indian-rupee"></i>
                                <p class="total-revenue-value">${totalRevenue}</p>
                            </div>
                        </div>

                        <div class="top-selling-product">
                            <p class="report-card-heading">Top Selling Product</p>
                            <div class="report-card-content">
                                <i class="top-selling-product-value-icon" data-lucide="chart-line"></i>
                                <p class="top-selling-product-value"> ${topSellingProduct}</p>
                            </div>
                        </div>

                    </div>

                    <div class="report-table">
                        <table>
                            <thead>
                                <tr>
                                    <th>Product Name</th>
                                    <th>Total Stocks</th>
                                    <th>Total Stock Value</th>
                                    <th>variants</th>
                                </tr>
                            </thead>
                            <tbody>
                                ${rows}
                            </tbody>
                        </table>
                    </div>
                `;
    }

    else if (reportType === "Stock") {

        content.innerHTML = "";

        // content.innerHTML = ` <input type="number" id="thresholdForStockSummary" placeholder="Threshold">`;

        // const threshold = document.getElementById("thresholdForStockSummary").value;

        const threshold = 2000;

        const response = await fetch(`${BASE_URL}/shops/${shopId}/branches/${branchId}/reports/stock-summary?threshold=${threshold}`);
        const reportData = await response.json();

        const totalStockQuantity = reportData.totalStockQuantity;
        const totalStockValue = reportData.totalStockValue;
        const productDetails = reportData.productDetails;

        let rows = "";

        for (const product of productDetails) {

            rows += `
            <tr>
                <td>${product.productName}</td>
                <td>${product.totalStock}</td>
                <td>${product.stockValue}</td>

                <td>
                    <button onclick="toggleVariantStocks(${product.productId},${shopId},${branchId})">
                        See Variants
                    </button>
                </td>
            </tr>

            <tr id="products-row-${product.productId}" 
                data-items='${JSON.stringify(product.variants)}'
                style="display:none;">

            </tr>
        `;

        }

        const lowStockResponse = await fetch(`${BASE_URL}/shops/${shopId}/branches/${branchId}/products/low_stock/${threshold}`);
        const lowStock = await lowStockResponse.json();

        let info = "";

        for (const product of lowStock) {
            for (const variantItem of product.variants) {

                const variantRes = await fetch(
                    `${BASE_URL}/shops/${shopId}/branches/${branchId}/products/${product.productId}/variants/${variantItem.variantId}`
                );

                const variant = await variantRes.json();

                const attributesString = Object.entries(variant.attributes)
                    .map(([key, value]) => `${key}: ${value}`)
                    .join(", ");

                info += `
            <div class="low-stock-item">
                <strong>${product.productName}</strong>
                <p>${attributesString}</p>
                <span>Qty:</span><span>${variantItem.quantity}</span>
            </div>
        `;
            }
        }

        content.innerHTML += `
                <div class="stock-summary-part-1">
                    <div class="report-cards">

                        <div class="total-stock">
                            <p class="report-card-heading">Total Stocks</p>
                            <div class="report-card-content">
                                <i class="total-stock-value-icon" data-lucide="shopping-bag"></i>
                                <p class="total-stock-value">${totalStockQuantity}</p>
                            </div>
                        </div>

                        <div class="total-stockValue">
                            <p class="report-card-heading">Total stock value </p>
                            <div class="report-card-content"> 
                                <i class="total-stockValue-value-icon" data-lucide="indian-rupee"></i>
                                <p class="total-stockValue-value">${totalStockValue}</p>
                            </div>
                        </div>

                    </div>

                    <div class="low-stock">
                        <div class="low-stock-heading">
                            <p class="report-card-heading">low stock product</p>
                        </div>
                        <div class="low-stock-card-content">
                            <p class="low-stock-product-value">${info}</p>
                        </div>
                    </div>
                    </div>
                    <div class="report-table">
                        <table>
                            <thead>
                                <tr>
                                    <th>Product Name</th>
                                    <th>Total Stock</th>
                                    <th>Stock Value</th>
                                    <th>See Variants</th>
                                </tr>
                            </thead>
                            <tbody>
                                ${rows}
                            </tbody>
                        </table>
                    </div>
                `;
    }

    lucide.createIcons();
    // <i class="total-billing-icon" data-lucide="ticket-check"></i> 
    // <i class="total-investment-icon" data-lucide="receipt-indian-rupee"></i>
    // <i class="top-vendor-icon" data-lucide="trending-up"></i>
}


async function toggleVariantStocks(productId, shopId, branchId) {

    const row = document.getElementById(`products-row-${productId}`);

    if (row.style.display === "none") {

        row.style.display = "table-row";

        const variants = JSON.parse(row.dataset.items);

        row.innerHTML = `
            <td colspan="6">
                <table border="1" width="100%">
                    <thead>
                        <tr>
                            <th>S.No</th>
                            <th>Product Name</th>
                            <th>Attributes</th>
                            <th>Stock Available</th>
                            <th>Stock Value</th>
                        </tr>
                    </thead>
                    <tbody id="variant-container-${productId}"></tbody>
                </table>
            </td>
        `;

        const container = document.getElementById(`variant-container-${productId}`);
        let i = 1;

        for (const item of variants) {
            const variantRes = await fetch(
                `${BASE_URL}/shops/${shopId}/branches/${branchId}/products/${productId}/variants/${item.variantId}`
            );

            const variant = await variantRes.json();
            const attributesString = Object.entries(variant.attributes)
                .map(([key, value]) => `${key}: ${value}`)
                .join(", ");

            container.innerHTML += `
                <tr>
                    <td>${i++}</td>
                    <td>${item.productName}</td>
                    <td>${attributesString}</td>
                    <td>${item.stockAvailable}</td>
                    <td>${item.stockValue}</td>
                </tr>
            `;
        }

    } else {
        row.style.display = "none";
    }
}

