import { useSession, signOut } from "../node_modules/next-auth/react"
import { useRouter } from 'next/router';
import Link from 'next/link'

export default function About() {
  const { data: session } = useSession()
  const router = useRouter();

  return (
    <>
      {session ?
        <>
          Signed in as {session.user.email} <br />
          <button onClick={() => {
            signOut({
              redirect: false
            })
            router.push('/login');
          }}>Log out</button>
        </> :
        <>
          Not signed in <br />
          <Link href="/login"> <button>Log in</button> </Link>
        </>
      }
    </>
  )
}
