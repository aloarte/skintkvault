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
                        if(response.status === 401){
                            alert(`Invalid email. Please, ensure that the mail ${email} was used in the Skintker android application.`);
                        }
                        else{
                            alert("Something went wrong trying to removing your data. Please, try again and if this error persist, contact skintker@gmail.com");
                        }
                    }
                    else{
                        return response.json();
                    }
                })
                .then(data => {
                        console.log('Backend response:', data);
                        if (data === true) {
                            alert(`Data from the user ${email} successfully removed.`);
                        }
                })
                .catch(error => {
                    console.error('Error:', error);
                });
        }
   }