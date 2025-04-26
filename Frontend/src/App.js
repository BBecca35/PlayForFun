import { Routes, Route, Navigate } from "react-router-dom";
import './App.css';
import 'bootstrap/dist/css/bootstrap.min.css';
import Layout from "./components/Layout";
import Login from "./components/login/Login";
import Register from "./components/register/Register";
import Home from "./components/home/Home";
import MyGameDescriptions from "./components/my-game-descriptions/MyGameDescriptions";
import AddNewDescription from "./components/add-new-description/AddNewDescription";
import EditDescription from "./components/edit-description/EditDescription";
import Description from "./components/description/Description";
import Settings from "./components/settings/Settings";
import PersistLogin from "./components/persistLogin";
import RequireAuth from "./components/requireAuth";
import UserManagement from "./components/user-management/userManagement";
import BanUser from "./components/ban-user/banUser";
import Unauthorized from "./components/unauthorized/unauthorized";
import NotFoundPage from "./components/not-found-page/notFoundPage";
import ManageUser from "./components/manage-user/manageUser";
import EditBan from "./components/edit-ban/editBan";


const ROLES = {
  User: "USER",
  Moderator:"MODERATOR",
  Admin: "ADMIN",
};

function App() {
  return (
    <Routes>
      <Route path="/" element={<Layout />}>
        <Route path="login" element={<Login />} />
        <Route path="register" element={<Register />} />
        <Route path="unauthorized" element={<Unauthorized />} />
      
        <Route element={<PersistLogin />}>
          <Route element={<RequireAuth allowedRoles={[ROLES.User, ROLES.Admin, ROLES.Moderator]} />}>
            <Route path="/" element={<Navigate to="/home" replace />} />
            <Route path="home" element={<Home />} />
            <Route path="add-new-description" element={<AddNewDescription />} />
            <Route path="my-game-descriptions" element={<MyGameDescriptions />} />
            <Route path="edit-description" element={<EditDescription />} />
            <Route path="description" element={<Description />} />
            <Route path="settings" element={<Settings />} />          
          </Route>

          <Route element={<RequireAuth allowedRoles={[ROLES.Admin, ROLES.Moderator]} />}>
            <Route path="user-management" element={<UserManagement />} />
            <Route path="manage-user" element={<ManageUser />}/>
            <Route path="ban-user" element={<BanUser />} />
            <Route path="edit-ban" element={<EditBan />}/>
          </Route>
        </Route>

        <Route path="*" element={<NotFoundPage />}/>
      </Route>
    </Routes>
  );
}

export default App;