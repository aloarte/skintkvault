function onWipeButton() {
        var email = document.getElementById("emailInput").value;

        fetch('/wipedata', {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json'
            },
            body: JSON.stringify({ email: email })
        })
        .then(response => {
            if (!response.ok) {
                throw new Error('Error al enviar el correo electrónico al backend');
            }
            return response.json();
        })
        .then(data => {
                console.log('Respuesta del backend:', data);

                if (data === true) {
                    mostrarMensaje("Contenido eliminado correctamente");
                } else {
                    console.log('La respuesta del backend no es true');
                }
        })
        .catch(error => {
            console.error('Error:', error);
        });
    }

    function mostrarMensaje(mensaje) {
        alert(mensaje);
    }