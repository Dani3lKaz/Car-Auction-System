import { request } from './client.js'

export const getAllAuctions = async () => {
    const auctions = await request.get("/api/auctions")
    return auctions
}