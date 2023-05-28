import { signOut, useSession } from "next-auth/react";
import Link from "next/link";
import { useRouter } from "next/router";

import { Alert, Box, Button, Card, Container, Typography, Grid } from "@mui/material";

export default function About() {
  const { data: session } = useSession();
  const router = useRouter();

  return (
    <Container component="main" maxWidth="xs">
      <Card elevation={6} sx={{
        padding: 4,
        borderRadius: 4
      }}>
        {session ?
          <Box
            sx={{
              display: "flex",
              flexDirection: "column",
              alignItems: "center",
              justifyContent: "space-around"
            }}
          >
            {/* Display user info */}
            <Button variant="contained"
              fullWidth
              onClick={() => {
                signOut({
                  redirect: false
                });
                router.push("/login");
              }}>Log out</Button>
          </Box> :
          <>
            <Alert severity="error" sx={{
              mb: 3,
              borderRadius: 2
            }}>
              Please log in to view profile
            </Alert>
            <Link href="/login">
              <Button fullWidth variant="contained">Log in</Button>
            </Link>
          </>
        }
      </Card>
    </Container>
  );
}

