import { Container, Card, Typography, Stack, Box, IconButton, Dialog, DialogTitle, Grid, Button, DialogActions } from "@mui/material";
import { useEffect, useState } from "react";
import { useSession } from "next-auth/react";
import { AircraftUser, Aircraft, BookData } from "./types";
import { Add, Search } from "@mui/icons-material";
import { DatePicker, LocalizationProvider } from "@mui/x-date-pickers";
import { AdapterDayjs } from "@mui/x-date-pickers/AdapterDayjs";
import dayjs, { Dayjs } from "dayjs";
import { enqueueSnackbar } from "notistack";

import { bookAircraft } from "./api/auth/book";
import { useRouter } from "next/router";

export default function AircraftPage() {
    const [open, setOpen] = useState(false);
    const [airId, setAirId] = useState(0);
    const [startDate, setStartDate] = useState<Dayjs | null>(dayjs("2022-04-17"));
    const [endDate, setEndDate] = useState<Dayjs | null>(dayjs("2022-04-17"));
    const [aircrafts, setAircrafts] = useState(Array<Aircraft>);
    const { data: session } = useSession();
    const aircraftUser = session?.user as AircraftUser | undefined;
    const router = useRouter();

    const handleLearn = (aircraft: Aircraft) => {
        router.push({
            pathname: "/plane",
            query: {
                id: aircraft.aircraftId,
                name: aircraft.aircraftName,
                type: aircraft.aircraftType,
                registration: aircraft.aircraftRegistration,
                description: aircraft.aircraftDescription
            }
        });
    };

    const handleClickOpen = (aircraftId: number) => {
        setOpen(true);
        setAirId(aircraftId);
      };

    const handleClose = () => {
        setOpen(false);
        setAirId(0);
    };

    const saveBooking = async () => {
        const data: BookData = {
            aircraftId: airId,
            bookingStartDateTime: startDate,
            bookingEndDateTime: endDate
        };

        const response = await bookAircraft(data, aircraftUser?.jwttoken);

        if (!response) {
            enqueueSnackbar("Booking Failed", {
                key: "bookingFail",
                variant: "error"
              });
        } else {
            enqueueSnackbar("Booking Saved Successfully", {
                key: "bookingSuccess",
                variant: "success"
              });
        }

        setOpen(false);
    };

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
                            <IconButton onClick={() => handleClickOpen(air.aircraftId)}>
                                <Add sx={{ fontSize: 25 }} />
                            </IconButton>
                            <IconButton onClick={() => handleLearn(air)}>
                                <Search sx={{ fontSize: 25 }} />
                            </IconButton>
                            <Dialog open={open} onClose={handleClose}>
                                <DialogTitle>Book</DialogTitle>
                                <Box sx={{
                                    display: "flex",
                                    flexDirection: "column",
                                    alignTypographys: "center",
                                    justifyContent: "space-around",
                                    margin: "0 0 0 0 "}}>

                                <LocalizationProvider dateAdapter={AdapterDayjs}>
                                    <Grid container>
                                        <Grid xs={6} sx={{bgcolor: "white", borderRadius: "5px", padding: "5px"}}>
                                            <DatePicker label="From:" onChange={(newValue: Dayjs | null) => setStartDate(newValue)}/>
                                        </Grid>
                                        <Grid xs={6} sx={{bgcolor: "white", borderRadius: "5px", padding: "5px"}}>
                                            <DatePicker label="From:" onChange={(newValue: Dayjs | null) => setEndDate(newValue)}/>
                                        </Grid>
                                    </Grid>
                                </LocalizationProvider>

                                <DialogActions>
                                    <Button onClick={handleClose}>Cancel</Button>
                                    <Button onClick={saveBooking}>Save</Button>
                                </DialogActions>
                                </Box>
                            </Dialog>
                        </Box>
                    </Card>
                ))
            }
        </Container>
    );
}
