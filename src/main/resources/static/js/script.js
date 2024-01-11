function onWipeButton() {
        var email = document.getElementById("emailInput").value;
        var emailRegex = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;

        if(email === "" || !emailRegex.test(email)){
            alert("Please add a valid email address");
        }
        else{
            fetch('/reports/mail', {
                    method: 'DELETE',
                    headers: {
                        'Content-Type': 'application/json'
                    },
                    body: JSON.stringify({ email: email })
                })
                .then(response => {
                    if (!response.ok) {
                        alert("Something went wrong trying to removing your data. Please, try again and if this error persist, contact skintker@gmail.com");
                    }
                    return response.json();
                })
                .then(data => {
                        console.log('Backend response:', data);
                        if (data === true) {
                            alert("Contenido eliminado correctamente");
                        } else {
                            alert("Something went wrong trying to removing your data. Please, try again and if this error persist, contact skintker@gmail.com");
                        }
                })
                .catch(error => {
                    console.error('Error:', error);
                });
        }
   }