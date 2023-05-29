import SearchIcon from "@mui/icons-material/Search";
import { LocalizationProvider, DatePicker } from "@mui/x-date-pickers";
import { AdapterDayjs } from "@mui/x-date-pickers/AdapterDayjs";
import { Typography, Box, Grid, Button, FormControl, InputLabel, Select, MenuItem, Divider, RadioGroup, FormControlLabel, Radio, TextField } from "@mui/material";

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
              <FormControl fullWidth sx={{bgcolor: "white"}}>
                <InputLabel id="passengersSelect">Passangers</InputLabel>
                <Select
                  labelId="passangersSelect"
                  id="passangersSelect"
                  // value={age}
                  label="Passanger"
                  // onChange={handleChange}
                >
                  <MenuItem sx={{bgcolor: "white", borderRadius: "5px", padding: "5px", margin: "5px"}}>
                      <TextField inputMode="numeric" margin="none" label="Adults" defaultValue={1} variant="standard" fullWidth/>
                  </MenuItem>
                  <MenuItem sx={{bgcolor: "white", borderRadius: "5px", padding: "5px", margin: "5px"}}>
                      <TextField inputMode="numeric" margin="none" label="Children" defaultValue={0} variant="standard" fullWidth/>
                  </MenuItem>
                  <Divider/>
                  <MenuItem sx={{bgcolor: "white", borderRadius: "5px", padding: "5px", margin: "5px"}}>
                    <FormControl>
                      <RadioGroup defaultValue="economy" >
                        <FormControlLabel value="economy" control={<Radio />} label="Economy" />
                        <FormControlLabel value="comfort" control={<Radio />} label="Comfort" />
                        <FormControlLabel value="bussiness" control={<Radio />} label="Bussines" />
                      </RadioGroup>
                    </FormControl>
                  </MenuItem>
                </Select>
              </FormControl>
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
