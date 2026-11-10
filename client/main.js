// El backend se publica en el puerto 8089 del mismo servidor que entrega esta pagina
const API_URL = `http://${window.location.hostname}:8089/people`;

const showMessage = (text) => {
    document.getElementById('message').textContent = text;
}

const findAll = async () => {
    let tbody = document.getElementById('tbody');
    showMessage('Cargando...');

    try {
        const response = await fetch(API_URL, {
            method: 'GET',
            headers: {
                "Accept": "application/json",
            }
        });
        const result = await response.json();

        if (!response.ok) {
            throw new Error(result.message);
        }

        tbody.replaceChildren();

        // textContent evita que un nombre con HTML se ejecute en la pagina
        result.data.forEach(itm => {
            let row = document.createElement('tr');
            let cell = document.createElement('td');
            cell.textContent = itm.name;
            row.appendChild(cell);
            tbody.appendChild(row);
        });

        showMessage('');
    } catch (error) {
        showMessage('No se pudo cargar la lista de personas');
    }
}

(() => {
    findAll();
})()

const savePerson = async () => {
    const input = document.getElementById('name');
    const person = {
        name: input.value.trim()
    }

    if (person.name === '') {
        showMessage('Escribe un nombre');
        return;
    }

    showMessage('Guardando...');

    try {
        const response = await fetch(API_URL, {
            method: 'POST',
            headers: {
                "Content-Type": "application/json",
                "Accept": "application/json",
            },
            body: JSON.stringify(person)
        });
        const result = await response.json();

        if (!response.ok) {
            showMessage(result.message || 'No se pudo guardar la persona');
            return;
        }

        input.value = '';
        await findAll();
        showMessage(result.message);
    } catch (error) {
        showMessage('No se pudo guardar la persona');
    }
}
