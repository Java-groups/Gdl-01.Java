async function sendRequest(url, json) {
    const bearerCookie = localStorage.getItem('bearer');
        const request =  axios.post(url, json, {
                            headers: {
                                "Content-Type": "application/json",
                                "Authorization": `Bearer ${bearerCookie}`
                            },
                        }).then(res =>
                        {
                        return {message : res.data.message}
                        })
                        .catch(e =>{
                            throw new Error(e.response.data.message);
                        });
    return await request;
}