import { Dayjs } from "dayjs";

type AircraftUser = {
  id: number,
  username: string,
  email: string,
  firstName: string
  lastName: string,
  jwttoken: string
};

type Aircraft = {
  aircraftId: number,
  aircraftName: string,
  aircraftType: string,
  aircraftRegistration: string,
  aircraftDescription: string,
};

type AircraftImage = {
  aircraftId: number,
  aircraftImageId: number,
  fileName: string,
  fileType: string,
  fileSize: number,
};

type RegisterData = {
  firstName: string,
  lastName: string,
  email: string,
  username: string,
  password: string,
  passwordRep: string,
};

type BookData = {
  aircraftId: number,
  bookingStartDateTime: Dayjs | null,
  bookingEndDateTime: Dayjs | null
}

type Booking = {
  bookingId: number,
  aircraftId: number,
  bookingStartDateTime: Dayjs,
  bookingEndDateTime: Dayjs
}

type CertificateInfo = {
  certificateId: number,
  certificateName: string,
  expiryDate: string,
  ownerId: number,
  fileName: string,
  fileSize: number,
  fileType: string,
  certificateFile: string,
}

export { type AircraftUser, type Aircraft, type AircraftImage, type RegisterData, type BookData, type Booking, type CertificateInfo };
