function loadVendorsPage() {

    const content = document.getElementById("content");

    content.innerHTML = `
        <h2>Vendors</h2>

        <button onclick="showVendorForm()">Add Vendor</button>

        <div id="vendorFormContainer" style="margin-top:20px;"></div>

        <table border="1" width="100%" style="margin-top:20px;">
            <thead>
                <tr>
                    <th>ID</th>
                    <th>Name</th>
                    <th>Phone</th>
                    <th>City</th>
                    <th>Action</th>
                </tr>
            </thead>
            <tbody id="vendorTable"></tbody>
        </table>
    `;

    fetchVendors();
}

async function fetchVendors() {

    const response = await fetch(`${BASE_URL}/vendors`);
    const vendors = await response.json();

    const table = document.getElementById("vendorTable");
    table.innerHTML = "";

    vendors.forEach(vendor => {
        table.innerHTML += `
            <tr>
                <td>${vendor.vendorId}</td>
                <td>${vendor.vendorName}</td>
                <td>${vendor.vendorPhoneNumber}</td>
                <td>${vendor.vendorAddress?.cityName || ""}</td>
                <td>
                    <button onclick="editVendor(${vendor.vendorId})">Edit</button>
                    <button onclick="deleteVendor(${vendor.vendorId})">Delete</button>
                </td>
            </tr>
        `;
    });
}

function showVendorForm(vendor) {

    var formContainer = document.getElementById("vendorFormContainer");

    var vendorId = "";
    var vendorName = "";
    var vendorPhone = "";
    var doorNumber = "";
    var streetName = "";
    var cityName = "";
    var cityType = "";
    var stateName = "";
    var countryName = "";
    var pincode = "";

    if (vendor != null) {
        vendorId = vendor.vendorId;
        vendorName = vendor.vendorName;
        vendorPhone = vendor.vendorPhoneNumber;

        if (vendor.vendorAddress != null) {
            doorNumber = vendor.vendorAddress.doorNumber;
            streetName = vendor.vendorAddress.streetName;
            cityName = vendor.vendorAddress.cityName;
            cityType = vendor.vendorAddress.cityType;
            stateName = vendor.vendorAddress.stateName;
            countryName = vendor.vendorAddress.countryName;
            pincode = vendor.vendorAddress.pincode;
        }
    }
    formContainer.innerHTML = `
    <h3>${vendor != null ? "Update Vendor" : "Add Vendor"}</h3>

    <input type="hidden" id="vendorId" value="${vendorId}">

    <input type="text" id="vendorName" placeholder="Vendor Name" value="${vendorName}"><br><br>

    <input type="text" id="vendorPhone" placeholder="Phone Number" value="${vendorPhone}"><br><br>

    <h4>Address</h4>

    <input type="text" id="doorNumber" placeholder="Door Number" value="${doorNumber}"><br><br>

    <input type="text" id="streetName" placeholder="Street Name" value="${streetName}"><br><br>

    <input type="text" id="cityName" placeholder="City Name" value="${cityName}"><br><br>

    <input type="text" id="cityType" placeholder="City Type" value="${cityType}"><br><br>

    <input type="text" id="stateName" placeholder="State Name" value="${stateName}"><br><br>

    <input type="text" id="countryName" placeholder="Country Name" value="${countryName}"><br><br>

    <input type="text" id="pincode" placeholder="Pincode" value="${pincode}"><br><br>

    <button onclick="saveVendor()">Save</button>
`;
}

async function saveVendor() {

    const vendorId = document.getElementById("vendorId").value;

    if (vendorId) {
        const data = {
            vendorId: vendorId,
            vendorName: document.getElementById("vendorName").value,
            vendorPhoneNumber: document.getElementById("vendorPhone").value,
            vendorAddress: {
                doorNumber: document.getElementById("doorNumber").value,
                streetName: document.getElementById("streetName").value,
                cityName: document.getElementById("cityName").value,
                cityType: document.getElementById("cityType").value,
                stateName: document.getElementById("stateName").value,
                countryName: document.getElementById("countryName").value,
                pincode: document.getElementById("pincode").value
            }
        };

        console.log(data);

        await fetch(`${BASE_URL}/vendors`, {
            method: "PATCH",
            headers: { "Content-Type": "application/json" },
            body: JSON.stringify(data)
        });

    } else {

        const data = {
            vendorName: document.getElementById("vendorName").value,
            vendorPhoneNumber: document.getElementById("vendorPhone").value,
            vendorAddress: {
                doorNumber: document.getElementById("doorNumber").value,
                streetName: document.getElementById("streetName").value,
                cityName: document.getElementById("cityName").value,
                cityType: document.getElementById("cityType").value,
                stateName: document.getElementById("stateName").value,
                countryName: document.getElementById("countryName").value,
                pincode: document.getElementById("pincode").value
            }
        };

        await fetch(`${BASE_URL}/vendors`, {
            method: "POST",
            headers: { "Content-Type": "application/json" },
            body: JSON.stringify(data)
        });
    }

    document.getElementById("vendorFormContainer").innerHTML = "";
    fetchVendors();
}

async function editVendor(id) {

    const response = await fetch(`${BASE_URL}/vendors/${id}`);
    const vendor = await response.json();

    showVendorForm(vendor);
}

async function deleteVendor(id) {

    await fetch(`${BASE_URL}/vendors/${id}`, {
        method: "DELETE"
    });

    fetchVendors();
}