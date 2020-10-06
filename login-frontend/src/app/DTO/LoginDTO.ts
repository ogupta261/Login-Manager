export interface LoginDTO{
    userName: string;
    role: string;
    authConsent: boolean;
    lastLoginDate: Date;
}