import "@/styles/globals.css";
import CssBaseline from "@mui/material/CssBaseline";
import { createTheme, ThemeProvider } from "@mui/material/styles";
import type { AppProps } from "next/app";
import { SnackbarProvider } from "notistack";
import Layout from "../components/layout";
import { SessionProvider } from "../node_modules/next-auth/react";

import { LocalizationProvider, DatePicker } from "@mui/x-date-pickers";
import { AdapterDayjs } from "@mui/x-date-pickers/AdapterDayjs";

import "@fontsource/roboto/300.css";
import "@fontsource/roboto/400.css";
import "@fontsource/roboto/500.css";
import "@fontsource/roboto/700.css";

const theme = createTheme();

export default function App({ Component,
  pageProps: { session, ...pageProps },
}: AppProps) {
  console.log(session);
  return (
    <SessionProvider session={session}>
      <LocalizationProvider dateAdapter={AdapterDayjs}>
        <ThemeProvider theme={theme}>
          <SnackbarProvider maxSnack={2}>
            <Layout>
              <CssBaseline />
              <Component {...pageProps} />
            </Layout>
          </SnackbarProvider>
        </ThemeProvider>
      </LocalizationProvider>
    </SessionProvider>
  );
}
