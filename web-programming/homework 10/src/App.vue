<template>
    <div id="app">
        <Header :userId="userId" :users="users"/>
        <Middle :posts="posts" :users="users" :comments="comments"/>
        <Footer :countUsers="Object.keys(users).length" :countPosts="Object.keys(posts).length"/>
    </div>
</template>

<script>
import Header from "./components/Header";
import Middle from "./components/Middle";
import Footer from "./components/Footer";

export default {
    name: 'App',
    components: {
        Footer,
        Middle,
        Header
    },
    data: function () {
        return this.$root.$data;
    },
    beforeCreate() {
        this.$root.$on("onEnter", (login, password) => {
            if (password === "") {
                this.$root.$emit("onEnterValidationError", "Password is required");
                return;
            }

            const users = Object.values(this.users).filter(u => u.login === login);
            if (users.length === 0) {
                this.$root.$emit("onEnterValidationError", "No such user");
            } else {
                this.userId = users[0].id;
                this.$root.$emit("onChangePage", "Index");
            }
        });

        this.$root.$on("onRegister", (login, name) => {
            const trimmedLogin = login.trim()
            const trimmedName = name.trim()
            if (trimmedName.length < 1 || trimmedName.length > 32) {
                this.$root.$emit("onRegisterValidationError", "Name length should be in [1:32]");
                return;
            }
            if (trimmedLogin.length < 3 || trimmedLogin.length > 16) {
                this.$root.$emit("onRegisterValidationError", "Login length should be in [3:16]");
                return;
            }
            if (!/^[a-z]+$/.test(trimmedLogin)) {
                this.$root.$emit("onRegisterValidationError", "Login must contain only english lowercase letters");
                return;
            }
            const users = Object.values(this.users).filter(u => u.login === trimmedLogin);
            if (users.length !== 0) {
                this.$root.$emit("onRegisterValidationError", "Login is already in use");
            } else {
                const id = Math.max(...Object.keys(this.users)) + 1;
                this.$root.$set(this.users,
                    id, {
                        id: id, login: trimmedLogin, name: trimmedName, admin: false
                    }
                );
                this.$root.$emit("onChangePage", "Enter");
            }
        });

        this.$root.$on("onLogout", () => this.userId = null);

        this.$root.$on("onPost", (post) => {
            this.postId = post.id;
            this.$root.$emit("onChangePage", "Post");
        });

        this.$root.$on("onWritePost", (title, text) => {
            if (this.userId) {
                if (!title || title.length < 5) {
                    this.$root.$emit("onWritePostValidationError", "Title is too short");
                } else if (!text || text.length < 10) {
                    this.$root.$emit("onWritePostValidationError", "Text is too short");
                } else {
                    const id = Math.max(...Object.keys(this.posts)) + 1;
                    this.$root.$set(this.posts, id, {
                        id, title, text, userId: this.userId
                    });
                }
            } else {
                this.$root.$emit("onWritePostValidationError", "No access");
            }
        });

        this.$root.$on("onEditPost", (id, text) => {
            if (this.userId) {
                if (!id) {
                    this.$root.$emit("onEditPostValidationError", "ID is invalid");
                } else if (!text || text.length < 10) {
                    this.$root.$emit("onEditPostValidationError", "Text is too short");
                } else {
                    let posts = Object.values(this.posts).filter(p => p.id === parseInt(id));
                    if (posts.length) {
                        posts.forEach((item) => {
                            item.text = text;
                        });
                    } else {
                        this.$root.$emit("onEditPostValidationError", "No such post");
                    }
                }
            } else {
                this.$root.$emit("onEditPostValidationError", "No access");
            }
        });
    }
}
</script>

<style>
#app {

}
</style>
