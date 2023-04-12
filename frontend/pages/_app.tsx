import '@/styles/globals.css';
import CssBaseline from '@mui/material/CssBaseline';
import { createTheme, ThemeProvider } from '@mui/material/styles';
import type { AppProps } from 'next/app';
import { SnackbarProvider } from 'notistack';
import Layout from "../components/layout";
import { SessionProvider } from "../node_modules/next-auth/react";

import '@fontsource/roboto/300.css';
import '@fontsource/roboto/400.css';
import '@fontsource/roboto/500.css';
import '@fontsource/roboto/700.css';

const theme = createTheme();

export default function App({ Component,
  pageProps: { session, ...pageProps },
}: AppProps) {
  console.log(session);
  return (
    <SessionProvider session={session}>
      <ThemeProvider theme={theme}>
        <SnackbarProvider>
          <Layout>
            <CssBaseline />
            <Component {...pageProps} />
          </Layout>
        </SnackbarProvider>
      </ThemeProvider>
    </SessionProvider>
  );
}
