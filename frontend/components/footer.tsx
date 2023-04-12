import { Link, Typography } from '@mui/material'

const Footer = (props: any) => (
  <Typography variant="body2" color="text.secondary" align="center" {...props}>
    {'Copyright © '}
    <Link color="inherit" href="https://spages.mini.pw.edu.pl/~katwikirizee/">
      Certified Bruh Moments Inc.
    </Link>{' '}
    {new Date().getFullYear()}
  </Typography>
);

export default Footer;
