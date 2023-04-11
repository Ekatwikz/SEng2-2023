import Link from 'next/link'

const Navbar = () => {
    return (
        <nav>
            <div className="logo">
                <h1>logo</h1>
            </div>

            <Link href="/">Home</Link>
            <Link href="/profile">Profile</Link>
            <Link href="/login">Login</Link>
        </nav>
    )
}

export default Navbar;