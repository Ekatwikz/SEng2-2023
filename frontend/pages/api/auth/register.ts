import { RegisterData } from "../../register";

const EMAIL_REGEX = /^[a-zA-Z0-9.!#$%&'*+/=?^_`{|}~-]+@[a-zA-Z0-9-]+(?:\.[a-zA-Z0-9-]+)*$/;
const ENDPOINT = "http://localhost:8069/registration";

export const auth = async(data: RegisterData): Promise<boolean> => {
    if (!data.email || !data.username || !data.password || !data.passwordRep || data.password !== data.passwordRep) {
        return false;
    }

    if (!data.email.match(EMAIL_REGEX)) {
        return false;
    }

    return await fetch(ENDPOINT, {
        method: "POST",
        headers: { "Content-Type": "application/json" },
        body: JSON.stringify({
            "firstName": data.firstName,
            "lastName": data.lastName,
            "username": data.username,
            "password": data.password,
            "email": data.email
        })
    })
    .then(response => {
        if (response.ok){
            console.log(response.json());
            return true;
        } else {
            throw response;
        }
    })
    .catch(error => {
        console.error(JSON.stringify(error));
        return false;
    });
};

