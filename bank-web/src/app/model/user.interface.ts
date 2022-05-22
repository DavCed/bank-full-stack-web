export interface User {
    id?: number
    firstName: string
    lastname: string
    email: string
    password: string
    phoneNumber: string
    userType: string
}

export interface Credentials {
    email: string
    password: string
}

export interface UserResponse {
    userId?: number
    name: string
    email: string
    message: string
}