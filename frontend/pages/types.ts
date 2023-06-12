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

type RegisterData = {
  firstName: string,
  lastName: string,
  email: string,
  username: string,
  password: string,
  passwordRep: string,
};

export { type AircraftUser, type Aircraft, type RegisterData };
