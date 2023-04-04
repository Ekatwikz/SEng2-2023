import Navbar from "./navbar";
import Footer from "./footer";

const Layout = ({ children }) => {
    return (
        <div className="content">
            <Navbar></Navbar>
            {children}<br/><br/><br/>
            <Footer></Footer>
        </div>
    );
}

export default Layout;