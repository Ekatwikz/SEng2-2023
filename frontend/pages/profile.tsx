import { signOut, useSession } from "next-auth/react";
import { useRouter } from "next/router";

import { Box, Button, Card, Container } from "@mui/material";

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

