// ------------------------product------------------------

function loadProductsPage() {

    const content = document.getElementById("content");

    content.innerHTML = `
        <h2>Products</h2>

        <div>
            <button onclick="loadProducts()">Load Products</button>
            <button onclick="showProductForm()">Add Product</button>
        </div>

        <div id="productFormContainer" style="margin-top:20px;"></div>

        <table border="1" width="100%" style="margin-top:20px;">
            <thead>
                <tr>
                    <th>S.No</th>
                    <th>Name</th>
                    <th>Brand</th>
                    <th>Model</th>
                    <th>Action</th>
                </tr>
            </thead>
            <tbody id="productTable"></tbody>
        </table>

        <div id="variantSection" style="margin-top:30px;"></div>
    `;
}

async function loadProducts() {
     const response = await fetch(
        `${BASE_URL}/shops/${AppConfig.shopId}/branches/${AppConfig.branchId}/products`
    );

    const products = await response.json();
    const table = document.getElementById("productTable");
    table.innerHTML = "";
    let a = 1;
    products.forEach(product => {

        table.innerHTML += `
            <tr>
                <td>${a++}</td>
                <td>${product.productName}</td>
                <td>${product.brandName}</td>
                <td>${product.model}</td>
                <td>
                    <button onclick="editProduct(${product.productId})">Edit</button>
                    <button onclick="deleteProduct(${product.productId})">Delete</button>
                    <button onclick="loadVariants(${product.productId})">Variants</button>
                </td>
            </tr>
        `;
    });
}

async function showProductForm(product) {

    const container = document.getElementById("productFormContainer");

    let productId = "";
    let productName = "";
    let categoryName = "";
    let brandName = "";
    let model = "";

    if (product != null) {
        productId = product.productId;
        productName = product.productName;
        categoryName = product.categoryName;
        brandName = product.brandName;
        model = product.model;
    }

    container.innerHTML = `
        <h3>${product ? "Update Product" : "Add Product"}</h3>

        <input type="hidden" id="productId" value="${productId}">

        <input type="text" id="productName" placeholder="Product Name" value="${productName}"><br><br>
        <input type="text" id="categoryName" placeholder="Category" value="${categoryName}"><br><br>
        <input type="text" id="brandName" placeholder="Brand" value="${brandName}"><br><br>
        <input type="text" id="model" placeholder="Model" value="${model}"><br><br>
    `;

    if (!product) {
        container.innerHTML += `
        <h4>Variant details</h4>
        <input type="number" id="variantPrice" placeholder="Variant Price"><br><br>
        <input type="number" id="variantMrp" placeholder="Variant MRP"><br><br>
        <div id="variantAttributesSection" style="margin-top:20px; display: flex; gap: 20px;">
            <div class="totalVariantAttributesContainer" style="border: 2px solid #2c3e50; padding: 10px; margin-top: 10px; border-radius: 5px;"></div>
            <div class="selectedVariantAttributesContainer" style="border: 2px solid #34495e; padding: 10px; margin-top: 10px; min-width: 228px;border-radius: 5px;">
                <h4>Selected Variant Attributes</h4>
            </div>
        </div>
        `;

        const response = await fetch(`${BASE_URL}/product-attributes`)
        const attributes = await response.json();
        console.log(attributes);
        const totalVariantAttributesContainer = document.querySelector(".totalVariantAttributesContainer");
        totalVariantAttributesContainer.innerHTML = "<h4>Total Variant Attributes</h4>";

        Object.entries(attributes).forEach(([id, name]) => {
            totalVariantAttributesContainer.innerHTML += `
                <div class="variantAttribute" id="variantAttribute_${id}" draggable="true" style="margin: 5px;">
                  <input type="text" 
                                    id="attribute_${name}" 
                                    placeholder="${name}" 
                                    style="width: 100%; padding: 8px; border: 1px solid #ccc; border-radius: 4px; font-size: 13px; box-sizing: border-box;">
                </div>
                `;
        });

        let list = document.querySelectorAll(".variantAttribute");
        let selectedVariantAttributesContainer = document.querySelector(".selectedVariantAttributesContainer");
        let draggedItem = null;

        list.forEach(item => {
            item.addEventListener("dragstart", function (e) {
                draggedItem = this;
            });
        });

        totalVariantAttributesContainer.addEventListener("dragover", function (e) {
            e.preventDefault();
        });

        selectedVariantAttributesContainer.addEventListener("dragover", function (e) {
            e.preventDefault();
        });

        totalVariantAttributesContainer.addEventListener("drop", function () {
            if (draggedItem) {
                totalVariantAttributesContainer.appendChild(draggedItem);
                draggedItem = null;
            }
        });

        selectedVariantAttributesContainer.addEventListener("drop", function () {
            if (draggedItem) {
                selectedVariantAttributesContainer.appendChild(draggedItem);
                draggedItem = null;
            }
        });

    }

    const saveBtn = document.createElement("button");
    saveBtn.textContent = "Save Product";
    saveBtn.onclick = saveProduct;
    container.appendChild(saveBtn);



}

async function saveProduct() {

    const productId = document.getElementById("productId").value;

    if (productId) {

        await fetch(
            `${BASE_URL}/shops/${AppConfig.shopId}/branches/${AppConfig.branchId}/products/${productId}`,
            {
                method: "PATCH",
                headers: { "Content-Type": "application/json" },
                body: JSON.stringify({
                    productName: document.getElementById("productName").value,
                    categoryName: document.getElementById("categoryName").value,
                    brandName: document.getElementById("brandName").value,
                    model: document.getElementById("model").value
                })
            }
        );

    } else {

        const data = {
            product: {
                productName: document.getElementById("productName").value,
                categoryName: document.getElementById("categoryName").value,
                brandName: document.getElementById("brandName").value,
                model: document.getElementById("model").value
            },
            variants: [{
                price: document.getElementById("variantPrice").value,
                mrp: document.getElementById("variantMrp").value,
                attributes: getSelectedAttributes()
            }]
        };

        await fetch(
            `${BASE_URL}/shops/${AppConfig.shopId}/branches/${AppConfig.branchId}/products`,
            {
                method: "POST",
                headers: { "Content-Type": "application/json" },
                body: JSON.stringify(data)
            }
        );
    }
    document.getElementById("productFormContainer").innerHTML = "";
    loadProducts();
}

function getSelectedAttributes() {

    const selectedContainer = document.querySelector(".selectedVariantAttributesContainer");

    const inputs = selectedContainer.querySelectorAll("input");

    const attributes = {};

    inputs.forEach(input => {
        const key = input.id.split("_")[1];
        const value = input.value;
        attributes[key] = value;
    });
    console.log(attributes);
    return attributes;

}

async function deleteProduct(productId) {

    await fetch(
        `${BASE_URL}/shops/${AppConfig.shopId}/branches/${AppConfig.branchId}/products/${productId}`,
        { method: "DELETE" }
    );

    loadProducts();
}

async function editProduct(productId) {

    const response = await fetch(
        `${BASE_URL}/shops/${AppConfig.shopId}/branches/${AppConfig.branchId}/products/${productId}`
    );

    const product = await response.json();
    showProductForm(product);
}

// --------------------------------Variants--------------------------------

async function loadVariants(productId) {

    const response = await fetch(
        `${BASE_URL}/shops/${AppConfig.shopId}/branches/${AppConfig.branchId}/products/${productId}/variants`
    );

    const variants = await response.json();

    const section = document.getElementById("variantSection");

    section.innerHTML = `
        <h3>Variants</h3>
        <button onclick="showVariantForm(${productId})">Add Variant</button>

        <table border="1" width="100%" style="margin-top:10px;">
            <thead>
                <tr>
                    <th>S.No</th>
                    <th>Price</th>
                    <th>MRP</th>
                    <th>Action</th>
                    <th>Attributes</th>
                </tr>
            </thead>
            <tbody id="variantTable"></tbody>
        </table>

        <div id="variantFormContainer"></div>
    `;

    const table = document.getElementById("variantTable");
    table.innerHTML = "";
    let a = 1;
    variants.forEach(v => {
        table.innerHTML += `
            <tr>
                <td>${a++}</td>
                <td>${v.price}</td>
                <td>${v.mrp}</td>
               <td>
                ${v.attributes
                ? Object.entries(v.attributes)
                    .map(([key, value]) => `${key} : ${value}`)
                    .join(", ")
                : ""
            }
                </td>
                <td>
                    <button onclick="editVariant(${productId}, ${v.variantId})">Edit</button>
                    <button onclick="deleteVariant(${productId}, ${v.variantId})">Delete</button>
                </td>
        
            </tr>
        `;
    });
}

async function showVariantForm(productId, variant) {

    container = document.getElementById("variantFormContainer");
    container.innerHTML = "";
    let variantPrice = "";
    let variantMrp = "";
    let variantId = "";

    if (variant) {
        variantPrice = variant.price;
        variantMrp = variant.mrp;
        variantId = variant.variantId;
    }

    container.innerHTML += `
        <h4>Variant details</h4>
        <input type="hidden" id="variantId" value="${variantId}">
        <input type="number" id="variantPrice" placeholder="Variant Price" value="${variantPrice}"><br><br>
        <input type="number" id="variantMrp" placeholder="Variant MRP" value="${variantMrp}"><br><br>`;

    if (!variant) {
        container.innerHTML += `
        <div id="variantAttributesSection" style="margin-top:20px; display: flex; gap: 20px;">
            <div class="totalVariantAttributesContainer" style="border: 2px solid #2c3e50; padding: 10px; margin-top: 10px; border-radius: 5px;"></div>
            <div class="selectedVariantAttributesContainer" style="border: 2px solid #34495e; padding: 10px; margin-top: 10px; min-width: 228px;border-radius: 5px;">
                <h4>Selected Variant Attributes</h4>
            </div>
        </div>
        `;

        const response = await fetch(`${BASE_URL}/product-attributes`)
        const attributes = await response.json();
        const totalVariantAttributesContainer = document.querySelector(".totalVariantAttributesContainer");
        const selectedVariantAttributesContainer = document.querySelector(".selectedVariantAttributesContainer");

        totalVariantAttributesContainer.innerHTML = "<h4>Total Variant Attributes</h4>";

        Object.entries(attributes).forEach(([id, name]) => {
            totalVariantAttributesContainer.innerHTML += `
                <div class="variantAttribute" id="variantAttribute_${id}" draggable="true" style="margin: 5px;">
                  <input type="text" 
                                    id="attribute_${name}" 
                                    placeholder="${name}" 
                                    style="width: 100%; padding: 8px; border: 1px solid #ccc; border-radius: 4px; font-size: 13px; box-sizing: border-box;">
                </div>
                `;
        });

        let list = document.querySelectorAll(".variantAttribute");
        let draggedItem = null;

        list.forEach(item => {
            item.addEventListener("dragstart", function (e) {
                draggedItem = this;
            }); variant
        });

        totalVariantAttributesContainer.addEventListener("dragover", function (e) {
            e.preventDefault();
        });

        selectedVariantAttributesContainer.addEventListener("dragover", function (e) {
            e.preventDefault();
        });

        totalVariantAttributesContainer.addEventListener("drop", function () {
            if (draggedItem) {
                totalVariantAttributesContainer.appendChild(draggedItem);
                draggedItem = null;
            }
        });

        selectedVariantAttributesContainer.addEventListener("drop", function () {
            if (draggedItem) {
                selectedVariantAttributesContainer.appendChild(draggedItem);
                draggedItem = null;
            }
        });

    } else {
        container.innerHTML += `
        <div id="variantAttributesSection" style="margin-top:20px; display: flex; gap: 20px;">
            <div class="totalVariantAttributesContainer" style="border: 2px solid #2c3e50; padding: 10px; margin-top: 10px; border-radius: 5px;"></div>
            <div class="selectedVariantAttributesContainer" style="border: 2px solid #34495e; padding: 10px; margin-top: 10px; min-width: 228px;border-radius: 5px;">
                <h4>Selected Variant Attributes</h4>
            </div>
        </div>
        `;


        const response = await fetch(`${BASE_URL}/product-attributes`)
        const attributes = await response.json();
        console.log(attributes);
        const totalVariantAttributesContainer = document.querySelector(".totalVariantAttributesContainer");
        const selectedVariantAttributesContainer = document.querySelector(".selectedVariantAttributesContainer");

        Object.entries(variant.attributes).forEach(([id, name]) => {
            selectedVariantAttributesContainer.innerHTML += `
                <div class="variantAttribute" id="variantAttribute_${id}" draggable="true" style="margin: 5px;">
                  <input type="text" 
                                    id="attribute_${name}" 
                                    placeholder="${name}" 
                                    style="width: 100%; padding: 8px; border: 1px solid #ccc; border-radius: 4px; font-size: 13px; box-sizing: border-box;">
                </div>
                `;
        });


        totalVariantAttributesContainer.innerHTML = "<h4>Total Variant Attributes</h4>";

        Object.entries(attributes).forEach(([id, name]) => {
            if (!(variant.attributes[id])) {
                totalVariantAttributesContainer.innerHTML += `
                <div class="variantAttribute" id="variantAttribute_${id}" draggable="true" style="margin: 5px;">
                  <input type="text" 
                                    id="attribute_${name}" 
                                    placeholder="${name}" 
                                    style="width: 100%; padding: 8px; border: 1px solid #ccc; border-radius: 4px; font-size: 13px; box-sizing: border-box;">
                </div>
                `;
            }
        });


        let list = document.querySelectorAll(".variantAttribute");
        let draggedItem = null;

        list.forEach(item => {
            item.addEventListener("dragstart", function (e) {
                draggedItem = this;
            }); variant
        });

        totalVariantAttributesContainer.addEventListener("dragover", function (e) {
            e.preventDefault();
        });

        selectedVariantAttributesContainer.addEventListener("dragover", function (e) {
            e.preventDefault();
        });

        totalVariantAttributesContainer.addEventListener("drop", function () {
            if (draggedItem) {
                totalVariantAttributesContainer.appendChild(draggedItem);
                draggedItem = null;
            }
        });

        selectedVariantAttributesContainer.addEventListener("drop", function () {
            if (draggedItem) {
                selectedVariantAttributesContainer.appendChild(draggedItem);
                draggedItem = null;
            }
        });
    }

    const saveBtn = document.createElement("button");
    saveBtn.textContent = "Save Variant";
    saveBtn.onclick = () => saveVariant(productId);
    container.appendChild(saveBtn);
}


async function saveVariant(productId) {

    const variantId = document.getElementById("variantId").value;


    const price = parseFloat(document.getElementById("variantPrice").value);
    const mrp = parseFloat(document.getElementById("variantMrp").value);

    if (variantId) {
        console.log(JSON.stringify({ price, mrp }));
        await fetch(
            `${BASE_URL}/shops/${AppConfig.shopId}/branches/${AppConfig.branchId}/products/${productId}/variants/${variantId}`,
            {
                method: "PATCH",
                headers: { "Content-Type": "application/json" },
                body: JSON.stringify({ price, mrp })
            }
        );
    } else {
        await fetch(
            `${BASE_URL}/shops/${AppConfig.shopId}/branches/${AppConfig.branchId}/products/${productId}/variants`,
            {
                method: "POST",
                headers: { "Content-Type": "application/json" },
                body: JSON.stringify({
                    price: document.getElementById("variantPrice").value,
                    mrp: document.getElementById("variantMrp").value,
                    attributes: getSelectedAttributes()
                })
            }
        );
    }
    loadVariants(productId);
}

async function deleteVariant(productId, variantId) {


    await fetch(
        `${BASE_URL}/shops/${AppConfig.shopId}/branches/${AppConfig.branchId}/products/${productId}/variants/${variantId}`,
        { method: "DELETE" }
    );

    loadVariants(productId);
}


async function editVariant(productId, variantId) {

    const response = await fetch(
        `${BASE_URL}/shops/${AppConfig.shopId}/branches/${AppConfig.branchId}/products/${productId}/variants/${variantId}`
    );

    const variant = await response.json();
    showVariantForm(productId, variant);
}   