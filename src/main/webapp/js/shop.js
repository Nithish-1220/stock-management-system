// ---------------------shops---------------------
function loadShopsPage() {

    const content = document.getElementById("content");

    content.innerHTML = `
    <h2>Shops</h2>

    <div>
        <button onclick="addShop()">Add Shop</button>        
    </div>

            <table border="1" width="100%" style="margin-top:20px;">
                <thead>
                    <tr>
                        <th>ID</th>
                        <th>Name</th>
                        <th>Owner</th>
                        <th>Action</th>
                    </tr>
                </thead>
                <tbody id="shopTable"></tbody>
            </table>
            `;

    fetchShops();
}

async function fetchShops() {

    const response = await fetch(`${BASE_URL}/shops`);
    const shops = await response.json();

    const table = document.getElementById("shopTable");
    table.innerHTML = "";

    shops.forEach(shop => {
        table.innerHTML += `
            <tr>
                <td>${shop.shopId}</td>
                <td>${shop.shopName}</td>
                <td>${shop.shopOwnerName}</td>
                <td>
                    <button onclick="deleteShop(${shop.shopId})">
                        Delete
                    </button>
                </td>
            </tr>
        `;
    });
}

async function addShop() {

    //     <input type="text" id="shopName" placeholder="Shop Name">
    // <input type="text" id="ownerName" placeholder="Owner Name"></input>

    const shopName = document.getElementById("shopName").value;
    const ownerName = document.getElementById("ownerName").value;

    const data = {
        shop: {
            shopName: shopName,
            shopOwnerName: ownerName
        },
        branch: {
            branchName: "Default Branch",
            address: {
                doorNumber: "11234",
                streetName: "Default Street",
                cityName: "Chennai",
                stateName: "TamiNadu",
                countryName: "India",
                pincode: "612002"
            }
        }
    };

    await fetch(`${BASE_URL}/shops`, {
        method: "POST",
        headers: {
            "Content-Type": "application/json"
        },
        body: JSON.stringify(data)
    });

    fetchShops();
}

async function deleteShop(id) {

    await fetch(`${BASE_URL}/shops/${id}`, {
        method: "DELETE"
    });

    fetchShops();
}

// --------------------------------Branches--------------------------------

function loadBranchesPage() {

    const content = document.getElementById("content");

    content.innerHTML = `
        <h2>Branches</h2>

        <div style="margin-bottom:15px;">
            <button onclick="loadBranchesForShop()">Load Branches</button>
            <button onclick="showBranchForm()">Add Branch</button>
        </div>

        <div id="branchFormContainer"></div>

        <table border="1" width="100%" style="margin-top:20px;">
            <thead>
                <tr>
                    <th>ID</th>
                    <th>Branch Name</th>
                    <th>City</th>
                    <th>Action</th>
                </tr>
            </thead>
            <tbody id="branchTable"></tbody>
        </table>
    `;
}

function loadBranchesForShop() {
    const shopId = AppConfig.shopId;
    fetchBranches(shopId);
}


function showBranchForm() {

    const formContainer = document.getElementById("branchFormContainer");

    formContainer.innerHTML = `
        <h3>Add Branch</h3>

        <input type="text" id="branchName" placeholder="Branch Name"><br><br>

        <input type="text" id="doorNumber" placeholder="Door Number"><br><br>
        <input type="text" id="streetName" placeholder="Street Name"><br><br>
        <input type="text" id="cityName" placeholder="City"><br><br>
        <input type="text" id="stateName" placeholder="State"><br><br>
        <input type="text" id="countryName" placeholder="Country"><br><br>
        <input type="text" id="pincode" placeholder="Pincode"><br><br>

        <button onclick="addBranch()">Submit Branch</button>
        <button onclick="cancelBranchForm()">Cancel</button>
    `;
}


function cancelBranchForm() {
    document.getElementById("branchFormContainer").innerHTML = "";
}

async function fetchBranches(shopId) {

    const response = await fetch(
        `${BASE_URL}/shops/${shopId}/branches`
    );

    const branches = await response.json();

    const table = document.getElementById("branchTable");
    table.innerHTML = "";

    branches.forEach(branch => {
        table.innerHTML += `
            <tr>
                <td>${branch.branchId}</td>
                <td>${branch.branchName}</td>
                <td>${branch.address.cityName}</td>
                <td>
                    <button onclick="deleteBranch(${shopId}, ${branch.branchId})">
                        Delete
                    </button>
                </td>
            </tr>
        `;
    });
}


async function addBranch() {

    const shopId = AppConfig.shopId;
    const data = {
        branchName: document.getElementById("branchName").value,
        address: {
            doorNumber: document.getElementById("doorNumber").value,
            streetName: document.getElementById("streetName").value,
            cityName: document.getElementById("cityName").value,
            stateName: document.getElementById("stateName").value,
            countryName: document.getElementById("countryName").value,
            pincode: document.getElementById("pincode").value
        }
    };

    await fetch(
        `${BASE_URL}/shops/${shopId}/branches`,
        {
            method: "POST",
            headers: {
                "Content-Type": "application/json"
            },
            body: JSON.stringify(data)
        }
    );

    cancelBranchForm();
    fetchBranches(shopId);
}

async function deleteBranch(shopId, branchId) {

    await fetch(
        `${BASE_URL}/shops/${shopId}/branches/${branchId}`,
        {
            method: "DELETE"
        }
    );

    fetchBranches(shopId);
}