import FlightTakeoffOutlinedIcon from '@mui/icons-material/FlightTakeoffOutlined';
import { Avatar, Tab, Tabs } from '@mui/material';
import Link from 'next/link';

const Navbar = () => (
    <nav>
        <Avatar className='logo' sx={{ m: 1, bgcolor: 'primary.main' }}>
            <FlightTakeoffOutlinedIcon />
        </Avatar>

        <Tabs
            textColor="secondary"
            indicatorColor="secondary"
        >
            <Link href="/">
                <Tab value="two" label="Home" />
            </Link>
            <Link href="/profile">
                <Tab value="one" label="Profile" />
            </Link>
            <Link href="/login">
                <Tab value="three" label="Login" />
            </Link>
            <Link href=""> { /* TODO, also remove disable */ }
                <Tab value="one" label="Register" disabled />
            </Link>
        </Tabs>
    </nav>
)

export default Navbar;
