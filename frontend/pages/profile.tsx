import { signOut, useSession } from "next-auth/react";
import Link from 'next/link';
import { useRouter } from 'next/router';

import { Alert, Box, Button, Card, Container, Typography } from "@mui/material";

export default function About() {
  const { data: session } = useSession()
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
              display: 'flex',
              flexDirection: 'column',
              alignItems: 'center',
              justifyContent: 'space-around'
            }}
          >
            <Typography component="h1" variant="h5" sx={{
              mb: 3
            }}>
              Welcome, {session.user?.email}!
            </Typography>
            <Alert severity="warning" sx={{
              mb: 3,
              borderRadius: 2
            }}>
              TODO: Flesh out this page
            </Alert>
            <Button variant="contained"
              fullWidth
              onClick={() => {
                signOut({
                  redirect: false
                })
                router.push('/login');
              }}>Log out</Button>
          </Box> :
          <>
            <Alert severity="error" sx={{
              mb: 3,
              borderRadius: 2
            }}>
              Cannot view profile - Not logged in!
            </Alert>
            <Alert severity="warning" sx={{
              mb: 3,
              borderRadius: 2
            }}>
              TODO: WE DONT NEED THIS ERROR, THE PAGE/TAB/ETC CAN BE HIDDEN FOR LOGGED OUT USERS, GET ON IT FRONTEND LADS
            </Alert>
            <Link href="/login">
              <Button fullWidth variant="contained">Log in</Button>
            </Link>
          </>
        }
      </Card>
    </Container>
  )
}

