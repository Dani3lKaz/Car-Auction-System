import { useQuery } from "@tanstack/react-query";
import { getAllBrands } from '../api/brandsApi.js'

export function useBrands() {
    return useQuery({
        queryKey: ["brands"],
        queryFn: getAllBrands,
    });
}
