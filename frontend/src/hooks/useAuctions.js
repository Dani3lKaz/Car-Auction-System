import { useQuery } from "@tanstack/react-query";
import { getAllAuctions } from '../api/auctionApi.js'

export function useAuctions() {
    return useQuery({
        queryKey: ["auctions"],
        queryFn: getAllAuctions,
    });
}
