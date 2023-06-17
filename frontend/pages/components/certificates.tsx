import { Box, Container, Card, Typography, Stack, Button, Grid, Avatar } from "@mui/material";
import { useEffect, useState } from "react";
import { useSession } from "next-auth/react";
import { CertificateInfo, AircraftUser } from "../types";
import { useRouter } from "next/router";

import TextSnippetOutlinedIcon from "@mui/icons-material/TextSnippetOutlined";

export default function Certificates() {
    const { data: session } = useSession();
    const aircraftUser = session?.user as AircraftUser | undefined;

    const [certificates, setCertificates] = useState(Array<CertificateInfo>);
    const router = useRouter();

    useEffect(() => {
        if (aircraftUser) {
            fetch("http://localhost:8069/user/certificates", {
                method: "GET",
                headers: {
                    "Authorization": `Bearer ${aircraftUser?.jwttoken}`,
                    "User-Agent": "undici"
                },
            })
                .then(response => {
                    if (response.ok) {
                        return response.json();
                    } else {
                        throw response;
                    }
                })
                .then(certificateList => {
                    setCertificates(certificateList);
                });
        }

        // blahblah eslint momen
        // eslint-disable-next-line react-hooks/exhaustive-deps
    }, [aircraftUser]);

    const downloadCert = (certificate: CertificateInfo) => {
        const anchor = document.createElement("a");
        document.body.appendChild(anchor); // ?!

        fetch(certificate.certificateFile, {
            headers: {
                "Authorization": `Bearer ${aircraftUser?.jwttoken}`,
                "User-Agent": "undici",
            }
        })
            .then(response => {
                if (response.ok) {
                    return response.blob();
                } else {
                    throw response;
                }
            })
            .then(blobby => {
                const objectUrl = window.URL.createObjectURL(blobby);

                anchor.href = objectUrl;
                anchor.download = certificate.fileName;
                anchor.click();

                window.URL.revokeObjectURL(objectUrl);
            });
    };

    return (
        <Container component="main" maxWidth="md">
            {
                certificates.map(certificate => (
                    <Card key={certificate.certificateId} sx={{ mt: 2, padding: 2 }}>
                        <Grid container rowSpacing={1} columnSpacing={{ xs: 1, sm: 2, md: 3 }}>
                            <Grid item xs={6}>
                                <Box sx={{ p: 2, display: "flex", flexDirection: "row", alignItems: "left" }}>
                                    <Avatar className="logo" sx={{ m: 2, bgcolor: "grey" }}>
                                        <TextSnippetOutlinedIcon />
                                    </Avatar>
                                    <Stack spacing={0.5} width="90%" sx={{ ml: "4vw" }}>
                                        <Typography fontWeight={700}>{certificate.certificateName}</Typography>
                                        <Typography variant="body2" color="text.secondary">Date Acquired: {certificate.expiryDate.replace("T", ", ")}</Typography>
                                        <Typography variant="body2" color="text.secondary">File Type: {certificate.fileType.replace(/.*\//, "")}</Typography>
                                    </Stack>
                                </Box>
                                <Stack spacing={0.5} width="90%" sx={{ ml: "4vw" }}>

                                </Stack>
                            </Grid>
                            <Grid item xs={6} style={{ textAlign: "center" }}>
                                <Button variant="contained" onClick={() => downloadCert(certificate)}>Download</Button>
                            </Grid>
                        </Grid>
                    </Card>
                ))
            }
            <Button variant="contained" sx={{ width: "150px", mt: 3 }}
                onClick={() => {
                    router.push("/createCertificate");
                }}>Create Certificate</Button>
        </Container>
    );
}
