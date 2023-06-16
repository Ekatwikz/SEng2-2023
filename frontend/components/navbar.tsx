import FlightTakeoffOutlinedIcon from "@mui/icons-material/FlightTakeoffOutlined";
import FaceIcon from "@mui/icons-material/Face";
import { Avatar, Tab, Tabs } from "@mui/material";
import Link from "next/link";

export default function Navbar() {
    return (
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

                <Link href="/aircrafts">
                    <Tab label="Aircrafts" />
                </Link>

                <Link href="/profile">
                    <Avatar className='logo' sx={{ m: 1, bgcolor: "grey" }}>
                        <FaceIcon />
                    </Avatar>
                </Link>
            </Tabs>
        </nav>
    );
};
