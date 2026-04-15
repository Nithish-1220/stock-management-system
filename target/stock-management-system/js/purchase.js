let purchaseCart = [];

function loadPurchasePage() {
    const contentDiv = document.getElementById("content");

    contentDiv.innerHTML = `
        <h2>Purchases</h2>
        <button onclick="loadPurchases()">Load Purchases</button>
        <button onclick="showPurchaseForm()">Create a new Invoice</button>

        <div id="purchaseFormContainer" style="margin-top:20px;"></div>
            <table border="1" width="100%" style="margin-top:20px;">
                <thead>
                    <tr>
                        <th>S.No</th>
                        <th>Purchase Date AND Time</th>
                        <th>Vendor Name</th>
                        <th>See Products</th>
                        <th>Purchase Amount</th>
                        <th>Action</th>
                    </tr>
                </thead>
                <tbody id="purchaseTable"></tbody>
            </table>    
    `;

}


async function loadPurchases() {
    const shopId = AppConfig.shopId;
    const branchId = AppConfig.branchId;

    if (!shopId || !branchId) {
        alert("Enter Shop ID and Branch ID");
        return;
    }

    const response = await fetch(`${BASE_URL}/shops/${shopId}/branches/${branchId}/purchases`);
    const purchases = await response.json();

    const table = document.getElementById("purchaseTable");
    table.innerHTML = "";

    let i = 1;

    for (const purchase of purchases) {
        const vendorRes = await fetch(`${BASE_URL}/vendors/${purchase.vendorId}`);
        const vendor = await vendorRes.json();

        table.innerHTML += `
        <tr>
            <td>${i++}</td>
            <td>${new Date(purchase.dataTime).toLocaleString()}</td>
            <td>${vendor.vendorName}</td>
            <td>
                <button onclick="togglePurchaseProducts(${purchase.purchaseId}, ${shopId}, ${branchId})">
                    See Products
                </button>
            </td>
            <td>${purchase.totalAmount}</td>
            <td>
                <button onclick="editPurchase(${purchase.purchaseId})">Edit</button>
                <button onclick="deletePurchase(${purchase.purchaseId})">Delete</button>
            </td>
        </tr>

        <tr id="products-row-${purchase.purchaseId}" 
            data-items='${JSON.stringify(purchase.purchaseItems)}'
            style="display:none;">
        </tr>
        `;
    }
}

async function togglePurchaseProducts(purchaseId, shopId, branchId) {

    const row = document.getElementById(`products-row-${purchaseId}`);

    if (row.style.display === "none") {

        row.style.display = "table-row";

        const purchaseItems = JSON.parse(row.dataset.items);

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
                    <tbody id="products-container-${purchaseId}"></tbody>
                </table>
            </td>
        `;

        const container = document.getElementById(`products-container-${purchaseId}`);
        let i = 1;

        for (const item of purchaseItems) {
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
                    <td>${item.costPrice}</td>
                    <td>${item.totalAmount}</td>
                </tr>
            `;
        }

    } else {
        row.style.display = "none";
    }
}


async function showPurchaseForm() {

    console.log(purchaseCart + "--------------------");
    const container = document.getElementById("purchaseFormContainer");

    container.innerHTML = `
        <h3>Add Purchase</h3>

        <select id="vendorSelect"></select> 

        <h4>Add Items</h4>

        <div>
            <select id="productSelect"></select>
            <select id="variantSelect"></select>
            <input type="number" id="quantityInput" placeholder="Quantity" min="1">
            <input type="number" id="priceInput" placeholder="Price" >
            <button onclick="addItemToPurchaseCart()">Add Item</button>
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

        <button onclick="savePurchase()">Save Purchase</button>
    `;

    purchaseCart = [];
    loadVendorsForPurchase();
    loadProductsForPurchase();
}


async function loadVendorsForPurchase() {
    const shopId = AppConfig.shopId;
    const branchId = AppConfig.branchId;

    const res = await fetch(
        `${BASE_URL}/vendors`
    );

    const vendors = await res.json();

    const select = document.getElementById("vendorSelect");
    select.innerHTML = `<option value="">Select Vendor</option>`;

    vendors.forEach(v => {
        select.innerHTML += `
            <option value="${v.vendorId}">
                ${v.vendorName} (${v.vendorPhoneNumber})
            </option>
        `;
        console.log(v.vendorId);
    });
}


async function loadProductsForPurchase() {
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

    select.onchange = loadVariantsForPurchase;
}

async function loadVariantsForPurchase() {

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
}

function addItemToPurchaseCart() {

    console.log("inside addItemToCart");

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
        costPrice: price,
        totalAmount: total
    };

    console.log(item);

    purchaseCart.push(item);

    console.log("----------" + JSON.stringify(purchaseCart));

    renderPurchaseCart();
}

function renderPurchaseCart() {
    console.log("inside renderPurchaseCart");
    const table = document.getElementById("cartTable");
    table.innerHTML = "";

    let grandTotal = 0;
    let i = 1;
    purchaseCart.forEach((item) => {

        grandTotal += item.totalAmount;

        table.innerHTML += `
            <tr>
                <td>${i++}</td>
                <td>${item.productName}</td>
                <td>${item.attributes}</td>
                <td>${item.quantity}</td>
                <td>${item.costPrice}</td>
                <td>${item.totalAmount}</td>
                <td>
                    <button onclick="removePurchaseCartItem(${i - 2})">Remove</button>
                </td>
            </tr>
        `;
    });

    document.getElementById("grandTotal").innerText = grandTotal;
}

function removePurchaseCartItem(index) {
    purchaseCart.splice(index, 1);
    renderPurchaseCart();
}

async function savePurchase() {

    const shopId = AppConfig.shopId;
    const branchId = AppConfig.branchId;
    const vendorId = parseInt(document.getElementById("vendorSelect").value);

    console.log(purchaseCart);

    if (purchaseCart.length === 0) {
        alert("Add at least one item");
        return;
    }

    const totalAmount = purchaseCart.reduce((sum, item) => sum + item.totalAmount, 0);

    console.log(purchaseCart);

    const data = {
        vendorId,
        totalAmount,
        requestPurchaseItems: purchaseCart.map(item => ({
            variantId: parseInt(item.variantId),
            quantity: item.quantity,
            costPrice: item.costPrice,
            totalAmount: item.totalAmount
        }))
    };

    console.log("------XXXXXXXXXXXX--------" + JSON.stringify(data));

    await fetch(
        `${BASE_URL}/shops/${shopId}/branches/${branchId}/purchases`,
        {
            method: "POST",
            headers: { "Content-Type": "application/json" },
            body: JSON.stringify(data)
        }
    );

    document.getElementById("purchaseFormContainer").innerHTML = "";
    loadPurchases();
}

async function deletePurchase(purchaseId) {

    const shopId = AppConfig.shopId;
    const branchId = AppConfig.branchId;

    const confirmDelete = confirm("Are you sure you want to delete this purchase?");
    if (!confirmDelete) return;

    await fetch(
        `${BASE_URL}/shops/${shopId}/branches/${branchId}/purchases/${purchaseId}`,
        { method: "DELETE" }
    );

    loadPurchases();
}