import { useSession } from "next-auth/react";
import { useEffect, useState } from "react";
import { withRouter } from "next/router";
import { Box, Button, Card, Container, Divider, Grid, ImageList, ImageListItem, Stack, Typography } from "@mui/material";
import { DatePicker, LocalizationProvider } from "@mui/x-date-pickers";
import { AdapterDayjs } from "@mui/x-date-pickers/AdapterDayjs";
import dayjs, { Dayjs } from "dayjs";
import { enqueueSnackbar } from "notistack";

import { Aircraft, AircraftUser, AircraftImage, BookData } from "./types";
import { bookAircraft } from "./api/auth/book";

function Plane(props: any) {
    const [startDate, setStartDate] = useState<Dayjs | null>(dayjs("2022-04-17"));
    const [endDate, setEndDate] = useState<Dayjs | null>(dayjs("2022-04-17"));

    const { data: session } = useSession();
    const aircraftUser = session?.user as AircraftUser | undefined;

    const aircraft: Aircraft = {
        aircraftId: props.router.query.id,
        aircraftName: props.router.query.name,
        aircraftType: props.router.query.type,
        aircraftRegistration: props.router.query.registration,
        aircraftDescription: props.router.query.description
    };

    const [images, setImages] = useState<Array<string>>([]);

    const saveBooking = async () => {
        const data: BookData = {
            aircraftId: aircraft.aircraftId,
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
    };

    useEffect(() => {
        async function fetchData() {
            if (aircraftUser) {
                const imageDatas: Array<AircraftImage> | void = await fetch(`http://localhost:8069/aircrafts/${aircraft.aircraftId}/images`, {
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
                    .catch(error => {
                        console.error(JSON.stringify(error));
                    });

                const returnImages: Array<string> = [];
                for (const imageData of imageDatas ? imageDatas : []) {
                    await fetch(`http://localhost:8069/aircrafts/${aircraft.aircraftId}/images/${imageData.aircraftImageId - 1}`, {
                        method: "GET",
                        headers: {
                            "Authorization": "Bearer " + aircraftUser.jwttoken,
                            "User-Agent": "undici"
                        },
                    })
                    .then(response => {
                        if (response.ok) {
                            return response.blob();
                        } else {
                            throw response;
                        }
                    })
                    .then(blob => {
                        returnImages.push(URL.createObjectURL(blob));
                    })
                    .catch(error => {
                        console.error(JSON.stringify(error));
                    });
                }

                setImages(returnImages);
            }
        }
        fetchData();
        // again, only fine here b/c necessity
        // eslint-disable-next-line react-hooks/exhaustive-deps
    }, [aircraftUser]);

  return (
    <Container component="main" maxWidth="xl">
        <Card elevation={6} sx={{
            padding: 4,
            borderRadius: 4
        }}>
            <Box
                sx={{
                    display: "flex",
                    flexDirection: "column",
                    alignItems: "left",
                }}

                >
                <Grid container columnSpacing={2}>
                    <Grid item xs={8}>
                        <Box sx={{ width: 500, height: 450, overflowY: "scroll" }}>
                            <ImageList variant="masonry" cols={3} gap={8}>
                                {images.map((item) => (
                                <ImageListItem key={item}>
                                    {/* eslint-disable-next-line @next/next/no-img-element */}
                                    <img src={item} alt=""/>
                                </ImageListItem>
                                ))}
                            </ImageList>
                        </Box>
                    </Grid>
                    <Grid item xs={4}>
                        <Box sx={{ p: 1, display: "flex", flexDirection: "row" }}>
                            <Stack alignContent="space-between">
                                <Stack spacing={1}>
                                    <Typography fontWeight={700} sx={{alignSelf: "center"}}>INFORMATION</Typography>
                                    <Divider/>
                                    <Typography> <b>Name</b>: {aircraft.aircraftName} </Typography>
                                    <Typography> <b>Type</b>: {aircraft.aircraftType} </Typography>
                                    <Typography> <b>Registration</b>: {aircraft.aircraftRegistration} </Typography>
                                    <Typography> <b>Description</b>: {aircraft.aircraftDescription} </Typography>
                                </Stack>
                                <Divider/>
                                <Stack spacing={1} sx={{mt: "60%"}}>
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
                                    <Button variant="contained" sx={{width: "150px", alignSelf: "center"}} onClick={saveBooking}>Book</Button>
                                </Stack>
                            </Stack>
                        </Box>
                    </Grid>
                </Grid>
            </Box>
        </Card>
    </Container>
  );
}

export default withRouter(Plane);
