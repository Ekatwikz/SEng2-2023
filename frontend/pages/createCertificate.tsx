import { useState } from "react";

import TextSnippetOutlinedIcon from "@mui/icons-material/TextSnippetOutlined";
import { Avatar, Box, Button, Card, Container, Grid, Stack, TextField, Typography } from "@mui/material";
import { useRouter } from "next/router";
import { useSnackbar } from "notistack";
import { useSession } from "next-auth/react";
import { StaticDateTimePicker } from "@mui/x-date-pickers/StaticDateTimePicker";

import { useFilePicker } from "use-file-picker";

import { AircraftUser } from "./types";

export default function CreateCertificate() {
  const { data: session } = useSession();
  const aircraftUser = session?.user as AircraftUser | undefined;
  const { enqueueSnackbar } = useSnackbar();
  const router = useRouter();

  const [id, setId] = useState(0);
  const [expiryDate, setExpiryDate] = useState("1970-01-01'T'00:00:00");
  const [certificateName, setCertificateName] = useState("");
  const [openFileSelector, { filesContent }] = useFilePicker({
    readAs: "DataURL",
    multiple: false,
    maxFileSize: 50
  });
  const fileContent = filesContent[0];

  return (
    <Container component="main" maxWidth="md">
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
              <TextSnippetOutlinedIcon />
            </Avatar>
            <Typography component="h1" variant="h5">
              Create
            </Typography>
          </Box>
          <Box sx={{ mt: 1 }}>
            <Grid container columnSpacing={2}>
              <Grid item xs={6}>
                <Stack>
                  <TextField
                    margin="normal"
                    required
                    fullWidth
                    id="id"
                    label="ID"
                    name="id"
                    type="number"
                    onChange={e => setId(parseInt(e.target.value))}
                  />

                  <TextField
                    margin="normal"
                    required
                    fullWidth
                    id="certificateName"
                    label="Certificate Name"
                    name="certificateName"
                    onChange={e => setCertificateName(e.target.value)}
                  />

                  <Button
                    variant="contained"
                    onClick={() => openFileSelector()}
                  >Add Image</Button>
                </Stack>
              </Grid>
              <Grid item xs={6}>
                <StaticDateTimePicker
                  value={expiryDate}
                  onChange={newExpiryDate => setExpiryDate(newExpiryDate!)}
                />
              </Grid>
            </Grid>
            <Button
              fullWidth
              variant="contained"
              sx={{ mt: 3, mb: 2 }}
              onClick={() => {
                var data = new FormData();
                data.append("id", id.toString());
                data.append("file", new File([fileContent.content], fileContent.name));
                data.append("certificateName", certificateName);
                data.append("expiryDate", new Date(expiryDate).toISOString().slice(0, 19));

                fetch("http://localhost:8069/user/certificates", {
                  method: "POST",
                  body: data,
                  headers: {
                    "Authorization": `Bearer ${aircraftUser?.jwttoken}`,
                    "User-Agent": "undici"
                  }
                })
                  .then(response => {
                    if (response.ok) {
                      console.log(response.json());
                      enqueueSnackbar("Certificate Creaated", {
                        variant: "success"
                      });
                    } else {
                      console.error(JSON.stringify(response));
                    }
                  });
              }}
            >
              Create Certificate
            </Button>

            <Grid container>
            </Grid>
          </Box>
        </Box>
      </Card>
    </Container>
  );
}
