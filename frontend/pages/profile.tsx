import { signOut, useSession } from "next-auth/react";
import { useRouter } from "next/router";

import { Box, Button, Card, Container, TextField, Typography } from "@mui/material";
import { userAgent } from "next/server";

import { AircraftUser } from "./types";

export default function About() {
  const { data: session } = useSession();
  const router = useRouter();


  if (!session) {
    router.push("/login");
  }
  return (
    <Container component="main" maxWidth="xs">
      <Card elevation={6} sx={{
        padding: 4,
        borderRadius: 4
      }}>
        <Box
          sx={{
            display: "flex",
            flexDirection: "column",
            alignItems: "center",
            justifyContent: "space-around"
          }}
        >
          {/* Display user info */}
          <Typography component="h1" variant="h5" sx={{ mb: 3 }}>email: {session?.user?.email}</Typography>
          <Typography component="h1" variant="h5" sx={{ mb: 3 }}>firstame : {(session?.user as AircraftUser | undefined)?.firstName}</Typography>
          <Typography component="h1" variant="h5" sx={{ mb: 3 }}>lastname : {(session?.user as AircraftUser | undefined)?.lastName}</Typography>
          <Button variant="contained"
            fullWidth
            onClick={() => {
              signOut({
                redirect: false
              });
              router.push("/login");
            }}>Log out</Button>
        </Box>
      </Card>
    </Container>
  );
}

