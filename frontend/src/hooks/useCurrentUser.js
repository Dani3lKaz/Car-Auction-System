import { useQuery } from "@tanstack/react-query";
import { getCurrentUser } from '../api/userApi.js'

export function useCurrentUser() {
    return useQuery({
        queryKey: ["currentUser"],
        queryFn: getCurrentUser,
    });
}