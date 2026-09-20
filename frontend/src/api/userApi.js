import { request } from './client.js'

export const login = async (email, password) => {
    return await request.post('/api/auth/generateToken', {email, password})
}

export const register = async (firstName, lastName, email, password) => {
    return await request.post('/api/users', {firstName, lastName, email, password})
}

export const getCurrentUser = async () => {
    try {
        return await request.get('/api/users/me')
    }catch(err) {
        if (err.status === 401) return null
        throw err
    }
}

export const logout = async () => await request.post('/api/auth/logout')