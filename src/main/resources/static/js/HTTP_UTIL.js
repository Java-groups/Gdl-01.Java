async function sendRequest(url, json) {
    const bearerCookie = localStorage.getItem('bearer');
    try{
        const request = await fetch(url, {
                method: 'POST',
                headers: {
                    'Accept': 'application/json',
                    'Content-Type': 'application/json',
                    'Authorization': `Bearer ${bearerCookie}`
                },
                body: JSON.stringify(json)
        });
    }catch(e){
        console.error(e);
    }

    return await request.json();
}