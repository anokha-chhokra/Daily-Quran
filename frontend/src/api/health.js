import { apiGet } from "./client";

export function checkHealth(){
    return apiGet('/health')
}