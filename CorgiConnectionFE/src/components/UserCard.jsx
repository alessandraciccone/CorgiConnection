import { Link } from "react-router-dom";
import "../css/UserCard.css";

const UserCard = ({ user }) => {
  return (
    <div className="user-card" key={user.id}>
      <Link to={`/profilo/${user.id}`} className="username-link">
        <strong className="us2">@{user.username}</strong>
      </Link>
    </div>
  );
};

export default UserCard;
