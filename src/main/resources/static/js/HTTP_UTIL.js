async function sendRequest(url, json) {
    const bearerCookie = localStorage.getItem('bearer');
        const request =  axios.post("/api/article/new", json, {
                            headers: {
                                "Content-Type": "application/json",
                                "Authorization": `Bearer ${bearerCookie}`
                            },
                        });
    return await request.json();
}