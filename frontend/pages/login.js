import Footer from "../components/footer";
import Navbar from "../components/navbar";

export default function About() {
    return (
        <form>
        <label for="username">Username:</label>
        <input type="text" id="username" name="username"/><br/><br/>
        
        <label for="password">Password:</label>
        <input type="password" id="password" name="password"/><br/><br/>
        
        <input type="submit" value="Login"/>
        </form>
    )
  }
  