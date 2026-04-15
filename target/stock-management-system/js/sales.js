let saleCart = [];
function loadSalesPage() {
    const contentDiv = document.getElementById("content");

    contentDiv.innerHTML = `
        <h2>Sales</h2>
        <button onclick="loadSales()">Load Sales</button>
        <button onclick="showSalesForm()">Add Sale</button>

        <div id="salesFormContainer" style="margin-top:20px;"></div>
            <table border="1" width="100%" style="margin-top:20px;">
                <thead>
                    <tr>
                        <th>S.No</th>
                        <th>Sale Date AND Time</th>
                        <th>Customer Name</th>
                        <th>See Products</th>
                        <th>Sale Amount</th>
                        <th>Action</th>
                    </tr>
                </thead>
                <tbody id="salesTable"></tbody>
            </table>    
    `;

}


async function loadSales() {
     const shopId = AppConfig.shopId;
    const branchId = AppConfig.branchId;
    if (!shopId || !branchId) {
        alert("Enter Shop ID and Branch ID");
        return;
    }

    const response = await fetch(`${BASE_URL}/shops/${shopId}/branches/${branchId}/sales`);
    const sales = await response.json();

    const table = document.getElementById("salesTable");
    table.innerHTML = "";

    let i = 1;

    for (const sale of sales) {
        const customerRes = await fetch(`${BASE_URL}/shops/${shopId}/branches/${branchId}/customers/${sale.customerId}`);
        const customer = await customerRes.json();

        table.innerHTML += `
        <tr>
            <td>${i++}</td>
            <td>${new Date(sale.dateTime).toLocaleString()}</td>
            <td>${customer.customerName}, ${customer.customerPhoneNumber}</td>
            <td>
                <button onclick="toggleSalesProducts(${sale.saleId}, ${shopId}, ${branchId})">
                    See Products
                </button>
            </td>
            <td>${sale.totalAmount}</td>
            <td>
                <button onclick="editSale(${sale.saleId})">Edit</button>
                <button onclick="deleteSale(${sale.saleId})">Delete</button>
            </td>
        </tr>

        <tr id="products-row-${sale.saleId}" 
            data-items='${JSON.stringify(sale.saleItems)}'
            style="display:none;">
        </tr>
        `;
    }
}


async function toggleSalesProducts(saleId, shopId, branchId) {

    const row = document.getElementById(`products-row-${saleId}`);

    if (row.style.display === "none") {

        row.style.display = "table-row";

        const saleItems = JSON.parse(row.dataset.items);

        row.innerHTML = `
            <td colspan="6">
                <table border="1" width="100%">
                    <thead>
                        <tr>
                            <th>S.No</th>
                            <th>Product Name</th>
                            <th>Attributes</th>
                            <th>Quantity</th>
                            <th>Price Per Unit</th>
                            <th>Total Price</th>
                        </tr>
                    </thead>
                    <tbody id="products-container-${saleId}"></tbody>
                </table>
            </td>
        `;

        const container = document.getElementById(`products-container-${saleId}`);
        let i = 1;

        for (const item of saleItems) {
            const variantRes = await fetch(
                `${BASE_URL}/shops/${shopId}/branches/${branchId}/products/${item.productId}/variants/${item.variantId}`
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
                    <td>${item.quantity}</td>
                    <td>${item.sellingPrice}</td>
                    <td>${item.totalAmount}</td>
                </tr>
            `;
        }

    } else {
        row.style.display = "none";
    }
}

async function showSalesForm() {

    const container = document.getElementById("salesFormContainer");

    container.innerHTML = `
        <h3>Add Sale</h3>

        <select id="customerSelect"></select> 

        <h4>Add Items</h4>

        <div>
            <select id="productSelect"></select>
            <select id="variantSelect"></select>
            <input type="number" id="quantityInput" placeholder="Quantity" min="1">
            <input type="number" id="priceInput" placeholder="Price" readonly>
            <button onclick="addItemToCart()">Add Item</button>
        </div>

        <table border="1" width="100%" style="margin-top:15px;">
            <thead>
                <tr>
                    <th>S.No</th>
                    <th>Product</th>
                    <th>Attributes</th>
                    <th>Qty</th>
                    <th>Price</th>
                    <th>Total</th>
                    <th>Action</th>
                </tr>
            </thead>
            <tbody id="cartTable"></tbody>
        </table>

        <h3>Total: <span id="grandTotal">0</span></h3>

        <button onclick="saveSale()">Save Sale</button>
    `;

    saleCart = [];
    loadCustomerForSale();
    loadProductsForSale();
}

async function loadCustomerForSale(){
    const shopId = AppConfig.shopId;
    const branchId = AppConfig.branchId;

    const res = await fetch(
        `${BASE_URL}/shops/${shopId}/branches/${branchId}/customers`
    );

    const customers = await res.json();

    const select = document.getElementById("customerSelect");
    select.innerHTML = `<option value="">Select Customer</option>`;

    customers.forEach(c => {
        select.innerHTML += `
            <option value="${c.customerId}">
                ${c.customerName} (${c.customerPhoneNumber})
            </option>
        `;
        console.log(c.customerId);
    });
}


async function loadProductsForSale() {

     const shopId = AppConfig.shopId;
    const branchId = AppConfig.branchId;
    const res = await fetch(
        `${BASE_URL}/shops/${shopId}/branches/${branchId}/products`
    );

    const products = await res.json();

    const select = document.getElementById("productSelect");
    select.innerHTML = `<option value="">Select Product</option>`;

    products.forEach(p => {
        select.innerHTML += `
            <option value="${p.productId}">
                ${p.productName} (${p.brandName})
            </option>
        `;
    });

    select.onchange = loadVariantsForSale;
}

async function loadVariantsForSale() {

    const shopId = AppConfig.shopId;
    const branchId = AppConfig.branchId;
    const productId = document.getElementById("productSelect").value;

    const res = await fetch(
        `${BASE_URL}/shops/${shopId}/branches/${branchId}/products/${productId}/variants`
    );

    const variants = await res.json();

    const select = document.getElementById("variantSelect");
    select.innerHTML = `<option value="">Select Variant</option>`;

    variants.forEach(v => {

        const attributesString = Object.entries(v.attributes)
            .map(([key, val]) => `${key}: ${val}`)
            .join(", ");

        select.innerHTML += `
            <option value="${v.variantId}" data-price="${v.price}">
                ${attributesString}
            </option>
        `;
    });

    select.onchange = function () {
        const price = this.selectedOptions[0].dataset.price;
        document.getElementById("priceInput").value = price;
    };
}

function addItemToCart() {

    const productSelect = document.getElementById("productSelect");
    const variantSelect = document.getElementById("variantSelect");

    const productId = productSelect.value;
    const productName = productSelect.selectedOptions[0].text;

    const variantId = variantSelect.value;
    const attributes = variantSelect.selectedOptions[0].text;

    const quantity = parseInt(document.getElementById("quantityInput").value);
    const price = parseFloat(document.getElementById("priceInput").value);

    if (!productId || !variantId || !quantity) {
        alert("Select all fields");
        return;
    }

    const total = quantity * price;

    const item = {
        productId,
        variantId,
        productName,
        attributes,
        quantity,
        sellingPrice: price,
        totalAmount: total
    };

    saleCart.push(item);

    renderCart();
}

function renderCart() {

    const table = document.getElementById("cartTable");
    table.innerHTML = "";

    let grandTotal = 0;
    let i =1;
    saleCart.forEach((item) => {

        grandTotal += item.totalAmount;

        table.innerHTML += `
            <tr>
                <td>${i++}</td>
                <td>${item.productName}</td>
                <td>${item.attributes}</td>
                <td>${item.quantity}</td>
                <td>${item.sellingPrice}</td>
                <td>${item.totalAmount}</td>
                <td>
                    <button onclick="removeItem(${i-2})">Remove</button>
                </td>
            </tr>
        `;
    });

    document.getElementById("grandTotal").innerText = grandTotal;
}

function removeItem(index) {
    saleCart.splice(index, 1);
    renderCart();
}

async function saveSale() {

      const shopId = AppConfig.shopId;
    const branchId = AppConfig.branchId;
    const customerId = parseInt(document.getElementById("customerSelect").value);

    if (saleCart.length === 0) {
        alert("Add at least one item");
        return;
    }

    const totalAmount = saleCart.reduce((sum, item) => sum + item.totalAmount, 0);

    console.log(saleCart);

    const data = {
        customerId,
        totalAmount,
        saleItems: saleCart.map(item => ({
            variantId: parseInt(item.variantId),
            quantity: item.quantity,
            sellingPrice: item.sellingPrice,
            totalAmount: item.totalAmount
        }))
    };

    console.log(data);

    await fetch(
        `${BASE_URL}/shops/${shopId}/branches/${branchId}/sales`,
        {
            method: "POST",
            headers: { "Content-Type": "application/json" },
            body: JSON.stringify(data)
        }
    );

    document.getElementById("salesFormContainer").innerHTML = "";
    loadSales();
}

// --------------------
async function editSale(saleId) {

     const shopId = AppConfig.shopId;
    const branchId = AppConfig.branchId;

    const response = await fetch(
        `${BASE_URL}/shops/${shopId}/branches/${branchId}/sales/${saleId}`
    );

    const sale = await response.json();

    showSalesForm(sale);
}

async function deleteSale(saleId) {

      const shopId = AppConfig.shopId;
    const branchId = AppConfig.branchId;

    const confirmDelete = confirm("Are you sure you want to delete this sale?");
    if (!confirmDelete) return;

    await fetch(
        `${BASE_URL}/shops/${shopId}/branches/${branchId}/sales/${saleId}`,
        { method: "DELETE" }
    );

    loadSales();
}