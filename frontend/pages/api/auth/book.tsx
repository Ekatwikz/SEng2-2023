import { BookData } from "@/pages/types";

const ENDPOINT = "http://localhost:8069/bookings";

export const bookAircraft = async(data: BookData, token: string | undefined): Promise<BookData> => {
    if (!data.bookingStartDateTime || !data.bookingEndDateTime || !data.aircraftId) {
        throw data;
    }

    return await fetch(ENDPOINT, {
        method: "POST",
        headers: {
            "Content-Type": "application/json",
            "Authorization": "Bearer " + token,
            "User-Agent": "undici"
        },
        body: JSON.stringify(data)
    })
    .then(response => {
        if (response.ok){
            return response.json();
        } else {
            throw response;
        }
    })
    .catch(error => {
        console.error(JSON.stringify(error));
    });
};

