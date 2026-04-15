function loadInventoryPage() {

    const content = document.getElementById("content");

    content.innerHTML = `
        <h2>Inventory (Stocks)</h2>

        <div>
            <button onclick="loadStocks()">Load Stocks</button>
        </div>

        <table border="1" width="100%" style="margin-top:20px;">
            <thead>
                <tr>
                    <th>Variant</th>
                    <th>Attributes</th>
                    <th>Available Quantity</th>
                </tr>
            </thead>
            <tbody id="stockTable"></tbody>
        </table>
    `;
}

async function loadStocks() {


    if (!AppConfig.shopId || !AppConfig.branchId) {
        alert("Enter Shop ID and Branch ID");
        return;
    }

    try {
        const response = await fetch(
            `${BASE_URL}/shops/${AppConfig.shopId}/branches/${AppConfig.branchId}/stocks`
        );

        if (!response.ok) {
            throw new Error("Server error");
        }

        const stocks = await response.json();
        console.log(stocks);

        const table = document.getElementById("stockTable");
        table.innerHTML = "";

        for (const stock of stocks) {

            const responseAttributes = await fetch(
                `${BASE_URL}/product-attributes/${stock.variantId}`
            );

            const attributes = await responseAttributes.json();

            let attributeHTML = "";

            for (const [key, value] of Object.entries(attributes)) {
                attributeHTML += `<div>${key}: ${value}</div>`;
            }

            table.innerHTML += `
                <tr>
                    <td style="text-align: center;">${stock.variantId}</td>
                    <td style="text-align: center;">${attributeHTML}</td>
                    <td style="text-align: center;">${stock.quantity}</td>
                </tr>
            `;
        }

    } catch (error) {
        console.error(error);
        alert("Error fetching stock data");
    }
}