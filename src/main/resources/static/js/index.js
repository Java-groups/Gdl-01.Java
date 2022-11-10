function submitForm(){

    const username = document.querySelector("#user-name").value;
    const password = document.querySelector("#password").value;
    let jsonData = {
        username,
        password
    };

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
        localStorage.setItem('bearer', data.token);
        window.location.replace('/start');
      })

      .catch((error) => {
        console.error('Error:', error);
      });
}