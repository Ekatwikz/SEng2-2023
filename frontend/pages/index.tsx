import SearchIcon from '@mui/icons-material/Search';
import { LocalizationProvider, DatePicker } from '@mui/x-date-pickers';
import { AdapterDayjs } from '@mui/x-date-pickers/AdapterDayjs';
import { Typography, Box, Grid, Button } from "@mui/material";

export default function Home() {
  return (
    <>
      <Typography component="h1" variant="h1">
        Here lies your next adventure ~
      </Typography>

      <Box sx={{ 
        display: "flex",
        flexDirection: "column",
        alignTypographys: "center",
        justifyContent: "space-around",
        margin: "100px 0 0 0 "}}>
        
      <LocalizationProvider dateAdapter={AdapterDayjs}>
          <Grid container>
            <Grid xs={2} sx={{bgcolor: "white", borderRadius: "5px", padding: "5px", margin: "5px"}}>
              <Typography>From:</Typography>
            </Grid>
            <Grid xs={2} sx={{bgcolor: "white", borderRadius: "5px", padding: "5px", margin: "5px"}}>
              <Typography>To:</Typography>
            </Grid>
            <Grid xs={2} sx={{bgcolor: "white", borderRadius: "5px", padding: "5px", margin: "5px"}}>
              <DatePicker label="From:" />
            </Grid>
            <Grid xs={2} sx={{bgcolor: "white", borderRadius: "5px", padding: "5px", margin: "5px"}}>
            <DatePicker label="From:" />
            </Grid>
            <Grid xs={2} sx={{bgcolor: "white", borderRadius: "5px", padding: "5px", margin: "5px"}}>
              <Typography>Passengers</Typography>
            </Grid>
            <Grid sx={{borderRadius: "5px", padding: "5px", margin: "5px"}}>
              <Button variant="contained" endIcon={<SearchIcon />}>Search</Button>
            </Grid>
          </Grid>
        </LocalizationProvider>
      </Box>

    </>
  );
}
