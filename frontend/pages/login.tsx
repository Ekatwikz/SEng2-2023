import LockOutlinedIcon from '@mui/icons-material/LockOutlined';

import { Avatar, Box, Button, Card, Container, Grid, Link, TextField, Typography } from '@mui/material';

import { signIn } from "next-auth/react";
import { useRouter } from 'next/router';

import { useSnackbar } from 'notistack';

// Adjusted template from: https://github.com/mui/material-ui/tree/v5.12.0/docs/data/material/getting-started/templates/sign-in

export default function SignIn() {
  const { enqueueSnackbar } = useSnackbar();
  const router = useRouter();

  const handleSubmit = async (event: React.FormEvent<HTMLFormElement>) => {
    event.preventDefault();
    const data = new FormData(event.currentTarget);

    const result = await signIn('credentials', {
      username: data.get('username'),
      password: data.get('password'),
      redirect: false
    });

    // TODO: move this somewhere else!!!!
    // UI should not be concerned with bizwax
    if (!result || result.error || result.status != 200) {
      // Handle authentication error
      console.error(result); // TODO: Replace with (or add) "login failed message/popup" or something

      enqueueSnackbar('Login Failed [Example Snack, Y\'all can remove]', {
        key: 'loginFail',
        variant: 'error'
      });
    } else {
      // Handle successful authentication
      console.log(result);
      router.push('/profile');

      enqueueSnackbar('Login Successful [Example Snack, Y\'all can remove]', {
        key: 'loginSucess',
        variant: 'success'
      });
    }
  };

  return (
    <Container component="main" maxWidth="xs">
      <Card elevation={6} sx={{
        padding: 5,
        borderRadius: 4
      }}>
        <Box
          sx={{
            display: 'flex',
            flexDirection: 'column',
            alignItems: 'center',
          }}
        >
          <Box sx={{
            m: 5
          }}>
            <Avatar sx={{ m: 1, bgcolor: 'secondary.main' }}>
              <LockOutlinedIcon />
            </Avatar>
            <Typography component="h1" variant="h5">
              Log In
            </Typography>
          </Box>
          <Box component="form" onSubmit={handleSubmit} noValidate sx={{ mt: 1 }}>
            <TextField
              margin="normal"
              required
              fullWidth
              id="username"
              label="Username"
              name="username"
              autoComplete="username"
              autoFocus
            />
            <TextField
              margin="normal"
              required
              fullWidth
              name="password"
              label="Password"
              type="password"
              id="password"
              autoComplete="current-password"
            />
            <Button
              type="submit"
              fullWidth
              variant="contained"
              sx={{ mt: 3, mb: 2 }}
            >
              Log In
            </Button>

            <Grid container>
              <Grid item>
                <Link href="#" variant="body2">
                  {"Don't have an account? Register! [TODO]"}
                </Link>
              </Grid>
            </Grid>
          </Box>
        </Box>
      </Card>
    </Container >
  );
}
