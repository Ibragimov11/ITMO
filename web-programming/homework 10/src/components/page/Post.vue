<template>
    <div>
        <ViewPost :post="post"
                  :countComments="numberOfComments(post)"
                  :user="findUser(post)"
                  :key="post.id"/>

        <div class="comment" v-for="comment in comments" :key="comment.id">
            {{findUser(comment).name}}: {{comment.text}}
        </div>

    </div>
</template>

<script>
import ViewPost from "@/components/page/ViewPost";

export default {
    name: "Post",
    components: {ViewPost},
    props: ["post", "comments", "findUser", "numberOfComments"]
}
</script>

<style scoped>

</style>