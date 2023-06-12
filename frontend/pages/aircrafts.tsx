import { Container } from "@mui/material";
import { useEffect, useState } from "react";
import { useSession } from "next-auth/react";
import { AircraftUser } from "./types";

export class Aircraft {
    id: number;
    name: string;
    type: string;
    registration: string;
    description: string;

    constructor(id: number, name: string, type: string, registration: string, description: string) {
        this.id = id;
        this.name = name;
        this.type = type;
        this.registration = registration;
        this.description = description;
    }
}

export default function AircraftPage() {
    const { data: session } = useSession();
    const aircraftUser = session?.user as AircraftUser | undefined;
    const [aircrafts, setAircrafts] = useState(Array<Aircraft>);

    useEffect( () => {
        async function fetchData() {
            try {
                await fetch("http://localhost:8069/aircrafts", {
                    method: "GET",
                    headers: {
                        "Authorization": "Bearer " + aircraftUser?.jwttoken,
                        "User-Agent": "undici"
                    },
                })
                .then(response => {
                    if (response.ok){
                        return response.json();
                    } else {
                        throw response;
                    }
                })
                .then(aircraftList => {
                    let aircraftsArray: Array<Aircraft> = [];
                    aircraftList.forEach( (aircraft: object) => {
                        aircraftsArray.push(aircraft.aircraftId, aircraft.aircraftName, aircraft.aircraftType,
                            aircraft.aircraftRegistration, aircraft.aircraftDescription);
                    });

                    setAircrafts(aircraftsArray);
                    console.log(aircrafts);
                    console.log(aircraftsArray);
                    console.log(aircraftList);
                });
            } catch (err) {
                console.log(err);
            }
        }
        fetchData();
    }, [aircrafts, aircraftUser?.jwttoken]);

    return (
        <Container component="main" maxWidth="xl">

        </Container>
    );
}
