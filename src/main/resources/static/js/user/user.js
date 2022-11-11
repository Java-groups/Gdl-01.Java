function saveUser(){
    const userForm = document.querySelector("#user-form");

    const spinner = document.querySelector("#spinner-loading");

    spinner.style.display = "block";

    let bodyFormData = new FormData();
    bodyFormData.append('firstname', userForm[0].value);
    bodyFormData.append('lastname', userForm[1].value);
    bodyFormData.append('email', userForm[2].value);
    bodyFormData.append('password', userForm[3].value);
    bodyFormData.append('confirmPassword', userForm[4].value);

    sendRequest('/api/user/create-account', bodyFormData).then(data => {
        const divAlert = document.querySelector('#error-message');
        divAlert.innerHTML = "";

        const generalMessage = document.querySelector("#general-message");
        generalMessage.innerHTML = data.message;
    }).catch(e =>{
        const message = e.message;
        const divAlert = document.querySelector('#error-message');
        divAlert.innerHTML = message;
    });

    spinner.style.display = "none";
}