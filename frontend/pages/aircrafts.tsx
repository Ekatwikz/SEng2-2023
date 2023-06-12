import { Container, Card, Typography, Stack, Box, IconButton } from "@mui/material";
import { useEffect, useState } from "react";
import { useSession } from "next-auth/react";
import { AircraftUser, Aircraft } from "./types";
import { Add } from "@mui/icons-material";

export default function AircraftPage() {
    const { data: session } = useSession();
    const aircraftUser = session?.user as AircraftUser | undefined;
    const [aircrafts, setAircrafts] = useState(Array<Aircraft>);

        useEffect(() => {
        if (aircraftUser) {
            fetch("http://localhost:8069/aircrafts", {
                method: "GET",
                headers: {
                    "Authorization": "Bearer " + aircraftUser.jwttoken,
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
                    setAircrafts(aircraftList);
                });
        }

        // again, only fine here b/c necessity
        // eslint-disable-next-line react-hooks/exhaustive-deps
    }, [aircraftUser]);

    return (
        <Container component="main" maxWidth="md">
            {
                aircrafts.map(air => (
                    <Card key={air.aircraftId} sx={{mb: 2}}>
                        <Box sx={{ p: 2, display: "flex" }}>
                            <Stack spacing={0.5} width="90%">
                            <Typography fontWeight={700}> {air.aircraftName}: {air.aircraftType}</Typography>
                            <Typography variant="body2" color="text.secondary"> {air.aircraftRegistration} </Typography>
                            </Stack>
                            <IconButton>
                                <Add sx={{ fontSize: 25 }} />
                            </IconButton>
                        </Box>
                    </Card>
                ))
            }
        </Container>
    );
}
