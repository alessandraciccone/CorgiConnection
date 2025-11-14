import PostCard from "./PostCard";
import "../css/PostList.css";

const PostList = ({ posts, onRefresh }) => {
  return (
    <div className="post-list">
      {posts.map((post) => (
        <PostCard key={post.id} post={post} onRefresh={onRefresh} />
      ))}
    </div>
  );
};

export default PostList;
