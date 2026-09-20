const BASE_URL = "http://localhost:8080"

class ApiError extends Error {
    constructor(status, data) {
        super(`API error ${status}`);
        this.status = status;
        this.data = data;
    }
}

const apiRequest = async (endpoint, options = {}) => {

    const res = await fetch(`${BASE_URL}${endpoint}`, {
        ...options,
        credentials: "include",
        headers: {
            'content-type': 'application/json',
            ...options.headers,
        }
    })

    if(!res.ok) {
        const data = await res.json().catch(() => null)
        throw new ApiError(res.status, data)
    }

    if (res.status === 204) {return undefined}

    return res.json()
}

export const request = {
    get: (endpoint) => apiRequest(endpoint),
    post: (endpoint, data) => apiRequest(endpoint, {method: 'POST', body: JSON.stringify(data) }),
    put: (endpoint, data) => apiRequest(endpoint, {method: 'PUT', body: JSON.stringify(data) }),
    patch: (endpoint, data) => apiRequest(endpoint, {method: 'PATCH', body: JSON.stringify(data) }),
}