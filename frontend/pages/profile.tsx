import { useState, useEffect } from "react";

import { signOut, useSession } from "next-auth/react";
import { useRouter } from "next/router";

import AccountCircleIcon from "@mui/icons-material/AccountCircle";
import { Avatar, Box, Button, Card, Container, Divider, Grid, MenuItem, MenuList, Stack, Typography } from "@mui/material";

import { AircraftUser, CertificateInfo } from "./types";
import Bookings from "./components/bookings";

export default function About() {
  const { data: session } = useSession();
  const aircraftUser = session?.user as AircraftUser | undefined;
  const router = useRouter();
  const [subPage, setSubPage] = useState("Bookings");

  const [certificates, setCertificates] = useState(Array<CertificateInfo>);

  useEffect(() => {
    if (aircraftUser) {
      fetch("http://localhost:8069/user/certificates", {
        method: "GET",
        headers: {
          "Authorization": `Bearer ${aircraftUser?.jwttoken}`,
          "User-Agent": "undici"
        },
      })
        .then(response => {
          if (response.ok) {
            return response.json();
          } else {
            throw response;
          }
        })
        .then(certificateList => {
          setCertificates(certificateList);
        });
    }

    // blahblah eslint momen
    // eslint-disable-next-line react-hooks/exhaustive-deps
  }, [aircraftUser]);

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
                </MenuList>
              </Stack>
            </Grid>
            <Grid item xs={10}>
              {subPage === "Bookings" ? <Bookings/> : <></>}
            </Grid>
          </Grid>
        </Box>
        <Container component="main" maxWidth="md">
          <Typography variant="h4" style={{ textAlign: "center" }}>Certificates</Typography>
          {
            certificates.map(certificate => (
              <Card key={certificate.certificateId} sx={{ mb: 2, padding: 2 }}>
                <Grid container rowSpacing={1} columnSpacing={{ xs: 1, sm: 2, md: 3 }}>
                  <Grid item xs={6} >
                    <Stack spacing={0.5} width="90%" sx={{ ml: "4vw" }}>
                      <Typography fontWeight={700}>{certificate.certificateName}</Typography>
                      <Typography variant="body2" color="text.secondary">Date Acquired: {certificate.expiryDate.replace("T", ", ")}</Typography>
                      <Typography variant="body2" color="text.secondary">File Type: {certificate.fileType.replace(/.*\//, "")}</Typography>
                    </Stack>
                  </Grid>
                  <Grid item xs={6} style={{ textAlign: "center" }}>
                    <Button variant="contained" onClick={() => {
                      let anchor = document.createElement("a");
                      document.body.appendChild(anchor); // ?

                      fetch(certificate.certificateFile, {
                        headers: {
                          "Authorization": `Bearer ${aircraftUser?.jwttoken}`,
                          "User-Agent": "undici",
                        }
                      })
                        .then(response => {
                          if (response.ok) {
                            return response.blob();
                          } else {
                            throw response;
                          }
                        })
                        .then(blobby => {
                          let objectUrl = window.URL.createObjectURL(blobby);

                          anchor.href = objectUrl;
                          anchor.download = certificate.fileName;
                          anchor.click();

                          window.URL.revokeObjectURL(objectUrl);
                        });
                    }}>Download</Button>
                  </Grid>
                </Grid>
              </Card>
            ))
          }
        </Container>
        <Button variant="contained" sx={{ width: "150px", alignSelf: "center", ml: "500px" }}
          onClick={() => {
            router.push("/createCertificate");
          }}>Create Certificate</Button>
      </Card>
    </Container>
  );
}

