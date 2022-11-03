function submitForm(){

    const userName = document.querySelector("#user-name").value;
    const password = document.querySelector("#password").value;
    let jsonData = {
        userName,
        password
    };
    console.log(jsonData);

    let bearerCookie = fetch('/sport-hub/api/signin', {
        method: 'POST',
        headers: {
            'Accept': 'application/json',
            'Content-Type': 'application/json',
        },
        body: JSON.stringify(jsonData)
    })
      .then((response) => response.json())
      .then((data) => {
        console.log('Success:', data);
        localStorage.setItem('bearer', data.token);
      })
      .catch((error) => {
        console.error('Error:', error);
      });
}