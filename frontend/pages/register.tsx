import LockOutlinedIcon from "@mui/icons-material/LockOutlined";
import { Avatar, Box, Button, Card, Container, Grid, Link, TextField, Typography } from "@mui/material";
import { useRouter } from "next/router";
import { useSnackbar } from "notistack";

import { auth } from "./api/auth/register";
import { signIn } from "next-auth/react";

export class RegisterData {
    email: string;
    username: string;
    password: string;
    passwordRep: string;

    constructor(email: string, username: string, password: string, passwordRep: string) {
        this.email = email;
        this.username = username;
        this.password = password;
        this.passwordRep = passwordRep;
    }
}

export default function Register() {
  const { enqueueSnackbar } = useSnackbar();
  const router = useRouter();

  const handleSubmit = async (event: React.FormEvent<HTMLFormElement>) => {
    event.preventDefault();
    const data = new FormData(event.currentTarget);

    const result = new RegisterData(
      data.get("email")!.toString(),
      data.get("username")!.toString(),
      data.get("password")!.toString(),
      data.get("passwordRep")!.toString(),
    );

    if (await auth(result)) {
        await signIn("credentials", {
            username: result.username,
            password: result.password,
            redirect: false
          });
        router.push("/profile");
    } else {
        enqueueSnackbar("Registration Failed", {
            key: "registerFail",
            variant: "error"
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
            display: "flex",
            flexDirection: "column",
            alignItems: "center",
          }}
        >
          <Box sx={{
            m: 5
          }}>
            <Avatar sx={{ m: 1, ml: 3, bgcolor: "secondary.main" }}>
              <LockOutlinedIcon />
            </Avatar>
            <Typography component="h1" variant="h5">
              Register
            </Typography>
          </Box>
          <Box component="form" onSubmit={handleSubmit} noValidate sx={{ mt: 1 }}>
            <TextField
              margin="normal"
              required
              fullWidth
              id="email"
              label="Email"
              name="email"
              type="email"
            />
            <TextField
              margin="normal"
              required
              fullWidth
              id="username"
              label="Username"
              name="username"
            />
            <TextField
              margin="normal"
              required
              fullWidth
              name="password"
              label="Password"
              type="password"
              id="password"
            />
            <TextField
              margin="normal"
              required
              fullWidth
              name="passwordRep"
              label="Repeat Password"
              type="password"
              id="passwordRep"
            />
            <Button
              type="submit"
              fullWidth
              variant="contained"
              sx={{ mt: 3, mb: 2 }}
            >
              Register
            </Button>

            <Grid container>
              <Grid item>
                <Link href="/login" variant="body2">
                  {"Already have an account? Log In"}
                </Link>
              </Grid>
            </Grid>
          </Box>
        </Box>
      </Card>
    </Container >
  );
}
