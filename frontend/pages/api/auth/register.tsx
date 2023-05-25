import { RegisterData } from "../../register";

const ENDPOINT = "http://localhost:8069/logic/api/users";
const EMAIL_REGEX = /^[a-zA-Z0-9.!#$%&'*+/=?^_`{|}~-]+@[a-zA-Z0-9-]+(?:\.[a-zA-Z0-9-]+)*$/;

export const auth = async(data: RegisterData): Promise<boolean> => {
    if (!data.email || !data.username || !data.password || !data.passwordRep || data.password !== data.passwordRep) {
        return false;
    }

    if (!data.email.match(EMAIL_REGEX)) {
        return false;
    }

    await fetch(ENDPOINT, {
        method: "POST",
        headers: { "Content-Type": "application/json" },
        body: JSON.stringify({
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

    return true;
};

