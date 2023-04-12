import FlightTakeoffOutlinedIcon from "@mui/icons-material/FlightTakeoffOutlined";
import { Avatar, Tab, Tabs } from "@mui/material";
import Link from "next/link";

const Navbar = () => (
    <nav>
        <Avatar className='logo' sx={{ m: 1, bgcolor: "primary.main" }}>
            <FlightTakeoffOutlinedIcon />
        </Avatar>

        <Tabs
            textColor="secondary"
            indicatorColor="secondary"
        >
            <Link href="/">
                <Tab label="Home" />
            </Link>
            <Link href="/profile">
                <Tab label="Profile" />
            </Link>
            <Link href="/login">
                <Tab label="Login" />
            </Link>
            <Link href=""> { /* TODO, also remove disable */ }
                <Tab label="Register" disabled />
            </Link>
        </Tabs>
    </nav>
);

export default Navbar;
