import './App.css';
import 'bootstrap/dist/css/bootstrap.min.css'
import Login from './components/login/Login';
import Navbar from './components/navbar/navbar';


function App() {
  return (
    <div className="App">
      <Navbar></Navbar>
      <Login />
    </div>
  );
}

export default App;
