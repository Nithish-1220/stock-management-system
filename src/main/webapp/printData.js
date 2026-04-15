function createTable(jsonData,buttonContent,buttonAction) {
    const table = document.createElement('table');

    const headerRow = document.createElement('tr');
    const headers = Object.keys(jsonData[0]);
    headers.forEach(header => {
        const th = document.createElement('th');
        th.textContent = header;
        headerRow.appendChild(th);
    });
    table.appendChild(headerRow);

    const actionTh = document.createElement('th');
    actionTh.textContent = "Actions";
    headerRow.appendChild(actionTh);
    table.appendChild(headerRow);

    jsonData.forEach(item => {
        const row = document.createElement('tr');
        headers.forEach(header => {
            const td = document.createElement('td');
            td.textContent = item[header];
            row.appendChild(td);
        });

        const btnTd = document.createElement('td');
        const btn = document.createElement('button');
        btn.type = 'button';
        btn.textContent = buttonContent;
        btn.onclick = () => buttonAction(item[headers[0]]);
        btnTd.appendChild(btn);
        row.appendChild(btnTd);

        table.appendChild(row);
    });
    return table;
}