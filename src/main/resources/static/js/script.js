var attempts = 0;
var maxAttempts = 5;
var cooldownSeconds = 60;
var secondsLeft = cooldownSeconds;
var intervalId;

function resetCooldown() {
    var emailInput = document.getElementById("emailInput");
    var wipeButton = document.getElementById("wipeButton");
    attempts = 0;
    secondsLeft = cooldownSeconds
    clearInterval(intervalId);
    wipeButton.disabled = false;
    wipeButton.style.backgroundColor = "";
    wipeButton.innerText = "Wipe data";
}


function onWipeButton() {
    var emailInput = document.getElementById("emailInput");
    var wipeButton = document.getElementById("wipeButton");
    var email = emailInput.value;
    var emailRegex = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;
    if (email === "" || !emailRegex.test(email)) {
        alert("Please add a valid email address");
    } else {
        fetch('/reports/mail', {
            method: 'DELETE',
            headers: {
                'Content-Type': 'application/json'
            },
            body: JSON.stringify({ email: email })
        })
        .then(response => {
            if (!response.ok) {
                attempts++;
                if (attempts >= maxAttempts) {
                    wipeButton.disabled = true;
                    wipeButton.style.backgroundColor = "gray";
                    wipeButton.innerText = `Retry in ${secondsLeft} seconds`;
                    intervalId = setInterval(function () {
                        secondsLeft--;
                        if (secondsLeft <= 0) {
                            resetCooldown();
                        } else {
                            wipeButton.innerText = `Retry in ${secondsLeft} seconds`;
                        }
                    }, 1000);
                }

                if (response.status === 401) {
                    alert(`Invalid email. Please, ensure that the mail ${email} was used in the Skintker android application.`);
                } else {
                    alert("Something went wrong trying to removing your data. Please, try again and if this error persists, contact skintker@gmail.com");
                }
            } else {
                return response.json();
            }
        })
        .then(data => {
            if (data === true) {
                alert(`Data from the user ${email} successfully removed.`);
                resetCooldown();
            }
        })
        .catch(error => {
            console.error('Error:', error);
            resetCooldown();
        });
    }
}