import NextAuth from "next-auth";
import CredentialsProvider from "next-auth/providers/credentials";
import { AircraftUser } from "@/pages/types";

import { type User } from "next-auth";

let returnedUser: AircraftUser; // I cry

/* eslint new-cap: 0 */ // JUST for this file, since the functions are external!
export default NextAuth({
  providers: [
    CredentialsProvider({
      name: "seng-project-backend",
      credentials: {
        username: {
          label: "username",
          type: "text",
        },
        password: {
          label: "Password",
          type: "password"
        }
      },
      async authorize(credentials) {
        const payload = {
          username: credentials?.username,
          password: credentials?.password,
        };

        const res = await fetch(`http://${process.env.NEXT_PUBLIC_BACKEND_NETWORK_NAME}:${process.env.NEXT_PUBLIC_BACKEND_NETWORK_PORT}/login`, {
          method: "POST",
          body: JSON.stringify(payload),
          headers: {
            "Content-Type": "application/json",
          },
        });

        const user = await res.json();
        if (!res.ok) {
          throw new Error(user.message);
        }

        // If no error and we have user data, return it
        if (res.ok && user) {
          returnedUser = {
            id: user.id,
            username: user.username,
            email: user.email,
            firstName: user.firstName,
            lastName: user.lastName,
            jwttoken: user.jwttoken
          };

          return Promise.resolve(returnedUser as unknown as User); // bad
        }

        // Return null if user data could not be retrieved
        return null;
      },
    })
  ],

  secret: process.env.JWT_SECRET,
  pages: {
    signIn: "/login",
  },

  callbacks: {
    async jwt({ token, user, account }) {
      if (account && user) {
        return {
          ...token,
          accessToken: token, // ??
          //refreshToken: user.refreshToken, //  ??
        };
      }

      return token;
    },

    async session({ session }) {
      if (session.user) {
        console.log(`Session: ${session}`);

        const aircraftUser = session.user as AircraftUser;
        aircraftUser.firstName = returnedUser.firstName;
        aircraftUser.lastName = returnedUser.lastName;
        aircraftUser.jwttoken = returnedUser.jwttoken;
        session.user = aircraftUser;
      }

      return session;
    },
  },

  theme: {
    colorScheme: "auto", // "auto" | "dark" | "light"
    brandColor: "", // Hex color code #33FF5D
    logo: "/logo.png", // Absolute URL to image
  },

  // Enable debug messages in the console if you are having problems
  debug: process.env.NODE_ENV === "development",
});

