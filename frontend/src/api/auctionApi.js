import { request } from './client.js'

export const getAllAuctions = async () => {
    const auctions = await request.get("/api/auctions")
    return auctions
}

export const createAuction = async (auction, image) => {
    const formData = new FormData()
    formData.append('auction', new Blob([JSON.stringify(auction)], {type: 'application/json'}))
    if (image) {
        formData.append('image', image)
    }
    return await request.postForm('/api/auctions', formData)
}
