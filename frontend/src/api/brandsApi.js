import { request } from './client.js'

export const getAllBrands = async () => {
    const brands = await request.get("/api/brands")
    return brands
}