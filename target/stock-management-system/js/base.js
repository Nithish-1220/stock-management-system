const BASE_URL = "http://10.51.240.137:8080/stock-management-system/api";

window.AppConfig = {
    shopId: null,
    branchId: null
};

document.querySelector(".dashboard").classList.add("active");

document.querySelectorAll(".sidebar li").forEach(item => {
    item.addEventListener("click", function () {
        document.querySelectorAll(".sidebar li")
            .forEach(li => li.classList.remove("active"));

        this.classList.add("active");
        loadPage(this.classList[0]);
    });
});

loadShops();

async function loadShops() {
    const select = document.getElementById("shopSelect");
    select.innerHTML += `<option>select Shop</option>`
    const response = await fetch(`${BASE_URL}/shops`);
    const shops = await response.json();

    shops.forEach(shop => {
        select.innerHTML += `
                <option value="${shop.shopId}">
                    ${shop.shopName} (${shop.shopOwnerName})
                </option>`
    })

    select.onchange = () => { AppConfig.shopId = select.value; 
                loadBranches();
    };

}


async function loadBranches() {
    const select = document.getElementById("branchSelect");
    select.innerHTML = `<option>select Branch</option>`
    const response = await fetch(`${BASE_URL}/shops/${AppConfig.shopId}/branches`);
    const branches = await response.json();

    branches.forEach(branch => {
        select.innerHTML += `
                <option value="${branch.branchId}">
                    ${branch.branchName}
                </option>`
    })

    select.onchange = () => { AppConfig.branchId = select.value; };
    console.log(AppConfig.branchId)
}


function loadPage(page) {

    const content = document.getElementById("content");

    if (page === "shops") {
        loadShopsPage();
    }

    if (page === "branches") {
        loadBranchesPage();
    }

    if (page === "vendors") {
        loadVendorsPage();
    }

    if (page === "products") {
        loadProductsPage();
    }

    if (page === "inventory") {
        loadInventoryPage();
    }

    if (page === "purchases") {
        loadPurchasePage();
    }

    if (page === "sales") {
        loadSalesPage();
    }

    if (page === "reports") {
        loadReportsPage();
    }
}

lucide.createIcons();