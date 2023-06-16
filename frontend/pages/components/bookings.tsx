import { Container, Card, Typography, Stack, Box, Avatar, Grid} from "@mui/material";
import { useEffect, useState } from "react";
import { useSession } from "next-auth/react";
import AirplanemodeActiveIcon from "@mui/icons-material/AirplanemodeActive";
import { Aircraft, AircraftUser, Booking } from "../types";

export default function Bookings() {
    const [bookings, setBookings] = useState<Array<Booking>>([]);
    const [aircrafts, setAircrafts] = useState<Array<Aircraft>>([]);
    const { data: session } = useSession();
    const aircraftUser = session?.user as AircraftUser | undefined;

    useEffect(() => {
        if (aircraftUser) {
            fetch("http://localhost:8069/bookings", {
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
            .then(userBookings => {
                setBookings(userBookings);
            });

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
                bookings.map(book => (
                    <Card key={book.bookingId} sx={{mt: 2}}>
                        <Grid container columns={2} display="flex" flexDirection="row" alignItems="space-between" spacing={20}>
                            <Grid item>
                                <Box sx={{ p: 2, display: "flex", flexDirection: "row", alignItems: "left" }}>
                                    <Avatar className="logo" sx={{ m: 2, bgcolor: "grey" }}>
                                        <AirplanemodeActiveIcon />
                                    </Avatar>
                                    <Stack spacing={0.5}>
                                        <Typography fontWeight={700}>{ aircraftUser?.firstName ?? "Anon"} { aircraftUser?.lastName ?? "y Mous iksde"}</Typography>
                                        <Typography fontWeight={400}> {aircrafts.find(air => air.aircraftId === book.aircraftId)?.aircraftName}: {aircrafts.find(air => air.aircraftId === book.aircraftId)?.aircraftType}</Typography>
                                    </Stack>
                                </Box>
                            </Grid>
                            <Grid item>
                                <Box sx={{ p: 2, display: "flex", flexDirection: "row", alignContent: "normal" }}>
                                    <Stack spacing={0}>
                                        <Grid container columns={2} spacing={1}>
                                            <Grid item>
                                                <Typography fontWeight={700}> From: </Typography>
                                            </Grid>
                                            <Grid item>
                                                <Typography> {book.bookingStartDateTime.toString().slice(0, 10)} </Typography>
                                            </Grid>
                                        </Grid>
                                        <Grid container columns={2} spacing={1}>
                                            <Grid item>
                                                <Typography fontWeight={700}> To: </Typography>
                                            </Grid>
                                            <Grid item>
                                                <Typography> {book.bookingEndDateTime.toString().slice(0, 10)} </Typography>
                                            </Grid>
                                        </Grid>
                                    </Stack>
                                </Box>
                            </Grid>
                        </Grid>
                    </Card>
                ))
            }
        </Container>
    );
}
