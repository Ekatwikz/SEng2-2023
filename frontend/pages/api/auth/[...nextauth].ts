import NextAuth from "next-auth";
import CredentialsProvider from "next-auth/providers/credentials";

// TODO: USE PROPER TYPESCRIPT IN THIS FILE!!

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
      async authorize(credentials, req) {
        const payload = {
          username: credentials?.username,
          password: credentials?.password,
        };

        const res = await fetch(`http://${process.env.NEXT_PUBLIC_BACKEND_NETWORK_NAME}:${process.env.NEXT_PUBLIC_BACKEND_NETWORK_PORT}/logic/api/auth/login`, {
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
          return user;
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

    async session({ session, token }) {
      if (session.user) {
        //session.user.accessToken = token.accessToken; // ??
        //session.user.refreshToken = token.refreshToken; // ??
        //session.user.accessTokenExpires = token.accessTokenExpires; // ??
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

