import Footer from "./footer";
import Navbar from "./navbar";

import { Box, Container, CssBaseline } from "@mui/material";

const Layout = ({ children }:
    {
        children: React.ReactNode | null
    }) => {
    return (
        <Box
            sx={{
                display: "flex",
                flexDirection: "column",
                minHeight: "100vh",
            }}
        >
            <CssBaseline />
            <Container component="main" sx={{ mt: 2, mb: 2 }} >
                <Navbar />
                {children}
            </Container>
            <Box
                component="footer"
                sx={{
                    py: 3,
                    px: 2,
                    mt: "auto",
                    backgroundColor: (theme) =>
                        theme.palette.mode === "light"
                            ? theme.palette.grey[200]
                            : theme.palette.grey[800],
                }}
            >
                <Container maxWidth="sm">
                    <Footer />
                </Container>
            </Box>
        </Box>
    );
};

export default Layout;
