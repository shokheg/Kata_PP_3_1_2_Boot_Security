$(async function () {
    await mainTableWithUserId()
})

//=========ВЫВОД ТАБЛИЦЫ ЮЗЕРА=========ГОТОВО
async function mainTableWithUserId() {
    let table = $('#mainTableWithUserId');
    table.empty();

    fetch('api/user')
        .then(res => res.json())
        .then(user => {
            let tablePrincipal = `$(
                <tr>
                    <td>${user.id}</td>
                    <td>${user.firstName}</td>
                    <td>${user.lastName}</td>
                    <td>${user.age}</td>
                    <td>${user.email}</td>
                    <td>${user.roles.map(role => role.name.replace('ROLE_', ''))}</td>
                </tr>
            )`
            table.append(tablePrincipal)
        })
}






