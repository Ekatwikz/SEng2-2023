import { useState } from 'react';
import { signIn } from "next-auth/react"
import { useRouter } from 'next/router';

export default function LoginPage() {
  const [username, setUser] = useState('');
  const [password, setPassword] = useState('');
  const router = useRouter();

  const handleSubmit = async (e) => {
    e.preventDefault();

    const result = await signIn('credentials', {
      username,
      password,
      redirect: false
    });

    if (result.error) {
      // Handle authentication error
      console.error(result.error); // TODO: Replace with (or add) "login failed message/popup" or something
    } else {
      // Handle successful authentication
      console.log(result);
      router.push('/profile');
    }
  };

  return (
    <form onSubmit={handleSubmit}>
      <label>
        Username:
        <input
          type="text"
          value={username}
          onChange={(e) => setUser(e.target.value)}
        />
      </label>
      <br />
      <label>
        Password:
        <input
          type="password"
          value={password}
          onChange={(e) => setPassword(e.target.value)}
        />
      </label>
      <br />
      <button type="submit">Sign in</button>
    </form>
  );
}
