import Navbar from "./navbar";
import Footer from "./footer";

const Layout = ({ children }) => {
    return (
            <div className="content">
                <Navbar />
                    {children}
                    <br/><br/><br/> {/* TODO: stop using br lol */}
                <Footer />
            </div>
    );
}

export default Layout;