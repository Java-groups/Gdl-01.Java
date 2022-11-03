function sendRequest(url, json) {
    const bearerCookie = getCookie("sportHubApp");
    fetch(url, {
        method: 'POST',
        headers: {
            'Accept': 'application/json',
            'Content-Type': 'application/json',
            'Authorization': `Bearer ${bearerCookie}`
        },
        body: JSON.stringify(json)
    });
}