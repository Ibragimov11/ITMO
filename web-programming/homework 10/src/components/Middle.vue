<template>
    <div class="middle">
        <Sidebar :posts="viewPosts"/>
        <main>
            <Index v-if="page === 'Index'" :posts="posts" :findUser="findUser" :numberOfComments="numberOfComments"/>
            <Enter v-if="page === 'Enter'"/>
            <Register v-if="page === 'Register'"/>
            <WritePost v-if="page === 'WritePost'"/>
            <EditPost v-if="page === 'EditPost'"/>
            <Users v-if="page === 'Users'" :users="users"/>
            <Post v-if="page === 'Post'"
                  :post="post"
                  :comments="Object.values(this.comments).filter(comment => comment.postId === post.id)"
                  :findUser="findUser"
                  :numberOfComments="numberOfComments"
            />
        </main>
    </div>
</template>

<script>
import Sidebar from "./sidebar/Sidebar";
import Index from "./page/Index";
import Enter from "./page/Enter";
import WritePost from "./page/WritePost";
import EditPost from "./page/EditPost";
import Users from "./page/Users";
import Register from "@/components/page/Register";
import Post from "@/components/page/Post";

export default {
    name: "Middle",
    data: function () {
        return {
            page: "Index",
            post: null
        }
    },
    components: {
        Post,
        Register,
        Users,
        WritePost,
        Enter,
        Index,
        Sidebar,
        EditPost
    },
    props: ["users", "posts", "comments"],
    methods: {
        findUser: function (entity) {
            return Object.values(this.users).find((user) => user.id === entity.userId);
        },
        numberOfComments: function (post) {
            return Object.values(this.comments).filter(comment => comment.postId === post.id).length
        },
    },
    computed: {
        viewPosts: function () {
            return Object.values(this.posts).sort((a, b) => b.id - a.id).slice(0, 2);
        }
    },
    beforeCreate() {
        this.$root.$on("onChangePage", (page) => this.page = page)
        this.$root.$on("onPost", (post) => {
            this.page = "Post";
            this.post = post
        })
    }
}
</script>

<style scoped>

</style>
