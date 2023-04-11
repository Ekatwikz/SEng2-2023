import NextAuth from 'next-auth';
import CredentialsProvider from 'next-auth/providers/credentials';

export default NextAuth({
  providers: [
    CredentialsProvider({
      name: 'seng-project-backend',
      credentials: {
        username: {
          label: 'username',
          type: 'text',
        },
        password: {
          label: 'Password',
          type: 'password'
        }
      },
      async authorize(credentials, req) {
        const payload = {
          username: credentials.username,
          password: credentials.password,
        };

        const res = await fetch('http://spring-boot-backend:8080/logic/api/auth/login', {
          method: 'POST',
          body: JSON.stringify(payload),
          headers: {
            'Content-Type': 'application/json',
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
    signIn: '/login',
  },

  callbacks: {
    async jwt({ token, user, account }) {
      if (account && user) {
        return {
          ...token,
          accessToken: user.token,
          refreshToken: user.refreshToken,
        };
      }

      return token;
    },

    async session({ session, token }) {
      session.user.accessToken = token.accessToken;
      session.user.refreshToken = token.refreshToken;
      session.user.accessTokenExpires = token.accessTokenExpires;

      return session;
    },
  },

  theme: {
    colorScheme: 'auto', // "auto" | "dark" | "light"
    brandColor: '', // Hex color code #33FF5D
    logo: '/logo.png', // Absolute URL to image
  },

  // Enable debug messages in the console if you are having problems
  debug: process.env.NODE_ENV === 'development',
});
