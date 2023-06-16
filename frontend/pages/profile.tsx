import { useState } from "react";

import { signOut, useSession } from "next-auth/react";
import { useRouter } from "next/router";

import AccountCircleIcon from "@mui/icons-material/AccountCircle";
import { Avatar, Box, Button, Card, Container, Divider, Grid, MenuItem, MenuList, Stack, Typography } from "@mui/material";

import { AircraftUser } from "./types";
import Bookings from "./components/bookings";
import Certificates from "./components/certificates";

export default function About() {
  const { data: session } = useSession();
  const aircraftUser = session?.user as AircraftUser | undefined;
  const router = useRouter();
  const [subPage, setSubPage] = useState("Bookings");

  if (!session) {
    router.push("/login");
  }

  return (
    <Container component="main">
      <Card elevation={6} sx={{
        padding: 4,
        borderRadius: 4
      }}>
        <Box
          sx={{
            display: "flex",
            flexDirection: "column",
            alignItems: "left",
            justifyContent: "space-around"
          }}
        >
          <Box sx={{ p: 1, display: "flex" }}>
            <Avatar className='logo' sx={{ m: 2, bgcolor: "grey" }}>
              <AccountCircleIcon />
            </Avatar>
            <Stack spacing={0.5}>
              <Typography fontWeight={700}>{aircraftUser?.firstName ?? "Anon"} {aircraftUser?.lastName ?? "y Mous iksde"}</Typography>
              <Typography variant="body2" color="text.secondary"> {aircraftUser?.email} </Typography>
            </Stack>
            <Button variant="contained" sx={{ width: "150px", alignSelf: "center", ml: "500px" }}
              onClick={() => {
                signOut({
                  redirect: false
                });
                router.push("/login");
              }}>Log out</Button>
          </Box>
          <Divider style={{ borderBottom: "0.01em solid lightgray" }} />
          <Grid container spacing={2}>
            <Grid item xs={2}>
              <Stack
                direction="row"
                alignItems="center"
                justifyContent="space-between"
                sx={{ px: 2, py: 1, bgcolor: "background.default" }}
              >
                <MenuList style={{ borderRight: "0.01em solid lightgray" }}>
                  <MenuItem>
                    <Typography onClick={() => setSubPage("Bookings")}>Bookings</Typography>
                  </MenuItem>
                  <MenuItem>
                    <Typography onClick={() => setSubPage("Certificates")}>Certificates</Typography>
                  </MenuItem>
                </MenuList>
              </Stack>
            </Grid>
            <Grid item xs={10}>
              {subPage === "Bookings" ? <Bookings /> : <Certificates />}
            </Grid>
          </Grid>
        </Box>
      </Card>
    </Container>
  );
}

