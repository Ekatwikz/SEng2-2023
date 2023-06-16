import { Typography } from "@mui/material";
import Spline from "@splinetool/react-spline";

export default function Home() {
  return (
    <>
      <Typography component="h1" variant="h1">
        Here lies your next adventure ~
      </Typography>

      <Spline scene="https://prod.spline.design/OpKyHuf3TmU7MM6C/scene.splinecode" style={{ height: "500px" }} />
    </>
  );
}
